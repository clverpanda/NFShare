package com.clverpanda.nfshare.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.alibaba.fastjson.JSON;
import com.clverpanda.nfshare.NFShareApplication;
import com.clverpanda.nfshare.R;
import com.clverpanda.nfshare.WIFISendActivity;
import com.clverpanda.nfshare.dao.DaoSession;
import com.clverpanda.nfshare.dao.Device;
import com.clverpanda.nfshare.dao.Task;
import com.clverpanda.nfshare.model.DataType;
import com.clverpanda.nfshare.model.TaskStatus;
import com.clverpanda.nfshare.model.communicate.send.GetShareSend;
import com.clverpanda.nfshare.model.communicate.send.StartShareSend;
import com.clverpanda.nfshare.util.PropertiesGetter;
import com.hanks.htextview.evaporate.EvaporateTextView;

import java.io.IOException;
import java.net.URL;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.pedant.SweetAlert.SweetAlertDialog;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class TestFrag extends Fragment {

    @BindView(R.id.button)
    Button button;
    @BindView(R.id.test_textView)
    EvaporateTextView tvTest;

    public static final MediaType JSON_TYPE = MediaType.parse("application/json; charset=utf-8");



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
        tvTest.animateText("asfsdfasdf");
    }

    @OnClick(R.id.btn_server_test)
    void btnServerClicked()
    {
        new Thread(new Runnable() {
            @Override
            public void run() {
                OkHttpClient client = new OkHttpClient.Builder()
                        .connectTimeout(300, TimeUnit.SECONDS)
                        .build();
                try {
                    URL url = new URL(PropertiesGetter.getStartShareUrl(getContext()));
                    StartShareSend ssInfo = new StartShareSend(DataType.FILE, "test",
                            80, getContext());
                    RequestBody requestBody = RequestBody.create(JSON_TYPE, JSON.toJSONString(ssInfo));
                    Request request = new Request.Builder()
                            .url(url)
                            .post(requestBody)
                            .build();
                    Response response = client.newCall(request).execute();
                    if (response.isSuccessful())
                    {
                        Log.i("GET FROM SERVER", response.body().string());
                    }
                }
                catch (IOException e)
                {
                    Log.e("TEST", "error: connect to server");
                }
            }
        }).start();
    }

    @OnClick(R.id.btn_server_get)
    void btnServerGetClicked()
    {
        new Thread(new Runnable() {
            @Override
            public void run() {
                OkHttpClient client = new OkHttpClient.Builder()
                        .connectTimeout(300, TimeUnit.SECONDS)
                        .build();
                try {
                    URL url = new URL(PropertiesGetter.getGetShareUrl(getContext()));
                    GetShareSend gsInfo = new GetShareSend(1196, getContext());
                    RequestBody requestBody = RequestBody.create(JSON_TYPE, JSON.toJSONString(gsInfo));
                    Request request = new Request.Builder()
                            .url(url)
                            .post(requestBody)
                            .build();
                    Response response = client.newCall(request).execute();
                    if (response.isSuccessful())
                    {
                        Log.i("GET FROM SERVER", response.body().string());
                    }
                }
                catch (IOException e)
                {
                    Log.e("TEST", "error: connect to server");
                }
            }
        }).start();
    }

    @OnClick(R.id.button4)
    void btnDialogTestClicked()
    {
        new SweetAlertDialog(this.getContext(), SweetAlertDialog.WARNING_TYPE)
                .setTitleText("无法连接至对方设备")
                .setContentText("是否告知对方启用云传输")
                .setCancelText("否")
                .setConfirmText("是")
                .showCancelButton(true)
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog)
                    {

                    }
                })
                .show();
    }

}
