package com.clverpanda.nfshare.tasks;

import android.os.AsyncTask;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.clverpanda.nfshare.NFShareApplication;
import com.clverpanda.nfshare.dao.DaoSession;
import com.clverpanda.nfshare.dao.Task;
import com.clverpanda.nfshare.model.AppInfo;
import com.clverpanda.nfshare.model.FileInfo;
import com.clverpanda.nfshare.model.TaskStatus;
import com.clverpanda.nfshare.model.TransferData;

import java.io.IOException;
import java.net.URL;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by clverpanda on 2017/5/26 0026.
 * It's the file for NFShare.
 */

public class ConnectServerAsyncTask extends AsyncTask<String, Void, String>
{
    public static final String TAG = "ConnectServerAsyncTask";
    public AsyncResponse<String> asyncResponse;

    public ConnectServerAsyncTask()
    {

    }

    public void setOnAsyncResponse(AsyncResponse<String> asyncResponse)
    {
        this.asyncResponse = asyncResponse;
    }

    @Override
    protected String doInBackground(String... params)
    {
        if (params.length <= 0) return null;
        String urlstr = params[0];
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(3, TimeUnit.SECONDS)
                .build();
        try
        {
            URL url = new URL(urlstr);
            Request request = new Request.Builder().url(url).build();
            Response response = client.newCall(request).execute();
            if (response.isSuccessful())
            {
                Log.d(TAG, "run: 服务器连接成功");
                String result = response.body().string();
                Log.d(TAG, "run: " + result);
                return result;
            }
            else
                return null;
        }
        catch (IOException e)
        {
            Log.e(TAG, "run: ", e);
            return null;
        }
    }

    @Override
    protected void onPostExecute(String result)
    {
        if (result == null)
            asyncResponse.onDataReceivedFailed();
        else
            asyncResponse.onDataReceivedSuccess(result);
    }
}
