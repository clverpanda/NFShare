package com.clverpanda.nfshare;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.p2p.WifiP2pInfo;
import android.net.wifi.p2p.WifiP2pManager;
import android.net.wifi.p2p.nsd.WifiP2pDnsSdServiceInfo;
import android.net.wifi.p2p.nsd.WifiP2pDnsSdServiceRequest;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.sdk.android.oss.ClientException;
import com.alibaba.sdk.android.oss.OSS;
import com.alibaba.sdk.android.oss.OSSClient;
import com.alibaba.sdk.android.oss.ServiceException;
import com.alibaba.sdk.android.oss.callback.OSSCompletedCallback;
import com.alibaba.sdk.android.oss.callback.OSSProgressCallback;
import com.alibaba.sdk.android.oss.common.auth.OSSCredentialProvider;
import com.alibaba.sdk.android.oss.common.auth.OSSPlainTextAKSKCredentialProvider;
import com.alibaba.sdk.android.oss.internal.OSSAsyncTask;
import com.alibaba.sdk.android.oss.model.PutObjectRequest;
import com.alibaba.sdk.android.oss.model.PutObjectResult;
import com.clverpanda.nfshare.model.FileInfo;
import com.clverpanda.nfshare.model.TransferData;
import com.clverpanda.nfshare.model.communicate.receive.StartShareRec;
import com.clverpanda.nfshare.model.communicate.send.StartShareSend;
import com.clverpanda.nfshare.receiver.CloudSendBroadcastReceiver;
import com.clverpanda.nfshare.receiver.ConnUnavailableBroadcastReceiver;
import com.clverpanda.nfshare.service.HttpService;
import com.clverpanda.nfshare.tasks.AsyncResponse;
import com.clverpanda.nfshare.tasks.PostShareAsyncTask;
import com.clverpanda.nfshare.util.PropertiesGetter;
import com.hanks.htextview.evaporate.EvaporateTextView;
import com.skyfishjy.library.RippleBackground;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.pedant.SweetAlert.SweetAlertDialog;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by clverpanda on 2017/5/24 0024.
 * It's the file for NFShare.
 */

public class CloudSendActivity extends AppCompatActivity implements WifiP2pManager.ConnectionInfoListener
{
    public static final String DATA_INFO = "com.clverpanda.nfshare.CloudSendActivity.data";
    public static final String TAG = "CloudSendActivity";

    public static final String KEY_PORT = "port";
    public static final String KEY_NAME = "buddyname";
    public static final String KEY_AVAILABLE = "available";


    protected TransferData dataToSend;
    private StartShareRec startShareRec = null;
    private WifiP2pManager mManager;
    private WifiP2pManager.Channel mChannel;
    private BroadcastReceiver receiver = null;
    private BroadcastReceiver msgPushReceiver = null;
    private boolean isWifiP2pEnabled = false;

    protected int mLocalPort = 8080;
    private String mServiceName;

    @BindView(R.id.wifi_animation)
    RippleBackground rippleBackground;
    @BindView(R.id.send_log)
    EvaporateTextView tvLog;
    @BindView(R.id.textView_share)
    TextView tvShareWord;
    @BindView(R.id.tv_share_pin)
    TextView tvSharePin;
    @BindView(R.id.send_progress)
    ProgressBar progressBar;


    private final IntentFilter intentFilter = new IntentFilter();
    private final IntentFilter httpFilter = new IntentFilter();
    private final IntentFilter messagePushFilter = new IntentFilter();

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cloudsend);
        ButterKnife.bind(this);

        addFilterAction();
        addHttpFilterAction();
        addMessagePushFilterAction();

        mManager = (WifiP2pManager) getSystemService(Context.WIFI_P2P_SERVICE);
        mChannel = mManager.initialize(this, getMainLooper(), null);

        getINFO();
    }


    @Override
    public void onResume()
    {
        super.onResume();
        receiver = new CloudSendBroadcastReceiver(mManager, mChannel, this);
        msgPushReceiver = new ConnUnavailableBroadcastReceiver(this);
        registerReceiver(receiver, intentFilter);
        registerReceiver(httpReceiver, httpFilter);
        registerReceiver(msgPushReceiver, messagePushFilter);
        Intent intent = new Intent(this, HttpService.class);
        intent.putExtra(HttpService.HTTP_SHARE_FILEINFO, dataToSend);
        startService(intent);
        mManager.removeGroup(mChannel, null);

        rippleBackground.startRippleAnimation();
    }

    @Override
    public void onPause()
    {
        super.onPause();
        unregisterReceiver(receiver);
        unregisterReceiver(httpReceiver);
        unregisterReceiver(msgPushReceiver);
        mManager.clearLocalServices(mChannel, new WifiP2pManager.ActionListener() {
            @Override
            public void onSuccess()
            {
                Log.d(TAG, "onSuccess: 成功清理服务");
            }

            @Override
            public void onFailure(int reason) {
                Log.e(TAG, "onFailure: 清理服务失败");
            }
        });
        mManager.removeGroup(mChannel, new WifiP2pManager.ActionListener() {
            @Override
            public void onSuccess() {
                Log.d(TAG, "onSuccess: 成功关闭group");
            }

            @Override
            public void onFailure(int reason)
            {
                Log.e(TAG, "onFailure: 关闭group失败");
            }
        });
        rippleBackground.stopRippleAnimation();
    }

    public void confirmUseCloudUpdate(final int id)
    {
        if (startShareRec == null) return;
        if (id != startShareRec.getId()) return;
        final SweetAlertDialog theDialog = new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
                .setTitleText("对方无法连接到此设备")
                .setContentText("是否启用云传输")
                .setCancelText("否")
                .setConfirmText("是")
                .showCancelButton(true);
        theDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
            @Override
            public void onClick(SweetAlertDialog sDialog)
            {
                sendFileToCloud(id);
                theDialog.cancel();
            }
        });
        theDialog.show();
    }

    private void sendFileToCloud(final int id)
    {
        if (startShareRec == null) return;
        if (id == startShareRec.getId())
        {
            progressBar.setVisibility(View.VISIBLE);
            String endpoint = "http://oss-cn-shanghai.aliyuncs.com";
            OSSCredentialProvider credentialProvider = new OSSPlainTextAKSKCredentialProvider("LTAIJ5G4gu5tuOUX",
                    "KMYapV4p7XoBjQ88k9RkbA2AnCwbLl");

            OSS oss = new OSSClient(getApplicationContext(), endpoint, credentialProvider);
            FileInfo fileInfo = JSON.parseObject(dataToSend.getPayload(), FileInfo.class);

            // 构造上传请求
            PutObjectRequest put = new PutObjectRequest("nfshare", fileInfo.getFileName(), fileInfo.getFilePath());

            put.setProgressCallback(new OSSProgressCallback<PutObjectRequest>() {
                @Override
                public void onProgress(PutObjectRequest request, long currentSize, long totalSize)
                {
                    progressBar.setProgress((int) (currentSize / totalSize) * 100);
                }
            });
            OSSAsyncTask task = oss.asyncPutObject(put, new OSSCompletedCallback<PutObjectRequest, PutObjectResult>() {
                @Override
                public void onSuccess(PutObjectRequest request, PutObjectResult result)
                {
                    Toast.makeText(getApplicationContext(), "文件已经上传至云端", Toast.LENGTH_SHORT).show();
                    tvLog.animateText("文件已上传到云端");
                    Log.d("PutObject", "UploadSuccess");
                    reportUploadDone2Server(id);
                }

                @Override
                public void onFailure(PutObjectRequest request, ClientException clientExcepion, ServiceException serviceException)
                {
                    Toast.makeText(getApplicationContext(), "上传至云端失败", Toast.LENGTH_SHORT).show();
                    // 请求异常
                    if (clientExcepion != null) {
                        // 本地异常如网络异常等
                        clientExcepion.printStackTrace();
                    }
                    if (serviceException != null) {
                        // 服务异常
                        Log.e("ErrorCode", serviceException.getErrorCode());
                        Log.e("RequestId", serviceException.getRequestId());
                        Log.e("HostId", serviceException.getHostId());
                        Log.e("RawMessage", serviceException.getRawMessage());
                    }
                }
            });
            //task.waitUntilFinished();
        }
    }

    private void reportUploadDone2Server(final int id)
    {
        new Thread(new Runnable()
        {
            @Override
            public void run()
            {
                OkHttpClient client = new OkHttpClient.Builder()
                        .connectTimeout(3, TimeUnit.SECONDS)
                        .build();
                try
                {
                    URL url = new URL(PropertiesGetter.getUploadDoneCallbackUrl(getApplicationContext()) + id);
                    Request request = new Request.Builder().url(url).build();
                    Response response = client.newCall(request).execute();
                    if (response.isSuccessful())
                    {
                        String result = response.body().string();
                        Log.d(TAG, "服务器：回调" + result);
                    }
                    else
                        Log.d(TAG, "服务器：回调失败");
                }
                catch (IOException e)
                {
                    Log.e(TAG, "服务器：回调失败", e);
                }
            }
        }).start();
    }

    protected void getINFO()
    {
        dataToSend = getIntent().getParcelableExtra(DATA_INFO);
    }

    protected void addFilterAction()
    {
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION);
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION);
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION);
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION);
    }

    protected void addHttpFilterAction()
    {
        httpFilter.addAction(HttpService.ACTION_SERVER_CREATED);
    }

    protected void addMessagePushFilterAction()
    {
        messagePushFilter.addAction(ConnUnavailableBroadcastReceiver.ACTION_CONN_UNAVAILABLE);
    }

    public void setIsWifiP2pEnabled(boolean isWifiP2pEnabled) {
        this.isWifiP2pEnabled = isWifiP2pEnabled;
    }


    //连接到对等点
    @Override
    public void onConnectionInfoAvailable(final WifiP2pInfo info)
    {
        if (info.groupFormed)
        {

        }
    }

    protected void startServiceRegistration()
    {
        Map<String, String> record = new HashMap<>();
        record.put(KEY_PORT, String.valueOf(mLocalPort));
        record.put(KEY_NAME, dataToSend.getDataType().getName());
        record.put(KEY_AVAILABLE, "visible");

        WifiP2pDnsSdServiceInfo serviceInfo =
                WifiP2pDnsSdServiceInfo.newInstance("NFShare_FileShare_service", "_http._tcp", record);

        mManager.addLocalService(mChannel, serviceInfo, new WifiP2pManager.ActionListener()
        {
            @Override
            public void onSuccess() {
                tvLog.animateText("服务创建成功");
                Log.d(TAG, "onSuccess: 服务创建成功");
            }

            @Override
            public void onFailure(int arg0) {
                Log.e(TAG, "onFailure: 服务创建失败");
            }
        });
        mManager.createGroup(mChannel, new WifiP2pManager.ActionListener() {
            @Override
            public void onSuccess() {
                Log.d(TAG, "onSuccess: group创建成功");
            }

            @Override
            public void onFailure(int reason) {
                Log.e(TAG, "onFailure: group创建失败");
            }
        });
        WifiP2pDnsSdServiceRequest serviceRequest = WifiP2pDnsSdServiceRequest.newInstance();
        mManager.addServiceRequest(mChannel, serviceRequest,
                new WifiP2pManager.ActionListener() {

                    @Override
                    public void onSuccess() {
                        Log.d(TAG, "添加service request成功");
                    }

                    @Override
                    public void onFailure(int arg0) {
                        Log.e(TAG, "添加service request失败");
                    }
                });
        mManager.discoverServices(mChannel, new WifiP2pManager.ActionListener() {
            @Override
            public void onSuccess() {
                Log.d(TAG, "onSuccess: 服务广播成功");
            }

            @Override
            public void onFailure(int reason) {
                Log.e(TAG, "onFailure: 服务广播失败");
            }
        });
    }

    protected void startServerCommunication()
    {
        StartShareSend shareInfo = new StartShareSend(dataToSend.getDataType(),
                dataToSend.getPayload(), mLocalPort, getApplicationContext());
        PostShareAsyncTask postShareAsyncTask = new PostShareAsyncTask(getApplicationContext(),
                tvShareWord, tvSharePin);
        postShareAsyncTask.setAsyncResponse(new AsyncResponse<StartShareRec>()
        {
            @Override
            public void onDataReceivedSuccess(StartShareRec listData)
            {
                CloudSendActivity.this.startShareRec = listData;
            }

            @Override
            public void onDataReceivedFailed()
            {

            }
        });
        postShareAsyncTask.execute(shareInfo);
    }

    BroadcastReceiver httpReceiver = new BroadcastReceiver()
    {
        @Override
        public void onReceive(Context context, Intent intent)
        {
            if (HttpService.ACTION_SERVER_CREATED.equals(intent.getAction()))
            {
                mLocalPort = intent.getIntExtra("port", 0);
                startServiceRegistration();
                startServerCommunication();
            }

        }
    };

}
