package com.clverpanda.nfshare.Fragments;


import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
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

import com.clverpanda.nfshare.ContentShare.ContentPagerAdapter;
import com.clverpanda.nfshare.R;
import com.clverpanda.nfshare.ResourceShare.ResourcePagerAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;


public class ContentFrag extends Fragment {

    @BindView(R.id.content_viewpager)
    protected ViewPager viewPager;
    @BindView(R.id.content_tabs)
    protected TabLayout tabLayout;
    @BindView(R.id.content_layout)
    protected CoordinatorLayout coordinatorLayout;



    public ContentFrag() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_content, container, false);
        ButterKnife.bind(this, view);
        coordinatorLayout.removeView(tabLayout);
        AppBarLayout mainAppBar = (AppBarLayout) getActivity().findViewById(R.id.appbar);
        mainAppBar.addView(tabLayout);
        viewPager.setAdapter(new ContentPagerAdapter(getChildFragmentManager(), getContext()));
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.setTabMode(TabLayout.MODE_FIXED);
        return view;
    }

    @Override
    public void onDestroyView()
    {
        super.onDestroyView();
        AppBarLayout mainAppBar = (AppBarLayout) getActivity().findViewById(R.id.appbar);
        mainAppBar.removeView(tabLayout);
    }

}