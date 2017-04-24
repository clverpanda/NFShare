package com.clverpanda.nfshare;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.IntentFilter;
import android.net.wifi.p2p.WifiP2pInfo;
import android.net.wifi.p2p.WifiP2pManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.clverpanda.nfshare.model.TransferData;
import com.clverpanda.nfshare.receiver.WiFiSendBroadcastReceiver;
import com.skyfishjy.library.RippleBackground;

import butterknife.BindView;
import butterknife.ButterKnife;

public class WIFISendActivity extends AppCompatActivity implements WifiP2pManager.ConnectionInfoListener
{
    public static final String DATA_INFO = "com.clverpanda.nfshare.WIFISendActivity.data";
    public static final String TAG = "WIFISendActivity";

    private TransferData dataToSend;
    private WifiP2pManager mManager;
    private WifiP2pManager.Channel mChannel;
    private BroadcastReceiver receiver = null;
    private boolean isWifiP2pEnabled = false;

    @BindView(R.id.wifi_animation)
    RippleBackground rippleBackground;
    @BindView(R.id.send_log)
    TextView tvSendLog;


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
                tvSendLog.append("准备开始发送！\r\n");
            }

            @Override
            public void onFailure(int reasonCode)
            {
                tvSendLog.append("初始化发送失败！ \r\n");
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
        tvSendLog.append("已经连接到对方设备！\r\n");
        if (info.groupFormed && info.isGroupOwner)
        {

        }
        else if (info.groupFormed)
        {

        }
    }

}
