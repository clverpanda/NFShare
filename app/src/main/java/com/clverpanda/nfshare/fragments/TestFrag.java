package com.clverpanda.nfshare.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.clverpanda.nfshare.R;
import com.clverpanda.nfshare.WIFISendActivity;
import com.clverpanda.nfshare.util.database.TasksDbHelper;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class TestFrag extends Fragment {

    @BindView(R.id.button)
    Button button;



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
                TaskInfo taskInfo = new TaskInfo("下载测试", "{\"appName\":\"百度地图\",\"appVersion\":\"9.7.5\",\"pkgName\":\"com.baidu.BaiduMap\"}", 2, "测试机", 0);
                TasksDbHelper tasksDbHelper = new TasksDbHelper(getContext());
                tasksDbHelper.addTaskInfo(taskInfo);
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

}
