package com.clverpanda.nfshare.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.support.design.widget.CoordinatorLayout;

import com.clverpanda.nfshare.fragments.contentshare.AppShareFrag;
import com.clverpanda.nfshare.fragments.contentshare.ContactShareFrag;
import com.clverpanda.nfshare.fragments.contentshare.ContentPagerAdapter;
import com.clverpanda.nfshare.model.AppInfoTransfer;
import com.clverpanda.nfshare.model.ContactInfo;
import com.clverpanda.nfshare.model.DataType;
import com.clverpanda.nfshare.model.TransferData;
import com.clverpanda.nfshare.NFCSendActivity;
import com.clverpanda.nfshare.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import com.alibaba.fastjson.JSON;
import com.clverpanda.nfshare.util.DeviceInfoGetter;


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
                DeviceInfoGetter deviceInfoGetter = DeviceInfoGetter.getInstance(getContext());
                if (viewPager.getCurrentItem() == 0)
                {
                    AppShareFrag frag = (AppShareFrag) pagerAdapter.getCurrentFragment();
                    List<AppInfoTransfer> selectedData = frag.getSelectedItems();
                    TransferData nfcData = new TransferData(DataType.APP, deviceInfoGetter.getDeviceInfo(), JSON.toJSONString(selectedData));
                    Intent startIntent = new Intent(getContext(), NFCSendActivity.class);
                    startIntent.putExtra(NFCSendActivity.DATA_INFO, nfcData);
                    startActivity(startIntent);
                }
                else if (viewPager.getCurrentItem() == 1)
                {
                    ContactShareFrag frag = (ContactShareFrag) pagerAdapter.getCurrentFragment();
                    ContactInfo selectedData = frag.getSelectedItems().get(0);
                    TransferData nfcData = new TransferData(DataType.CONTACT, deviceInfoGetter.getDeviceInfo(), JSON.toJSONString(selectedData));
                    Intent startIntent = new Intent(getContext(), NFCSendActivity.class);
                    startIntent.putExtra(NFCSendActivity.DATA_INFO, nfcData);
                    startActivity(startIntent);
                }
            }
        });
    }


    @Override
    public void onDestroyView()
    {
        super.onDestroyView();
    }

}
