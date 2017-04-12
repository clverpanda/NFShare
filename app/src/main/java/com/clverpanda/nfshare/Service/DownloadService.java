package com.clverpanda.nfshare.Service;

import android.app.Service;
import android.content.Intent;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.URL;
/**
 * Created by clverpanda on 2017/4/12 0012.
 * It's the file for NFShare.
 */

//public class DownloadService extends Service
//{
//    private static final String TAG = "DownloadService";
//    //初始化
//    private static final int MSG_INIT = 0;
//    //开始下载
//    public static final String ACTION_START = "ACTION_START";
//    //暂停下载
//    public static final String ACTION_PAUSE = "ACTION_PAUSE";
//    //结束下载
//    public static final String ACTION_FINISHED = "ACTION_FINISHED";
//    //更新UI
//    public static final String ACTION_UPDATE = "ACTION_UPDATE";
//    //下载路径
//    public static final String DOWNLOAD_PATH = Environment.getExternalStorageDirectory().getAbsolutePath() + "/downloads/";
//
//    private DownloadTask mDownloadTask = null;
//}
