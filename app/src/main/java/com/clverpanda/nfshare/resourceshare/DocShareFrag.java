package com.clverpanda.nfshare.resourceshare;


import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.clverpanda.nfshare.model.TaskInfo;
import com.clverpanda.nfshare.R;
import com.clverpanda.nfshare.util.database.TasksDbHelper;

import butterknife.BindView;
import butterknife.ButterKnife;


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
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
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
                TaskInfo taskInfo = new TaskInfo("下载测试", "{\"appName\":\"百度地图\",\"appVersion\":\"9.7.5\",\"pkgName\":\"com.baidu.BaiduMap\"}", 2, "测试机", 0);
                TasksDbHelper tasksDbHelper = new TasksDbHelper(getContext());
                tasksDbHelper.addTaskInfo(taskInfo);
            }
        });
        return view;
    }

    private void startDownload()
    {

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


}
