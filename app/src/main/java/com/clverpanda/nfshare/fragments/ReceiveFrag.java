package com.clverpanda.nfshare.fragments;


import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.clverpanda.nfshare.R;
import com.skyfishjy.library.RippleBackground;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class ReceiveFrag extends Fragment
{


    @BindView(R.id.receive_toolbar)
    protected Toolbar toolbar;
    @BindView(R.id.search_ripple)
    protected RippleBackground rippleBackground;


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

        toolbar.setTitle(R.string.drawer_item_receive);

        return view;
    }



    @OnClick(R.id.btn_start_search)
    void searchClicked()
    {
        rippleBackground.startRippleAnimation();
    }

}
