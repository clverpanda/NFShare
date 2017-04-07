package com.clverpanda.nfshare;

import android.app.Application;

import com.liulishuo.filedownloader.FileDownloader;
import com.liulishuo.filedownloader.connection.FileDownloadUrlConnection;
import com.liulishuo.filedownloader.services.DownloadMgrInitialParams;

import java.net.Proxy;

/**
 * Created by clverpanda on 2017/4/6 0006.
 * It's the file for NFShare.
 */

public class NFSApplication extends Application
{
    @Override
    public void onCreate()
    {
        super.onCreate();

        FileDownloader.init(getApplicationContext(), new DownloadMgrInitialParams.InitCustomMaker()
                .connectionCreator(new FileDownloadUrlConnection
                        .Creator(new FileDownloadUrlConnection.Configuration()
                        .connectTimeout(15_000)
                        .readTimeout(15_000)
                        .proxy(Proxy.NO_PROXY)
                )));
    }
}
