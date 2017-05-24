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
import android.support.v4.app.Fragment;
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
import com.clverpanda.nfshare.dao.Device;
import com.clverpanda.nfshare.dao.Task;
import com.clverpanda.nfshare.model.DataType;
import com.clverpanda.nfshare.model.FileInfo;
import com.clverpanda.nfshare.model.TaskStatus;
import com.clverpanda.nfshare.model.TransferData;
import com.clverpanda.nfshare.receiver.WiFiReceiveBroadcastReceiver;
import com.clverpanda.nfshare.widget.RecyclerItemClickListener;
import com.skyfishjy.library.RippleBackground;

import java.io.IOException;
import java.io.Reader;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static android.os.Looper.getMainLooper;


public class ReceiveFrag extends Fragment
        implements WifiP2pManager.ConnectionInfoListener, RecyclerItemClickListener
{
    public static final String TAG = "ReceiveFrag";


    @BindView(R.id.receive_toolbar)
    protected Toolbar toolbar;
    @BindView(R.id.search_ripple)
    protected RippleBackground rippleBackground;
    @BindView(R.id.receive_device_list)
    protected RecyclerView recyclerView;


    private boolean btnReceiveClicked = false;
    private WifiP2pManager mManager;
    private WifiP2pManager.Channel mChannel;
    private BroadcastReceiver receiver = null;
    private boolean isWifiP2pEnabled = false;
    private final IntentFilter intentFilter = new IntentFilter();
    private List<WifiP2pDevice> peers = new ArrayList<>();

    private int mServerPort = 0;
    private ReceiveRecyclerAdapter mAdapter;
    WifiP2pDnsSdServiceRequest serviceRequest = null;



    public ReceiveFrag() { }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
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

        return view;
    }



    @OnClick(R.id.btn_start_search)
    void searchClicked()
    {
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

    private void addFilterAction()
    {
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION);
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION);
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION);
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION);
    }

    //连接到对等点
    @Override
    public void onConnectionInfoAvailable(final WifiP2pInfo info)
    {
        String ownerIP = info.groupOwnerAddress.getHostAddress();
        String rawUrl = "http://" + ownerIP + ":" + mServerPort;
        final String infoUrl = rawUrl + "/getInfo";
        final String fileUrl = rawUrl + "/getFile";
        if (info.groupFormed)
        {
            new Thread(new Runnable() {
                @Override
                public void run()
                {
                    OkHttpClient client = new OkHttpClient.Builder()
                            .connectTimeout(3, TimeUnit.SECONDS)
                            .build();
                    try {
                    URL url = new URL(infoUrl);
                    Request request = new Request.Builder().url(url).build();
                        Response response = client.newCall(request).execute();
                        if (response.isSuccessful())
                        {
                            Log.d(TAG, "run: 服务器连接成功");
                            String result = response.body().string();
                            Log.d(TAG, "run: " + result);
                            TransferData resultData = JSON.parseObject(result, TransferData.class);
                            FileInfo fileInfo = JSON.parseObject(resultData.getPayload(), FileInfo.class);
                            fileInfo.setDownloadUrl(fileUrl);
                            DaoSession daoSession = NFShareApplication.getInstance().getDaoSession();
                            long deviceId = daoSession.getDeviceDao().insertOrReplace(resultData.getDevice());
                            Task task2Add = new Task(fileInfo.getFileName(), JSON.toJSONString(fileInfo), resultData.getDataType(), TaskStatus.PAUSED,
                                    new Date(), deviceId);
                            daoSession.getTaskDao().insert(task2Add);
                        }
                    }
                    catch (IOException e)
                    {
                        Log.e(TAG, "run: ", e);
                    }
                }
            }).start();
            Toast.makeText(getActivity(), "成功获取数据", Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public void onItemClick(View v, int position)
    {
        WifiP2pDevice device = peers.get(position);
        connect(device);
    }

    public void connect(final WifiP2pDevice device2connect)
    {
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
                mServerPort = Integer.parseInt(ports.get(device2connect.deviceAddress));
                Toast.makeText(getActivity(), "已经连接到对方设备", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(int reason) {
                Toast.makeText(getActivity(), "连接失败，请重试", Toast.LENGTH_SHORT).show();
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

}
