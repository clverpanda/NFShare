package com.clverpanda.nfshare.Fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.support.design.widget.CoordinatorLayout;

import com.clverpanda.nfshare.ContentShare.AppShareFrag;
import com.clverpanda.nfshare.ContentShare.ContactShareFrag;
import com.clverpanda.nfshare.ContentShare.ContentPagerAdapter;
import com.clverpanda.nfshare.Model.AppInfo;
import com.clverpanda.nfshare.Model.AppInfoTransfer;
import com.clverpanda.nfshare.Model.ContactInfo;
import com.clverpanda.nfshare.Model.DataType;
import com.clverpanda.nfshare.Model.NFCTransferData;
import com.clverpanda.nfshare.NFCSendActivity;
import com.clverpanda.nfshare.R;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import com.alibaba.fastjson.JSON;


public class ContentFrag extends Fragment
{

    @BindView(R.id.content_viewpager)
    protected ViewPager viewPager;
    @BindView(R.id.content_tabs)
    protected TabLayout tabLayout;
    @BindView(R.id.content_layout)
    protected CoordinatorLayout coordinatorLayout;
    @BindView(R.id.content_toolbar)
    protected Toolbar toolbar;
    @BindView(R.id.content_share_menu_nfc)
    protected com.github.clans.fab.FloatingActionButton fabNfc;
    @BindView(R.id.content_share_menu_wifi)
    protected com.github.clans.fab.FloatingActionButton fabWifi;



    public ContentFrag() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_content, container, false);
        ButterKnife.bind(this, view);

        AppCompatActivity parentActivity = (AppCompatActivity) getActivity();
        parentActivity.setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) parentActivity.findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                parentActivity, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        setFabNfcClick();
        setFabWifiClick();
        toolbar.setTitle(R.string.drawer_item_content);
        viewPager.setAdapter(new ContentPagerAdapter(getChildFragmentManager(), getContext()));
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.setTabMode(TabLayout.MODE_FIXED);
        return view;
    }

    private void setFabNfcClick()
    {
        fabNfc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                ContentPagerAdapter pagerAdapter = (ContentPagerAdapter) viewPager.getAdapter();
                if (viewPager.getCurrentItem() == 0)
                {
                    AppShareFrag frag = (AppShareFrag) pagerAdapter.getCurrentFragment();
                    List<AppInfoTransfer> selectedData = frag.getSelectedItems();
                    NFCTransferData nfcData = new NFCTransferData(DataType.APP, JSON.toJSONString(selectedData));
                    Intent startIntent = new Intent(getContext(), NFCSendActivity.class);
                    startIntent.putExtra(NFCSendActivity.DATA_INFO, JSON.toJSONString(nfcData));
                    startActivity(startIntent);
                }
                else if (viewPager.getCurrentItem() == 1)
                {

                }
            }
        });
    }

    private void setFabWifiClick()
    {
        fabWifi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }


    @Override
    public void onDestroyView()
    {
        super.onDestroyView();
    }

}
