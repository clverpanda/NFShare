package com.clverpanda.nfshare;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.nsd.NsdManager;
import android.net.wifi.p2p.WifiP2pInfo;
import android.net.wifi.p2p.WifiP2pManager;
import android.net.wifi.p2p.nsd.WifiP2pDnsSdServiceInfo;
import android.net.wifi.p2p.nsd.WifiP2pDnsSdServiceRequest;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.clverpanda.nfshare.model.TransferData;
import com.clverpanda.nfshare.receiver.WiFiSendBroadcastReceiver;
import com.clverpanda.nfshare.service.HttpService;
import com.clverpanda.nfshare.webserver.HttpFileServer;
import com.hanks.htextview.evaporate.EvaporateTextView;
import com.skyfishjy.library.RippleBackground;

import java.net.ServerSocket;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

public class WIFISendActivity extends AppCompatActivity implements WifiP2pManager.ConnectionInfoListener
{
    public static final String DATA_INFO = "com.clverpanda.nfshare.WIFISendActivity.data";
    public static final String TAG = "WIFISendActivity";
    public static final String KEY_PORT = "port";
    public static final String KEY_NAME = "buddyname";
    public static final String KEY_AVAILABLE = "available";


    private TransferData dataToSend;
    private WifiP2pManager mManager;
    private WifiP2pManager.Channel mChannel;
    private BroadcastReceiver receiver = null;
    private boolean isWifiP2pEnabled = false;

    private int mLocalPort = 8080;
    private String mServiceName;

    @BindView(R.id.wifi_animation)
    RippleBackground rippleBackground;
    @BindView(R.id.send_log)
    EvaporateTextView tvLog;


    private final IntentFilter intentFilter = new IntentFilter();
    private final IntentFilter httpFilter = new IntentFilter();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wifisend);
        ButterKnife.bind(this);

        addFilterAction();
        addHttpFilterAction();

        mManager = (WifiP2pManager) getSystemService(Context.WIFI_P2P_SERVICE);
        mChannel = mManager.initialize(this, getMainLooper(), null);

        getINFO();
    }


    @Override
    public void onResume()
    {
        super.onResume();
        receiver = new WiFiSendBroadcastReceiver(mManager, mChannel, this);
        registerReceiver(receiver, intentFilter);
        registerReceiver(httpReceiver, httpFilter);
        Intent intent = new Intent(this, HttpService.class);
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


    private void getINFO()
    {
        dataToSend = getIntent().getParcelableExtra(DATA_INFO);
    }

    private void addFilterAction()
    {
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION);
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION);
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION);
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION);
    }

    private void addHttpFilterAction()
    {
        httpFilter.addAction(HttpService.ACTION_SERVER_CREATED);
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

    private void startServiceRegistration()
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

    BroadcastReceiver httpReceiver = new BroadcastReceiver()
    {
        @Override
        public void onReceive(Context context, Intent intent)
        {
            if (HttpService.ACTION_SERVER_CREATED.equals(intent.getAction()))
            {
                mLocalPort = intent.getIntExtra("port", 0);
                startServiceRegistration();
            }

        }
    };

}

