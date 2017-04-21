package com.clverpanda.nfshare;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.IntentFilter;
import android.net.wifi.p2p.WifiP2pManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.clverpanda.nfshare.model.TransferData;
import com.clverpanda.nfshare.receiver.WiFiSendBroadcastReceiver;
import com.skyfishjy.library.RippleBackground;

import butterknife.BindView;
import butterknife.ButterKnife;

public class WIFISendActivity extends AppCompatActivity
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
                Toast.makeText(WIFISendActivity.this, "准备开始发送", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(int reasonCode)
            {
                Toast.makeText(WIFISendActivity.this, "初始化发送失败！", Toast.LENGTH_SHORT).show();
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
        dataToSend = (TransferData) getIntent().getSerializableExtra(DATA_INFO);
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

}
