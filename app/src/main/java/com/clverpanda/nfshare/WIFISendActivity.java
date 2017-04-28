package com.clverpanda.nfshare;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.IntentFilter;
import android.net.nsd.NsdManager;
import android.net.nsd.NsdServiceInfo;
import android.net.wifi.p2p.WifiP2pInfo;
import android.net.wifi.p2p.WifiP2pManager;
import android.net.wifi.p2p.nsd.WifiP2pDnsSdServiceInfo;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.clverpanda.nfshare.model.TransferData;
import com.clverpanda.nfshare.receiver.WiFiSendBroadcastReceiver;
import com.hanks.htextview.evaporate.EvaporateTextView;
import com.skyfishjy.library.RippleBackground;

import java.io.IOException;
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
    public static final String KEY_NAME = "name";
    public static final String KEY_AVAILABLE = "available";




    private TransferData dataToSend;
    private WifiP2pManager mManager;
    private WifiP2pManager.Channel mChannel;
    private BroadcastReceiver receiver = null;
    private boolean isWifiP2pEnabled = false;

    private ServerSocket mServerSocket;
    private int mLocalPort;
    private NsdManager.RegistrationListener mRegistrationListener;
    private String mServiceName;
    private NsdManager mNsdManager;

    @BindView(R.id.wifi_animation)
    RippleBackground rippleBackground;
    @BindView(R.id.send_log)
    EvaporateTextView tvLog;


    private final IntentFilter intentFilter = new IntentFilter();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wifisend);
        ButterKnife.bind(this);

        addFilterAction();

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
        mManager.discoverPeers(mChannel, new WifiP2pManager.ActionListener() {
            @Override
            public void onSuccess()
            {
                tvLog.animateText("准备开始发送");
            }

            @Override
            public void onFailure(int reasonCode)
            {
                tvLog.animateText("初始化发送失败！");
            }
        });
        rippleBackground.startRippleAnimation();
    }

    @Override
    public void onPause()
    {
        super.onPause();
        unregisterReceiver(receiver);
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

    public void setIsWifiP2pEnabled(boolean isWifiP2pEnabled) {
        this.isWifiP2pEnabled = isWifiP2pEnabled;
    }

    //连接到对等点
    @Override
    public void onConnectionInfoAvailable(final WifiP2pInfo info)
    {
        String groupOwnerAddress = info.groupOwnerAddress.getHostAddress();
        tvLog.animateText("已经连接到接收设备");
        if (info.groupFormed && info.isGroupOwner)
        {

        }
        else if (info.groupFormed)
        {

        }
    }

    private void startRegistration()
    {
        initializeServerSocket();
        Map<String, String> record = new HashMap<>();
        record.put(KEY_PORT, String.valueOf(mLocalPort));
        record.put(KEY_NAME, "NFShare_FileShare_service");
        record.put(KEY_AVAILABLE, "visible");

        WifiP2pDnsSdServiceInfo serviceInfo =
                WifiP2pDnsSdServiceInfo.newInstance("NFShare_FileShare_service", "_http._tcp", record);

        mManager.addLocalService(mChannel, serviceInfo, new WifiP2pManager.ActionListener()
        {
            @Override
            public void onSuccess() {
                tvLog.animateText("服务创建成功");
                Log.d(TAG, "onSuccess: local service added");
            }

            @Override
            public void onFailure(int arg0) {
                Log.e(TAG, "onFailure: local service not added");
            }
        });
    }

    public void initializeServerSocket()
    {
        try
        {
            mServerSocket = new ServerSocket(0);
            mLocalPort = mServerSocket.getLocalPort();
        }
        catch (IOException e)
        {
            Log.e(TAG, "initializeServerSocket: failed", e);
        }
    }

}

