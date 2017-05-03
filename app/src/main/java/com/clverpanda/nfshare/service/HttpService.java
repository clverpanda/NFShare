package com.clverpanda.nfshare.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.clverpanda.nfshare.model.TransferData;
import com.clverpanda.nfshare.webserver.HttpFileServer;

import java.io.File;
import java.io.IOException;
import java.net.ServerSocket;

/**
 * Created by clverpanda on 2017/5/2 0002.
 * It's the file for NFShare.
 */

public class HttpService extends Service
{
    public static final String HTTP_SHARE_FILEINFO = "HTTP_SHARE_FILEINFO";
    public static final String ACTION_SERVER_CREATED = "ACTION_SERVER_CREATED";

    public static final String TAG = "HttpService";

    private HttpFileServer mHttpServer = null;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId)
    {
        TransferData fileData = intent.getParcelableExtra(HTTP_SHARE_FILEINFO);
        int thePort = 8080;
        try {
            ServerSocket testSocket = new ServerSocket(0);
            thePort = testSocket.getLocalPort();
            testSocket.close();
        }
        catch (IOException e)
        {
            Log.e(TAG, "onStartCommand: ", e);
        }

        while(thePort <= 65535)
        {
            try
            {
                mHttpServer = new HttpFileServer(thePort, fileData);
                mHttpServer.start();
                Log.d(TAG, "httpserver创建成功，端口号：" + thePort);
                sendServerCreatedBroadcast(thePort);
                break;
            } catch (IOException ex) {
                thePort += 1;
                Log.e(TAG, "httpserver创建失败，重试中...");
            }
        }
        return super.onStartCommand(intent, flags, startId);
    }

    protected void sendServerCreatedBroadcast(int port)
    {
        Intent intent = new Intent(HttpService.ACTION_SERVER_CREATED);
        intent.putExtra("port", port);
        sendBroadcast(intent);
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
