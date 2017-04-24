package com.clverpanda.nfshare.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.clverpanda.nfshare.NFShareApplication;
import com.clverpanda.nfshare.R;
import com.clverpanda.nfshare.WIFISendActivity;
import com.clverpanda.nfshare.dao.DaoSession;
import com.clverpanda.nfshare.dao.Device;
import com.clverpanda.nfshare.dao.Task;
import com.clverpanda.nfshare.model.DataType;
import com.clverpanda.nfshare.model.TaskStatus;

import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class TestFrag extends Fragment {

    @BindView(R.id.button)
    Button button;
    @BindView(R.id.test_textView)
    TextView tvTest;



    public TestFrag() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_test, container, false);
        ButterKnife.bind(this, view);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                DaoSession daoSession = NFShareApplication.getInstance().getDaoSession();
                Device device = new Device("测试机", "11:11:11:11:11", "");
                long deviceId = daoSession.getDeviceDao().insert(device);
                Task taskInfo = new Task("下载测试", "{\"appName\":\"百度地图\",\"appVersion\":\"9.7.5\",\"pkgName\":\"com.baidu.BaiduMap\"}",
                        DataType.APP, TaskStatus.PAUSED, new Date(), deviceId);
                daoSession.getTaskDao().insert(taskInfo);
            }
        });

        return view;
    }

    @OnClick(R.id.button2)
    void button2Clicked()
    {
        Intent intent = new Intent(getContext(), WIFISendActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.button3)
    void button3Clicked()
    {
        tvTest.append("测试测试测试！\r\n");
    }

}
