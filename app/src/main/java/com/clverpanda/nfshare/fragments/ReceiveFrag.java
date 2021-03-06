package com.clverpanda.nfshare.fragments;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.IntentFilter;
import android.net.wifi.WpsInfo;
import android.net.wifi.p2p.WifiP2pConfig;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pDeviceList;
import android.net.wifi.p2p.WifiP2pInfo;
import android.net.wifi.p2p.WifiP2pManager;
import android.net.wifi.p2p.nsd.WifiP2pDnsSdServiceRequest;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.clverpanda.nfshare.NFShareApplication;
import com.clverpanda.nfshare.R;
import com.clverpanda.nfshare.WIFISendActivity;
import com.clverpanda.nfshare.dao.DaoSession;
import com.clverpanda.nfshare.dao.Task;
import com.clverpanda.nfshare.model.FileInfo;
import com.clverpanda.nfshare.model.TaskStatus;
import com.clverpanda.nfshare.model.TransferData;
import com.clverpanda.nfshare.model.communicate.receive.GetShareRec;
import com.clverpanda.nfshare.model.communicate.send.GetShareSend;
import com.clverpanda.nfshare.receiver.WiFiReceiveBroadcastReceiver;
import com.clverpanda.nfshare.tasks.AsyncResponse;
import com.clverpanda.nfshare.tasks.ConnectServerAsyncTask;
import com.clverpanda.nfshare.tasks.PostGetShareAsyncTask;
import com.clverpanda.nfshare.util.DbHelper;
import com.clverpanda.nfshare.util.DeviceInfoGetter;
import com.clverpanda.nfshare.util.PropertiesGetter;
import com.clverpanda.nfshare.widget.RecyclerItemClickListener;
import com.goodiebag.pinview.Pinview;
import com.skyfishjy.library.RippleBackground;


import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.pedant.SweetAlert.SweetAlertDialog;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


import static android.os.Looper.getMainLooper;


public class ReceiveFrag extends Fragment
        implements WifiP2pManager.ConnectionInfoListener, RecyclerItemClickListener, WifiP2pManager.PeerListListener
{
    private static final String TAG = "ReceiveFrag";


    @BindView(R.id.receive_toolbar)
    protected Toolbar toolbar;
    @BindView(R.id.search_ripple)
    protected RippleBackground rippleBackground;
    @BindView(R.id.receive_device_list)
    protected RecyclerView recyclerView;
    @BindView(R.id.pinview)
    protected Pinview pinview;


    private boolean btnReceiveClicked = false;
    private WifiP2pManager mManager;
    private WifiP2pManager.Channel mChannel;
    private BroadcastReceiver receiver = null;
    private boolean isWifiP2pEnabled = false;
    private boolean isTryWifiDirect = false;
    private final IntentFilter intentFilter = new IntentFilter();
    private List<WifiP2pDevice> peers = new ArrayList<>();

    private int mServerPort = 0;
    private ReceiveRecyclerAdapter mAdapter;
    WifiP2pDnsSdServiceRequest serviceRequest = null;

    SweetAlertDialog pDialog;
    GetShareRec getShareRec = null;

    private static final int REPORT_ERR2SERVER_DONE = 1024;
    private boolean isGetFromWifiDirect = false;
    private Timer timer = new Timer();
    private TimerTask getFromWifiOverTimeTask;

    private class OverTimeTask extends TimerTask
    {
        @Override
        public void run()
        {
            if (!isGetFromWifiDirect)
            {
                mManager.stopPeerDiscovery(mChannel, null);
                reportConnErr2Server();
                Message message = new Message();
                message.what = REPORT_ERR2SERVER_DONE;
                handler.sendMessage(message);
            }
        }
    }


    Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            if (msg.what == REPORT_ERR2SERVER_DONE)
            {
                reportConnErr2ServerDone();
            }
        }
    };

    public ReceiveFrag() { }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_receive, container, false);
        ButterKnife.bind(this, view);

        AppCompatActivity parentActivity = (AppCompatActivity) getActivity();
        parentActivity.setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) parentActivity.findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                parentActivity, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        addFilterAction();
        mManager = (WifiP2pManager) getActivity().getSystemService(Context.WIFI_P2P_SERVICE);
        mChannel = mManager.initialize(getContext(), getMainLooper(), null);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        mAdapter = new ReceiveRecyclerAdapter(getContext(), peers, this);
        recyclerView.setAdapter(mAdapter);

        toolbar.setTitle(R.string.drawer_item_receive);

        pinview.setPinViewEventListener(new Pinview.PinViewEventListener()
        {
            @Override
            public void onDataEntered(Pinview pinview, boolean fromUser)
            {
                showPendingDialog();
                tryGetFromServer(Integer.parseInt(pinview.getValue()));
            }
        });

        return view;
    }

    void showPendingDialog()
    {
        pDialog = new SweetAlertDialog(getContext(), SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(ContextCompat.getColor(getContext(), R.color.colorAccent));
        pDialog.setTitleText("连接中");
        pDialog.setCancelable(false);
        pDialog.show();
    }

    private void tryGetFromServer(int pin_code)
    {
        isTryWifiDirect = false;
        PostGetShareAsyncTask getShareFromServerTask = new PostGetShareAsyncTask(getContext());
        getShareFromServerTask.setAsyncResponse(new AsyncResponse<GetShareRec>()
        {
            @Override
            public void onDataReceivedSuccess(final GetShareRec listData)
            {
                getShareRec = listData;
                mServerPort = listData.getPort();
                if (DeviceInfoGetter.getInstance(getContext()).isUsingWifi() && listData.getIp() != null)
                {
                    Log.d(TAG, "onDataReceivedSuccess: 云服务器连接成功");
                    String LANInfoUrl = "http://" + listData.getIp() + ":" + listData.getPort() + "/getInfo";
                    final String LANFileUrl = "http://" + listData.getIp() + ":" + listData.getPort() + "/getFile";
                    ConnectServerAsyncTask tryConnectInLAN = new ConnectServerAsyncTask();
                    tryConnectInLAN.setOnAsyncResponse(new AsyncResponse<TransferData>() {
                        @Override
                        public void onDataReceivedSuccess(TransferData resultData)
                        {
                            Log.d(TAG, "onDataReceivedSuccess: 成功从对方手机获取数据");
                            doneReceiveTask(resultData, LANFileUrl);
                        }
                        @Override
                        public void onDataReceivedFailed()
                        {
                            Log.e(TAG, "onDataReceivedFailed: 从对方手机获取数据失败");
                            tryGetFromWifiDirect();
                        }
                    });
                    tryConnectInLAN.execute(LANInfoUrl);
                }
                else
                    tryGetFromWifiDirect();
            }

            @Override
            public void onDataReceivedFailed()
            {
                pDialog.cancel();
                Log.e(TAG, "onDataReceivedFailed: 云服务器连接失败");
                Toast.makeText(getContext(), "与云服务器连接失败", Toast.LENGTH_SHORT).show();
            }
        });
        getShareFromServerTask.execute(new GetShareSend(pin_code, getContext()));
    }

    private void tryGetFromWifiDirect()
    {
        isGetFromWifiDirect = false;
        mManager.discoverPeers(mChannel, new WifiP2pManager.ActionListener()
        {
            @Override
            public void onSuccess()
            {
                Log.d(TAG, "onSuccess: 开始wifidirect扫描");
            }
            @Override
            public void onFailure(int reasonCode)
            {
                confirmReportConnErr2Server();
            }
        });
        getFromWifiOverTimeTask = new OverTimeTask();
        timer.schedule(getFromWifiOverTimeTask, 10000);
    }


    @Override
    public void onPeersAvailable(WifiP2pDeviceList peerList)
    {
        peers.clear();
        peers.addAll(peerList.getDeviceList());

        if (peers.size() == 0) {
            Log.d(TAG, "No devices found");
            return;
        }
        if (!isTryWifiDirect && getShareRec != null && peers.size() > 0)
        {
            for (WifiP2pDevice item : peers)
            {
                Log.e(TAG, "onPeersAvailable: " + item.deviceAddress);
                Log.e(TAG, "onPeersAvailable: " + getShareRec.getOrigin_phone());
                String tempStr = item.deviceAddress.substring(3);
                String tempStr1 = getShareRec.getOrigin_phone().substring(3);
                if (tempStr.equalsIgnoreCase(tempStr1))
                {
                    isTryWifiDirect = true;
                    isGetFromWifiDirect = true;
                    pDialog.cancel();
                    connect(item);
                    return;
                }
            }
            reportConnErr2Server();
        }
    }

    private void confirmReportConnErr2Server()
    {
        final SweetAlertDialog confirmConnErrDialog = new SweetAlertDialog(this.getContext(), SweetAlertDialog.WARNING_TYPE)
                .setTitleText("无法连接至对方设备")
                .setContentText("是否告知对方启用云传输")
                .setCancelText("否")
                .setConfirmText("是")
                .showCancelButton(true);
        confirmConnErrDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog)
                    {
                        reportConnErr2Server();
                        confirmConnErrDialog.cancel();
                    }
                });
        confirmConnErrDialog.show();
    }

    private void reportConnErr2Server()
    {
        if (getShareRec == null) return;
        isGetFromWifiDirect = true;
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
                    URL url = new URL(PropertiesGetter.getConnErrCallbackUrl(getContext()) + getShareRec.getId());
                    Request request = new Request.Builder().url(url).build();
                    Response response = client.newCall(request).execute();
                    if (response.isSuccessful())
                    {
                        String result = response.body().string();
                        Log.d(TAG, "服务器：回调" + result);
                    }
                    else
                        Log.d(TAG, "服务器：回调失败");
                    getShareRec = null;
                }
                catch (IOException e)
                {
                    Log.e(TAG, "服务器：回调失败", e);
                    getShareRec = null;
                }
            }
        }).start();
        mManager.stopPeerDiscovery(mChannel, null);
        Message message = new Message();
        message.what = REPORT_ERR2SERVER_DONE;
        handler.sendMessage(message);
    }

    private void reportConnErr2ServerDone()
    {
        Toast.makeText(getContext(), "无法找到对方，已向服务器汇报", Toast.LENGTH_SHORT).show();
        pDialog.cancel();
    }


    @OnClick(R.id.btn_start_search)
    void searchClicked()
    {
        getShareRec = null;
        if (!btnReceiveClicked)
        {
            btnReceiveClicked = true;
            discoverService();
            rippleBackground.startRippleAnimation();
        }
        else
        {
            btnReceiveClicked = false;
            mManager.clearServiceRequests(mChannel, null);
            mManager.cancelConnect(mChannel, null);
            rippleBackground.stopRippleAnimation();
        }
    }

    protected void doneReceiveTask(TransferData resultData, String fileUrl)
    {
        FileInfo fileInfo = JSON.parseObject(resultData.getPayload(), FileInfo.class);
        fileInfo.setDownloadUrl(fileUrl);
        DaoSession daoSession = NFShareApplication.getInstance().getDaoSession();
        long deviceId = DbHelper.getInstance().insertOrReplaceDevice(resultData.getDevice());
        Task task2Add = new Task(fileInfo.getFileName(), JSON.toJSONString(fileInfo), resultData.getDataType(), TaskStatus.PAUSED,
                new Date(), deviceId);
        daoSession.getTaskDao().insert(task2Add);
        Toast.makeText(getActivity(), "任务已添加", Toast.LENGTH_SHORT).show();
        if (pDialog != null) pDialog.cancel();
    }

    //连接到对等点
    @Override
    public void onConnectionInfoAvailable(final WifiP2pInfo info)
    {
        String ownerIP = info.groupOwnerAddress.getHostAddress();
        String rawUrl = "http://" + ownerIP + ":" + mServerPort;
        final String infoUrl = rawUrl + "/getInfo";
        final String fileUrl = rawUrl + "/getFile";
        if (info.groupFormed && !info.isGroupOwner)
        {
            ConnectServerAsyncTask connectWifiOwnerTask = new ConnectServerAsyncTask();
            connectWifiOwnerTask.setOnAsyncResponse(new AsyncResponse<TransferData>()
            {
                @Override
                public void onDataReceivedSuccess(TransferData resultData)
                {
                    doneReceiveTask(resultData, fileUrl);
                }

                @Override
                public void onDataReceivedFailed()
                {
                    mManager.removeGroup(mChannel, null);
                    Toast.makeText(getActivity(), "无法从对方设备获取数据", Toast.LENGTH_SHORT).show();
                    pDialog.cancel();
                }
            });
            connectWifiOwnerTask.execute(infoUrl);
        }
    }

    @Override
    public void onItemClick(View v, int position)
    {
        WifiP2pDevice device = peers.get(position);
        mServerPort = Integer.parseInt(ports.get(device.deviceAddress));
        connect(device);
    }

    public void connect(final WifiP2pDevice device2connect)
    {
        Log.e(TAG, "connect: start");
        WifiP2pConfig config = new WifiP2pConfig();
        config.deviceAddress = device2connect.deviceAddress;
        config.wps.setup = WpsInfo.PBC;
        mManager.cancelConnect(mChannel, null);
        mManager.removeGroup(mChannel, null);

        mManager.connect(mChannel, config, new WifiP2pManager.ActionListener()
        {
            @Override
            public void onSuccess()
            {
                Log.e(TAG, "onSuccess: connect");
                Toast.makeText(getActivity(), "已经连接到对方设备", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(int reason)
            {
                Log.e(TAG, "onFailure: connect");
                Toast.makeText(getActivity(), "连接失败，请重试", Toast.LENGTH_SHORT).show();
                confirmReportConnErr2Server();
            }
        });
    }


    public void setIsWifiP2pEnabled(boolean isWifiP2pEnabled)
    {
        this.isWifiP2pEnabled = isWifiP2pEnabled;
    }

    @Override
    public void onResume()
    {
        super.onResume();
        receiver = new WiFiReceiveBroadcastReceiver(mManager, mChannel, this);
        getActivity().registerReceiver(receiver, intentFilter);
    }

    @Override
    public void onPause()
    {
        super.onPause();
        getActivity().unregisterReceiver(receiver);
    }

    final HashMap<String, String> buddies = new HashMap<>();
    final HashMap<String, String> ports = new HashMap<>();
    private void discoverService()
    {
        WifiP2pManager.DnsSdTxtRecordListener txtListener = new WifiP2pManager.DnsSdTxtRecordListener()
        {
            @Override
            public void onDnsSdTxtRecordAvailable(String fullDomain, Map<String, String> record, WifiP2pDevice device)
            {
                Log.d(TAG, "DnsSdTxtRecord available -" + record.toString());
                buddies.put(device.deviceAddress, record.get(WIFISendActivity.KEY_NAME));
                ports.put(device.deviceAddress, record.get(WIFISendActivity.KEY_PORT));
            }
        };

        WifiP2pManager.DnsSdServiceResponseListener servListener = new WifiP2pManager.DnsSdServiceResponseListener() {
            @Override
            public void onDnsSdServiceAvailable(String instanceName, String registrationType,
                                                WifiP2pDevice resourceType)
            {
                resourceType.secondaryDeviceType = buddies
                        .containsKey(resourceType.deviceAddress) ? buddies
                        .get(resourceType.deviceAddress) : resourceType.deviceName;
                peers.add(resourceType);
                mAdapter.notifyDataSetChanged();
                Log.d(TAG, "onBonjourServiceAvailable " + instanceName);
            }
        };

        mManager.setDnsSdResponseListeners(mChannel, servListener, txtListener);

        serviceRequest = WifiP2pDnsSdServiceRequest.newInstance();
        mManager.addServiceRequest(mChannel, serviceRequest,
                new WifiP2pManager.ActionListener()
                {
                    @Override
                    public void onSuccess() {
                        Log.d(TAG, "onSuccess: added service request");
                    }

                    @Override
                    public void onFailure(int code) {
                        Log.e(TAG, "onFailure: add service request");
                    }
                });

        mManager.discoverServices(mChannel, new WifiP2pManager.ActionListener()
        {
            @Override
            public void onSuccess()
            {
                Log.d(TAG, "onSuccess: discover services");
            }

            @Override
            public void onFailure(int code) {
                Log.e(TAG, "onFailure: discover services");
                rippleBackground.stopRippleAnimation();
                btnReceiveClicked = false;
            }
        });
    }

    private void addFilterAction()
    {
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION);
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION);
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION);
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION);
    }
}
