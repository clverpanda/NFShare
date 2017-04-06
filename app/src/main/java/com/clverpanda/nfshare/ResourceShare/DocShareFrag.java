package com.clverpanda.nfshare.ResourceShare;


import android.Manifest;
import android.app.DownloadManager;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.StringDef;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.clverpanda.nfshare.R;
import com.liulishuo.filedownloader.BaseDownloadTask;
import com.liulishuo.filedownloader.FileDownloadListener;
import com.liulishuo.filedownloader.FileDownloadQueueSet;
import com.liulishuo.filedownloader.FileDownloader;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.content.Context.DOWNLOAD_SERVICE;


public class DocShareFrag extends Fragment {
    private static final int REQUEST_CODE_ASK_WRITE = 111;
    private static final String DOWNLOAD_FOLDER_NAME = "download/";
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;


    @BindView(R.id.test_button)
    Button button;
    @BindView(R.id.test_button2)
    Button button2;
    @BindView(R.id.test_textView)
    TextView textView;
    @BindView(R.id.test_textView2)
    TextView textView2;


    public DocShareFrag() {
        // Required empty public constructor
    }


    public static DocShareFrag newInstance(String param1, String param2) {
        DocShareFrag fragment = new DocShareFrag();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.content_doc_share, container, false);
        ButterKnife.bind(this, view);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
                {
                    if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE)
                            != PackageManager.PERMISSION_GRANTED)
                        requestPermissions(new String[] {Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_CODE_ASK_WRITE);
                    else
                        startDownload();
                }
                else
                    startDownload();
            }
        });

        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {

            }
        });
        return view;
    }

    private void startDownload()
    {
        String[] strings = {"http://www.wandoujia.com/apps/com.dianping.v1/download", "http://www.wandoujia.com/apps/com.dianping.v1/download"};
        for (int i = 0; i < strings.length; i++) {
            FileDownloader.getImpl().create(strings[i])
                    .setTag(i + 1)
                    .setPath(Environment.DIRECTORY_DOWNLOADS + "/")
                    .setCallbackProgressTimes(10)
                    .setListener(queueTarget)
                    .asInQueueTask()
                    .enqueue();
        }
        FileDownloader.getImpl().start(queueTarget, true);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)
    {
        switch (requestCode) {
            case REQUEST_CODE_ASK_WRITE:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED)
                    startDownload();
                else
                {}
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    final FileDownloadListener queueTarget = new FileDownloadListener() {
        @Override
        protected void pending(BaseDownloadTask task, int soFarBytes, int totalBytes) {
        }

        @Override
        protected void connected(BaseDownloadTask task, String etag, boolean isContinue, int soFarBytes, int totalBytes) {
        }

        @Override
        protected void progress(BaseDownloadTask task, int soFarBytes, int totalBytes)
        {
            if ((int)task.getTag() == 1)
                textView.setText("任务1:" + soFarBytes + "/" + totalBytes);
            else
                textView.setText("任务2:" + soFarBytes + "/" + totalBytes);
        }

        @Override
        protected void blockComplete(BaseDownloadTask task) {
        }

        @Override
        protected void retry(final BaseDownloadTask task, final Throwable ex, final int retryingTimes, final int soFarBytes) {
        }

        @Override
        protected void completed(BaseDownloadTask task) {
        }

        @Override
        protected void paused(BaseDownloadTask task, int soFarBytes, int totalBytes) {
        }

        @Override
        protected void error(BaseDownloadTask task, Throwable e) {
        }

        @Override
        protected void warn(BaseDownloadTask task) {
        }
    };


}
