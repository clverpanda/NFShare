package com.clverpanda.nfshare.service;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.clverpanda.nfshare.model.DownloadFileInfo;
import com.clverpanda.nfshare.model.DownloadThreadInfo;
import com.clverpanda.nfshare.util.database.ThreadDbHelper;

import java.io.File;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by clverpanda on 2017/4/12 0012.
 * It's the file for NFShare.
 */

public class DownloadTask
{
    private Context mContext = null;
    private DownloadFileInfo mFileInfo = null;
    private ThreadDbHelper mThreadDb = null;
    private long mFinished = 0;
    private int mThreadCount = DownloadService.runThreadCount;
    public boolean isPause = false;
    //线程池
    public static ExecutorService sExecutorService = Executors.newCachedThreadPool();

    private List<DownloadThread> mThradList = null;

    public DownloadTask(Context mContext, DownloadFileInfo mFileInfo, int threadCount)
    {
        this.mContext = mContext;
        this.mFileInfo = mFileInfo;
        this.mThreadDb = new ThreadDbHelper(mContext);
        this.mThreadCount = threadCount;
    }

    public void download() {
        //读取数据库的线程信息
        List<DownloadThreadInfo> threadInfos = mThreadDb.getThread(mFileInfo.getUrl());
        Log.e("threadsize==", threadInfos.size() + "");

        if (threadInfos.size() == 0) {
            //获得每个线程下载的长度
            long length = mFileInfo.getLength() / mThreadCount;
            for (int i = 0; i < mThreadCount; i++) {
                DownloadThreadInfo threadInfo = new DownloadThreadInfo(i, mFileInfo.getUrl(), length * i, (i + 1) * length - 1, 0);
                if (i + 1 == mThreadCount) {
                    threadInfo.setEnd(mFileInfo.getLength());
                }
                //添加到线程信息集合中
                threadInfos.add(threadInfo);

                //向数据库插入线程信息
                mThreadDb.insertThread(threadInfo);
            }
        }
        mThradList = new ArrayList<>();
        //启动多个线程进行下载
        for (DownloadThreadInfo thread : threadInfos) {
            DownloadThread downloadThread = new DownloadThread(thread);
//            downloadThread.start();
            DownloadTask.sExecutorService.execute(downloadThread);
            //添加线程到集合中
            mThradList.add(downloadThread);
        }
    }


    /**
     * 下载线程
     */
    class DownloadThread extends Thread {
        private DownloadThreadInfo threadInfo;
        public boolean isFinished = false;

        public DownloadThread(DownloadThreadInfo threadInfo) {
            this.threadInfo = threadInfo;
        }

        @Override
        public void run()
        {
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

                Intent intent = new Intent(DownloadService.ACTION_UPDATE);
                mFinished += threadInfo.getFinish();
                Log.e("threadInfo.getFinish==", threadInfo.getFinish() + "");

//                Log.e("getResponseCode ===", connection.getResponseCode() + "");
                //开始下载
                if (connection.getResponseCode() == HttpURLConnection.HTTP_PARTIAL) {
                    Log.e("getContentLength==", connection.getContentLength() + "");

                    //读取数据
                    is = connection.getInputStream();
                    byte[] buffer = new byte[1024 * 4];
                    int len = -1;
                    long time = System.currentTimeMillis();
                    while ((len = is.read(buffer)) != -1) {

                        if (isPause) {
                            Log.e("mfinished==pause===", mFinished + "");
                            //下载暂停时，保存进度到数据库
                            mThreadDb.updateThread(mFileInfo.getUrl(), mFileInfo.getId(), threadInfo.getFinish());
                            return;
                        }

                        //写入文件
                        raf.write(buffer, 0, len);
                        //累加整个文件下载进度
                        mFinished += len;
                        //累加每个线程完成的进度
                        threadInfo.setFinish(threadInfo.getFinish() + len);
                        //每隔1秒刷新UI
                        if (System.currentTimeMillis() - time > 1000) {//减少UI负载
                            time = System.currentTimeMillis();
                            //把下载进度发送广播给Activity
                            intent.putExtra("id", mFileInfo.getId());
                            intent.putExtra("finished", mFinished * 100 / mFileInfo.getLength());
                            mContext.sendBroadcast(intent);
                            Log.e(" mFinished==update==", mFinished * 100 / mFileInfo.getLength() + "");
                        }

                    }
                    //标识线程执行完毕
                    isFinished = true;
                    //检查下载任务是否完成
                    checkAllThreadFinished();
//                    //删除线程信息
//                    mThreadDb.deleteThread(mFileInfo.getUrl(), mFileInfo.getId());
                    is.close();
                }
                raf.close();
                connection.disconnect();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 判断所有线程是否都执行完毕
     */
    private synchronized void checkAllThreadFinished() {
        boolean allFinished = true;
        //编辑线程集合 判断是否执行完毕
        for (DownloadThread thread : mThradList) {
            if (!thread.isFinished) {
                allFinished = false;
                break;
            }
        }
        if (allFinished) {
            //删除线程信息
            mThreadDb.deleteThread(mFileInfo.getUrl());
            //发送广播给Activity下载结束
            Intent intent = new Intent(DownloadService.ACTION_FINISHED);
            intent.putExtra("fileinfo", mFileInfo);
            mContext.sendBroadcast(intent);
        }
    }
}
