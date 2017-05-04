package com.clverpanda.nfshare.service;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.clverpanda.nfshare.NFShareApplication;
import com.clverpanda.nfshare.dao.DaoSession;
import com.clverpanda.nfshare.dao.Task;
import com.clverpanda.nfshare.dao.TaskDao;
import com.clverpanda.nfshare.dao.TransferProgress;
import com.clverpanda.nfshare.dao.TransferProgressDao;
import com.clverpanda.nfshare.model.DownloadFileInfo;
import com.clverpanda.nfshare.model.DownloadThreadInfo;
import com.clverpanda.nfshare.model.TaskStatus;


import java.io.File;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by clverpanda on 2017/4/12 0012.
 * It's the file for NFShare.
 */

class DownloadTask
{
    private Context mContext = null;
    private DownloadFileInfo mFileInfo = null;
    private TaskDao taskDao;
    private TransferProgressDao transferProgressDao;
    private long mFinished = 0;
    boolean isPause = false;

    DownloadTask(Context mContext, DownloadFileInfo mFileInfo)
    {
        this.mContext = mContext;
        this.mFileInfo = mFileInfo;
        DaoSession daoSession = NFShareApplication.getInstance().getDaoSession();
        taskDao = daoSession.getTaskDao();
        transferProgressDao = daoSession.getTransferProgressDao();
    }

    public void download()
    {
        TransferProgress threadInfo = taskDao.loadDeep(mFileInfo.getId()).getTransferProgress();
        if (threadInfo == null)
        {
            threadInfo = new TransferProgress((long) 0, mFileInfo.getLength() - 1);
            threadInfo.setTask(taskDao.load(mFileInfo.getId()));
        }
        new DownloadThread(threadInfo).start();
    }

    protected void setFailed(long taskId)
    {
        Task task = taskDao.load(taskId);
        task.setStatus(TaskStatus.FAILED);
        taskDao.update(task);
        Intent intent = new Intent(DownloadService.ACTION_FAILED);
        intent.putExtra("id", taskId);
        mContext.sendBroadcast(intent);
    }

    class DownloadThread extends Thread
    {
        private TransferProgress threadInfo;
        private OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(3, TimeUnit.SECONDS)
                .build();

        public DownloadThread(TransferProgress threadInfo) {
            this.threadInfo = threadInfo;
        }

        @Override
        public void run()
        {
            Log.e("isExists==", transferProgressDao.hasKey(threadInfo) + "");
            if (!transferProgressDao.hasKey(threadInfo))
            {
                transferProgressDao.insert(threadInfo);
            }
            RandomAccessFile raf;
            InputStream is;
            try {
                URL url = new URL(mFileInfo.getUrl());

                //设置下载位置
                long start = threadInfo.getTransferredPosition();
                Request request = new Request.Builder()
                        .url(url)
                        .addHeader("Range", "bytes=" + start + "-" + threadInfo.getEndPosition())
                        .build();
                //设置文件写入位置
                File file = new File(DownloadService.DOWNLOAD_PATH, mFileInfo.getFileName());
                raf = new RandomAccessFile(file, "rwd");
                raf.seek(start);
                mFinished += threadInfo.getTransferredPosition();

                Response response = client.newCall(request).execute();
                Log.e("getResponseCode ===", response.code() + "");
                //开始下载
                if (response.code() == HttpURLConnection.HTTP_PARTIAL)
                {
                    Log.e("getContentLength==", response.body().contentLength() + "");
                    //读取数据
                    is = response.body().byteStream();
                    byte[] buffer = new byte[1024 * 4];
                    int len;
                    long time = System.currentTimeMillis();
                    while ((len = is.read(buffer)) != -1)
                    {
                        //下载暂停时，保存进度
                        if (isPause)
                        {
                            Log.e("mfinished==", mFinished + "");
                            threadInfo.setTransferredPosition(mFinished);
                            Task theTask = threadInfo.getTask();
                            theTask.setStatus(TaskStatus.PAUSED);
                            transferProgressDao.update(threadInfo);
                            taskDao.update(theTask);
                            Intent intent = new Intent(DownloadService.ACTION_PAUSED);
                            intent.putExtra("id", mFileInfo.getId());
                            mContext.sendBroadcast(intent);
                            return;
                        }
                        //写入文件
                        raf.write(buffer, 0, len);
                        //把下载进度发送广播给Activity
                        mFinished += len;
                        if (System.currentTimeMillis() - time > 600)
                        {
                            time = System.currentTimeMillis();
                            Intent intent = new Intent(DownloadService.ACTION_UPDATE);
                            intent.putExtra("finished", (int)(mFinished * 100 / mFileInfo.getLength()));
                            intent.putExtra("id", mFileInfo.getId());
                            mContext.sendBroadcast(intent);
                            Log.e(" mFinished percent===", mFinished * 100 / mFileInfo.getLength() + "");
                        }

                    }
                    Task theTask = threadInfo.getTask();
                    theTask.setStatus(TaskStatus.DONE);
                    taskDao.update(theTask);
                    transferProgressDao.delete(threadInfo);
                    Intent intent = new Intent(DownloadService.ACTION_FINISHED);
                    intent.putExtra("fileinfo", mFileInfo);
                    mContext.sendBroadcast(intent);
                    is.close();
                }
                raf.close();
                response.close();
            }
            catch (Exception e)
            {
                e.printStackTrace();
                setFailed(mFileInfo.getId());
            }
        }
    }
}
