package com.clverpanda.nfshare.fragments;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.IntentFilter;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pDeviceList;
import android.net.wifi.p2p.WifiP2pManager;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.clverpanda.nfshare.R;
import com.clverpanda.nfshare.receiver.WiFiReceiveBroadcastReceiver;
import com.clverpanda.nfshare.receiver.WiFiSendBroadcastReceiver;
import com.skyfishjy.library.RippleBackground;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static android.os.Looper.getMainLooper;


public class ReceiveFrag extends Fragment implements WifiP2pManager.PeerListListener
{
    public static final String TAG = "ReceiveFrag";


    @BindView(R.id.receive_toolbar)
    protected Toolbar toolbar;
    @BindView(R.id.search_ripple)
    protected RippleBackground rippleBackground;
    @BindView(R.id.receive_device_list)
    protected RecyclerView recyclerView;


    private WifiP2pManager mManager;
    private WifiP2pManager.Channel mChannel;
    private BroadcastReceiver receiver = null;
    private boolean isWifiP2pEnabled = false;
    private final IntentFilter intentFilter = new IntentFilter();
    private List<WifiP2pDevice> peers = new ArrayList<>();

    private ReceiveRecyclerAdapter mAdapter;




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
        mAdapter = new ReceiveRecyclerAdapter(getContext(), peers);
        recyclerView.setAdapter(mAdapter);

        toolbar.setTitle(R.string.drawer_item_receive);

        return view;
    }



    @OnClick(R.id.btn_start_search)
    void searchClicked()
    {
        rippleBackground.startRippleAnimation();
        mManager.discoverPeers(mChannel, new WifiP2pManager.ActionListener()
        {
            @Override
            public void onSuccess()
            {
                Toast.makeText(getContext(), "开始搜索", Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onFailure(int reasonCode)
            {
                Toast.makeText(getContext(), "开始失败", Toast.LENGTH_SHORT).show();
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


    @Override
    public void onPeersAvailable(WifiP2pDeviceList peerList)
    {
        peers.clear();
        peers.addAll(peerList.getDeviceList());
        mAdapter.notifyDataSetChanged();
        if (peers.size() == 0)
        {
            Log.d(TAG, "No devices found");
        }
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

}
