package com.clverpanda.nfshare.taskslist;


import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.clverpanda.nfshare.model.DownloadFileInfo;
import com.clverpanda.nfshare.model.TaskInfo;
import com.clverpanda.nfshare.R;
import com.clverpanda.nfshare.service.DownloadService;
import com.clverpanda.nfshare.util.database.TasksDbHelper;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class RunningTasksFrag extends Fragment {

    private static final int REQUEST_CODE_ASK_WRITE = 111;
    private static final String TAG = "Download";


    @BindView(R.id.running_tasks_recyclerView)
    RecyclerView recyclerView;

    protected RunningRecyclerAdapter mAdapter;
    protected TasksDbHelper tasksDb;


    public RunningTasksFrag() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.content_running_tasks, container, false);
        ButterKnife.bind(this, view);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        layoutManager.setOrientation(OrientationHelper.VERTICAL);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        IntentFilter filter = new IntentFilter();
        filter.addAction(DownloadService.ACTION_UPDATE);
        filter.addAction(DownloadService.ACTION_FINISHED);
        getActivity().registerReceiver(mReceiver, filter);

        tasksDb = new TasksDbHelper(getContext());
        List<TaskInfo> allRunningTasks = tasksDb.getAllRunningTaskInfo();
        mAdapter = new RunningRecyclerAdapter(getContext(), allRunningTasks);
        recyclerView.setAdapter(mAdapter);

        return view;
    }

    @Override
    public void onResume()
    {
        super.onResume();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
        {
            if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED)
                requestPermissions(new String[] {Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_CODE_ASK_WRITE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)
    {
        switch (requestCode) {
            case REQUEST_CODE_ASK_WRITE:
                if (grantResults[0] != PackageManager.PERMISSION_GRANTED)
                {
                    Toast.makeText(getContext(), "没有写入外部存储权限，程序可能出错！", Toast.LENGTH_LONG).show();
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    BroadcastReceiver mReceiver = new BroadcastReceiver()
    {
        @Override
        public void onReceive(Context context, Intent intent)
        {
            if (DownloadService.ACTION_UPDATE.equals(intent.getAction()))
            {
                int finished = intent.getIntExtra("finished", 0);
                int id = intent.getIntExtra("id", 0);
                Log.e(TAG, "finished==" + finished);
                Log.e(TAG, "id==" + id);
                mAdapter.updateProgress(id, finished);
            }
            else if (DownloadService.ACTION_FINISHED.equals(intent.getAction()))
            {
                DownloadFileInfo fileinfo = (DownloadFileInfo) intent.getSerializableExtra("fileinfo");
                //更新进度为100
                mAdapter.updateProgress(fileinfo.getId(), 100);
                mAdapter.removeTask(fileinfo.getId());
                Toast.makeText(
                        getActivity(),
                        fileinfo.getFileName() + "下载完成",
                        Toast.LENGTH_SHORT).show();
            }
        }
    };

}
