package com.clverpanda.nfshare.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.clverpanda.nfshare.webserver.HttpFileServer;

import java.io.File;
import java.io.IOException;

/**
 * Created by clverpanda on 2017/5/2 0002.
 * It's the file for NFShare.
 */

public class HttpService extends Service
{
    public static final String HTTP_SHARE_FILEPATH = "http_share_filepath";

    public static final String TAG = "HttpService";

    private HttpFileServer mHttpServer = null;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId)
    {
        //String filePath2Supply = intent.getStringExtra(HTTP_SHARE_FILEPATH);
        //mHttpServer = new HttpFileServer(8080, new File(filePath2Supply));
        mHttpServer = new HttpFileServer(8080);
        try
        {
            mHttpServer.start();
        }
        catch (IOException ex)
        {
            Log.e(TAG, "onStartCommand: ", ex);
        }
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();
        if (mHttpServer != null)
            mHttpServer.stop();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
