package com.clverpanda.nfshare;

import android.app.Application;

import com.liulishuo.filedownloader.FileDownloader;

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

        FileDownloader.init(getApplicationContext());
    }
}
