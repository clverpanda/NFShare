package com.clverpanda.nfshare.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.NetworkInfo;
import android.net.wifi.p2p.WifiP2pManager;
import android.util.Log;

import com.clverpanda.nfshare.WIFISendActivity;
import com.clverpanda.nfshare.fragments.ReceiveFrag;

/**
 * Created by clverpanda on 2017/4/19 0019.
 * It's the file for NFShare.
 */

public class WiFiReceiveBroadcastReceiver extends BroadcastReceiver
{
    private WifiP2pManager manager;
    private WifiP2pManager.Channel channel;
    private ReceiveFrag fragment;


    public WiFiReceiveBroadcastReceiver(WifiP2pManager manager, WifiP2pManager.Channel channel,
                                     ReceiveFrag fragment)
    {
        super();
        this.manager = manager;
        this.channel = channel;
        this.fragment = fragment;
    }


    @Override
    public void onReceive(Context context, Intent intent)
    {
        String action = intent.getAction();

        if (WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION.equals(action))
        {

            int state = intent.getIntExtra(WifiP2pManager.EXTRA_WIFI_STATE, -1);
            if (state == WifiP2pManager.WIFI_P2P_STATE_ENABLED)
            {
                fragment.setIsWifiP2pEnabled(true);
            }
            else
            {
                fragment.setIsWifiP2pEnabled(false);
            }
            Log.d(WIFISendActivity.TAG, "P2P state changed - " + state);
        }
        else if (WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION.equals(action))
        {

        }
        else if (WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION.equals(action))
        {
            if (manager == null) {
                return;
            }

            NetworkInfo networkInfo = intent.getParcelableExtra(WifiP2pManager.EXTRA_NETWORK_INFO);

            if (networkInfo.isConnected())
            {
                manager.requestConnectionInfo(channel, fragment);
            }
        }
        else if (WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION.equals(action))
        {


        }
    }
}
