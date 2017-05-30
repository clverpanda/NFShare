package com.clverpanda.nfshare.fragments;


import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.design.widget.CoordinatorLayout;
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


import com.clverpanda.nfshare.R;
import com.clverpanda.nfshare.fragments.taskslist.TasksPagerAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;


public class TasksFrag extends Fragment {
    public static final String DATA_INFO = "DATA_INFO";


    @BindView(R.id.tasks_viewpager)
    protected ViewPager viewPager;
    @BindView(R.id.tasks_tabs)
    protected TabLayout tabLayout;
    @BindView(R.id.tasks_layout)
    protected CoordinatorLayout coordinatorLayout;
    @BindView(R.id.tasks_toolbar)
    protected Toolbar toolbar;



    public TasksFrag() {}

    public static TasksFrag newInstance(String Data)
    {
        TasksFrag fragment = new TasksFrag();
        Bundle args = new Bundle();
        args.putString(DATA_INFO, Data);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tasks, container, false);
        ButterKnife.bind(this, view);

        AppCompatActivity parentActivity = (AppCompatActivity) getActivity();
        parentActivity.setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) parentActivity.findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                parentActivity, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        toolbar.setTitle(R.string.drawer_item_tasks);
        viewPager.setAdapter(new TasksPagerAdapter(getChildFragmentManager(), getContext()));
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.setTabMode(TabLayout.MODE_FIXED);
        return view;
    }

}
