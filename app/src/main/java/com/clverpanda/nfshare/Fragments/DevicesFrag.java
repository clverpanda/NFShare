package com.clverpanda.nfshare.Fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.clverpanda.nfshare.R;
import com.clverpanda.nfshare.Tasks.LoadDeviceListAsyncTask;
import com.romainpiel.shimmer.Shimmer;
import com.romainpiel.shimmer.ShimmerTextView;

import butterknife.BindView;
import butterknife.ButterKnife;


public class DevicesFrag extends Fragment
{

    @BindView(R.id.devices_toolbar)
    Toolbar toolbar;
    @BindView(R.id.devices_recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.devices_loading)
    ShimmerTextView shimmerTextView;

    private Shimmer shimmer;



    public DevicesFrag() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_devices, container, false);
        ButterKnife.bind(this, view);

        AppCompatActivity parentActivity = (AppCompatActivity) getActivity();
        parentActivity.setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) parentActivity.findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                parentActivity, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        layoutManager.setOrientation(OrientationHelper.VERTICAL);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        toolbar.setTitle(R.string.drawer_item_devices);

        //载入动画
        shimmer = new Shimmer();
        shimmer.start(shimmerTextView);
        LoadDeviceListAsyncTask loadDeviceListAsyncTask = new LoadDeviceListAsyncTask(getContext(),
                recyclerView, shimmer, shimmerTextView);
        loadDeviceListAsyncTask.execute();
        return view;
    }

}
