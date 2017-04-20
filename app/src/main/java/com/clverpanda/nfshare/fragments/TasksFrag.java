package com.clverpanda.nfshare.fragments;


import android.content.Intent;
import android.net.Uri;
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

import com.alibaba.fastjson.JSON;
import com.clverpanda.nfshare.model.AppInfo;
import com.clverpanda.nfshare.model.ContactInfo;
import com.clverpanda.nfshare.model.DataType;
import com.clverpanda.nfshare.model.NFCTransferData;
import com.clverpanda.nfshare.model.TaskInfo;
import com.clverpanda.nfshare.R;
import com.clverpanda.nfshare.taskslist.TasksPagerAdapter;
import com.clverpanda.nfshare.util.database.DeviceDbHelper;
import com.clverpanda.nfshare.util.database.TasksDbHelper;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class TasksFrag extends Fragment {
    public static final String DATA_INFO = "DATA_INFO";
    public static final String DATA_TYPE = "DATA_TYPE";


    @BindView(R.id.tasks_viewpager)
    protected ViewPager viewPager;
    @BindView(R.id.tasks_tabs)
    protected TabLayout tabLayout;
    @BindView(R.id.tasks_layout)
    protected CoordinatorLayout coordinatorLayout;
    @BindView(R.id.tasks_toolbar)
    protected Toolbar toolbar;



    public TasksFrag() {}

    public static TasksFrag newInstance(String Data, int Type)
    {
        TasksFrag fragment = new TasksFrag();
        Bundle args = new Bundle();
        args.putString(DATA_INFO, Data);
        args.putInt(DATA_TYPE, Type);
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

    @Override
    public void onResume()
    {
        super.onResume();
        processData();
    }


    protected void processData()
    {
        Bundle args = this.getArguments();
        if (args == null) return;
        String data = args.getString(DATA_INFO);
        int type = args.getInt(DATA_TYPE);
        if (data != null)
        {
            if (type == 1)
            {
                NFCTransferData NFCData = JSON.parseObject(data, NFCTransferData.class);
                DeviceDbHelper deviceDb = new DeviceDbHelper(getContext());
                TasksDbHelper tasksDb = new TasksDbHelper(getContext());
                deviceDb.addDeviceInfoNotRe(NFCData.getDeviceInfo());
                switch (NFCData.getDataType())
                {
                    case APP:
                        List<AppInfo> appInfo = JSON.parseArray(NFCData.getPayload(), AppInfo.class);
                        for (AppInfo appInfoItem : appInfo)
                        {
                            TaskInfo taskInfo = new TaskInfo(appInfoItem.getAppName(), JSON.toJSONString(appInfoItem),
                                    DataType.APP.getIndex(), NFCData.getDeviceInfo().getName(), 0);
                            tasksDb.addTaskInfo(taskInfo);
                        }
                        break;
                    case CONTACT:
                        ContactInfo contactInfo = JSON.parseObject(NFCData.getPayload(), ContactInfo.class);
                        TaskInfo taskInfo = new TaskInfo(contactInfo.getName(), JSON.toJSONString(contactInfo),
                                DataType.CONTACT.getIndex(), NFCData.getDeviceInfo().getName(), 1);
                        tasksDb.addTaskInfo(taskInfo);
                        Intent addIntent = new Intent(Intent.ACTION_INSERT, Uri.withAppendedPath(Uri.parse("content://com.android.contacts"), "contacts"));
                        addIntent.setType("vnd.android.cursor.dir/person");
                        addIntent.setType("vnd.android.cursor.dir/contact");
                        addIntent.setType("vnd.android.cursor.dir/raw_contact");
                        addIntent.putExtra(ContactsContract.Intents.Insert.PHONE, contactInfo.getNumber());
                        addIntent.putExtra(ContactsContract.Intents.Insert.NAME, contactInfo.getName());
                        startActivity(addIntent);
                        break;
                }
            }

        }
    }

}
