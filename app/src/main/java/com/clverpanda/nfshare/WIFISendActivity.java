package com.clverpanda.nfshare;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.IntentFilter;
import android.net.wifi.p2p.WifiP2pManager;
import android.net.wifi.p2p.nsd.WifiP2pDnsSdServiceInfo;
import android.net.wifi.p2p.nsd.WifiP2pDnsSdServiceRequest;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.clverpanda.nfshare.model.NFCTransferData;
import com.clverpanda.nfshare.model.WIFITransferData;
import com.clverpanda.nfshare.receiver.WiFiDirectBroadcastReceiver;

import java.util.HashMap;
import java.util.Map;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class WIFISendActivity extends AppCompatActivity
{
    public static final String DATA_INFO = "com.clverpanda.nfshare.WIFISendActivity.data";
    public static final String TAG = "WIFISendActivity";

    private WIFITransferData dataToSend;
    private WifiP2pManager mManager;
    private WifiP2pManager.Channel mChannel;
    private BroadcastReceiver receiver = null;
    private boolean isWifiP2pEnabled = false;


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
        receiver = new WiFiDirectBroadcastReceiver(mManager, mChannel, this);
        registerReceiver(receiver, intentFilter);
    }

    @Override
    public void onPause()
    {
        super.onPause();
        unregisterReceiver(receiver);
    }


    private void getINFO()
    {
        dataToSend = (WIFITransferData) getIntent().getSerializableExtra(DATA_INFO);
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

    @OnClick(R.id.button3)
    void button3Clicked()
    {
        mManager.discoverPeers(mChannel, new WifiP2pManager.ActionListener() {

            @Override
            public void onSuccess()
            {
                Toast.makeText(WIFISendActivity.this, "成功开始搜索！", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(int reasonCode)
            {

            }
        });
    }


}
