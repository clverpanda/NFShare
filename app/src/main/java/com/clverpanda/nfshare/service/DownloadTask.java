package com.clverpanda.nfshare.service;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.clverpanda.nfshare.model.DownloadFileInfo;
import com.clverpanda.nfshare.model.DownloadThreadInfo;
import com.clverpanda.nfshare.util.database.TasksDbHelper;
import com.clverpanda.nfshare.util.database.ThreadDbHelper;

import java.io.File;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

/**
 * Created by clverpanda on 2017/4/12 0012.
 * It's the file for NFShare.
 */

class DownloadTask
{
    private Context mContext = null;
    private DownloadFileInfo mFileInfo = null;
    private ThreadDbHelper mThreadDb = null;
    private long mFinished = 0;
    boolean isPause = false;

    DownloadTask(Context mContext, DownloadFileInfo mFileInfo)
    {
        this.mContext = mContext;
        this.mFileInfo = mFileInfo;
        mThreadDb = new ThreadDbHelper(mContext);
    }

    public void download()
    {
        DownloadThreadInfo threadInfo = mThreadDb.getThread(mFileInfo.getId());
        if (threadInfo == null)
        {
            threadInfo = new DownloadThreadInfo(mFileInfo.getId(), mFileInfo.getUrl(), 0, mFileInfo.getLength(), 0);
        }
        new DownloadThread(threadInfo).start();
    }


    class DownloadThread extends Thread
    {
        private DownloadThreadInfo threadInfo;

        public DownloadThread(DownloadThreadInfo threadInfo) {
            this.threadInfo = threadInfo;
        }

        @Override
        public void run() {
            Log.e("isExists==", mThreadDb.isExists(threadInfo.getId()) + "");
            if (!mThreadDb.isExists(threadInfo.getId()))
            {
                mThreadDb.insertThread(threadInfo);
            }
            HttpURLConnection connection;
            RandomAccessFile raf;
            InputStream is;
            try {
                URL url = new URL(threadInfo.getUrl());
                connection = (HttpURLConnection) url.openConnection();
                connection.setConnectTimeout(3000);
                connection.setRequestMethod("GET");
                //设置下载位置
                long start = threadInfo.getStart() + threadInfo.getFinish();
                connection.setRequestProperty("Range", "bytes=" + start + "-" + threadInfo.getEnd());
                //设置文件写入位置
                File file = new File(DownloadService.DOWNLOAD_PATH, mFileInfo.getFileName());
                raf = new RandomAccessFile(file, "rwd");
                raf.seek(start);

                mFinished += threadInfo.getFinish();
                Log.e("threadInfo.getFinish==", threadInfo.getFinish() + "");
                Log.e("getResponseCode ===", connection.getResponseCode() + "");
                //开始下载
                if (connection.getResponseCode() == HttpURLConnection.HTTP_PARTIAL)
                {
                    Log.e("getContentLength==", connection.getContentLength() + "");
                    //读取数据
                    is = connection.getInputStream();
                    byte[] buffer = new byte[1024 * 4];
                    int len;
                    long time = System.currentTimeMillis();
                    while ((len = is.read(buffer)) != -1)
                    {
                        //下载暂停时，保存进度
                        if (isPause)
                        {
                            Log.e("mfinished==", mFinished + "");
                            mThreadDb.updateThread(mFileInfo.getId(), mFinished);
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
                    Intent intent = new Intent(DownloadService.ACTION_FINISHED);
                    intent.putExtra("fileinfo", mFileInfo);
                    mContext.sendBroadcast(intent);
                    mThreadDb.deleteThread(mFileInfo.getId());
                    TasksDbHelper tasksDb = new TasksDbHelper(mContext);
                    tasksDb.setIsDone(mFileInfo.getId(), 1);
                    is.close();
                }
                raf.close();
                connection.disconnect();
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
    }
}
