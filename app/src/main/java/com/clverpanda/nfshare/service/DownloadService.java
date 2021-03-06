package com.clverpanda.nfshare.service;

import android.app.Service;
import android.content.Intent;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;

import com.clverpanda.nfshare.NFShareApplication;
import com.clverpanda.nfshare.dao.DaoSession;
import com.clverpanda.nfshare.dao.Task;
import com.clverpanda.nfshare.dao.TaskDao;
import com.clverpanda.nfshare.model.DownloadFileInfo;
import com.clverpanda.nfshare.model.TaskStatus;

import java.io.File;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by clverpanda on 2017/4/12 0012.
 * It's the file for NFShare.
 */

public class DownloadService extends Service
{
    public static final int runThreadCount = 3;


    private static final String TAG = "DownloadService";
    //初始化
    private static final int MSG_INIT = 0x2;
    //开始下载
    public static final String ACTION_START = "ACTION_START";
    //暂停下载
    public static final String ACTION_PAUSE = "ACTION_PAUSE";
    //结束下载
    public static final String ACTION_FINISHED = "ACTION_FINISHED";
    //更新UI
    public static final String ACTION_UPDATE = "ACTION_UPDATE";
    public static final String ACTION_STARTED = "ACTION_STARTED";
    public static final String ACTION_PAUSED = "ACTION_PAUSED";
    public static final String ACTION_PAUSE_ALL = "ACTION_PAUSE_ALL";
    public static final String ACTION_FAILED = "ACTION_FAILED";

    public static final String DOWNLOAD_FILE_INFO = "DOWNLOAD_FILE_INFO";
    //下载路径
    public static final String DOWNLOAD_PATH = Environment.getExternalStorageDirectory().getAbsolutePath() + "/download/";

    //下载任务集合
    private Map<Long, DownloadTask> tasks = new LinkedHashMap<>();
    private TaskDao taskDao;


    @Override
    public int onStartCommand(Intent intent, int flags, int startId)
    {
        DaoSession daoSession = NFShareApplication.getInstance().getDaoSession();
        taskDao = daoSession.getTaskDao();
        if (ACTION_START.equals(intent.getAction()))
        {
            DownloadFileInfo fileInfo = intent.getParcelableExtra(DOWNLOAD_FILE_INFO);
            if (tasks.containsKey(fileInfo.getId()))
            {
                if (!tasks.get(fileInfo.getId()).isPause)
                    return super.onStartCommand(intent, flags, startId);
            }
            Log.e(TAG, "onStartCommand: ACTION_START-" + fileInfo.toString());
            setStarted(fileInfo.getId());
            new InitThread(fileInfo).start();
        }
        else if (ACTION_PAUSE.equals(intent.getAction()))
        {
            DownloadFileInfo fileInfo = intent.getParcelableExtra(DOWNLOAD_FILE_INFO);
            Log.e(TAG, "onStartCommand:ACTION_PAUSE- " + fileInfo.toString());
            DownloadTask task = tasks.get(fileInfo.getId());
            if (task != null)
            {
                task.isPause = true;
            }

        }
        return super.onStartCommand(intent, flags, startId);
    }

    protected void setStarted(long taskId)
    {
        Task task = taskDao.load(taskId);
        task.setStatus(TaskStatus.RUNNING);
        taskDao.update(task);
        Intent intent = new Intent(DownloadService.ACTION_STARTED);
        intent.putExtra("id", taskId);
        sendBroadcast(intent);
    }

    protected void setFailed(long taskId)
    {
        Task task = taskDao.load(taskId);
        task.setStatus(TaskStatus.FAILED);
        taskDao.update(task);
        Intent intent = new Intent(DownloadService.ACTION_FAILED);
        intent.putExtra("id", taskId);
        sendBroadcast(intent);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_INIT:
                    DownloadFileInfo fileinfo = (DownloadFileInfo) msg.obj;
                    Log.e("mHandler--fileinfo:", fileinfo.toString());
                    //启动下载任务
                    DownloadTask downloadTask = new DownloadTask(DownloadService.this, fileinfo);
                    downloadTask.download();

                    tasks.put(fileinfo.getId(), downloadTask);
                    break;
            }
        }
    };


    class InitThread extends Thread
    {
        private DownloadFileInfo tFileInfo;

        public InitThread(DownloadFileInfo tFileInfo) {
            this.tFileInfo = tFileInfo;
        }

        @Override
        public void run()
        {
            OkHttpClient client = new OkHttpClient.Builder()
                    .connectTimeout(3, TimeUnit.SECONDS)
                    .build();
            RandomAccessFile raf ;
            try {
                //连接网络文件
                URL url = new URL(tFileInfo.getUrl());
                Request request = new Request.Builder().url(url).build();
                Response response = client.newCall(request).execute();
                long length = -1;
                Log.e("getResponseCode==", response.code() + "");
                if (response.isSuccessful())
                {
                    //获取文件长度
                    length = response.body().contentLength();
                    Log.e("length==", length + "");
                }
                else
                    throw new Exception();
                if (length < 0) {
                    return;
                }
                File dir = new File(DOWNLOAD_PATH);
                if (!dir.exists()) {
                    if (!dir.mkdir()){
                        return;
                    }
                }
                //在本地创建文件
                File file = new File(dir, tFileInfo.getFileName());
                raf = new RandomAccessFile(file, "rwd");
                //设置本地文件长度
                raf.setLength(length);
                tFileInfo.setLength(length);
                Log.e("tFileInfo.getLength==", tFileInfo.getLength() + "");
                mHandler.obtainMessage(MSG_INIT, tFileInfo).sendToTarget();

                raf.close();
                response.close();
            }
            catch (Exception e)
            {
                e.printStackTrace();
                setFailed(tFileInfo.getId());
            }
        }
    }
}
