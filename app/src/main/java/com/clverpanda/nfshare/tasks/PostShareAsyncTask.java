package com.clverpanda.nfshare.tasks;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.clverpanda.nfshare.model.communicate.receive.StartShareRec;
import com.clverpanda.nfshare.model.communicate.send.StartShareSend;
import com.clverpanda.nfshare.util.PropertiesGetter;

import java.io.IOException;
import java.net.URL;
import java.util.concurrent.TimeUnit;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by clverpanda on 2017/5/24 0024.
 * It's the file for NFShare.
 */

public class PostShareAsyncTask extends AsyncTask<StartShareSend, Void, Integer>
{
    public static final MediaType JSON_TYPE = MediaType.parse("application/json; charset=utf-8");
    private static final String TAG = "PostShareAsyncTask";

    private Context context;
    private TextView tvShareWord;
    private TextView tvSharePin;
    public PostShareAsyncTask(Context context, TextView tvShareWord, TextView tvSharePin)
    {
        this.context = context;
        this.tvShareWord = tvShareWord;
        this.tvSharePin = tvSharePin;
    }

    @Override
    protected Integer doInBackground(StartShareSend... params)
    {
        if (params.length <= 0) return -1;
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(5, TimeUnit.SECONDS)
                .build();
        try
        {
            URL url = new URL(PropertiesGetter.getStartShareUrl(context));
            StartShareSend ssInfo = params[0];
            RequestBody requestBody = RequestBody.create(JSON_TYPE,
                    JSON.toJSONString(ssInfo, SerializerFeature.WriteMapNullValue));
            Request request = new Request.Builder()
                    .url(url)
                    .post(requestBody)
                    .build();
            Response response = client.newCall(request).execute();
            if (response.isSuccessful())
            {
                String serverResult = response.body().string();
                Log.i(TAG, serverResult);
                StartShareRec receivedData = JSON.parseObject(serverResult, StartShareRec.class);
                return receivedData.getPin();
            }
            else
                return -1;
        }
        catch (IOException e)
        {
            Log.e(TAG, "error: connect to server");
            return -1;
        }
    }

    @Override
    protected void onPostExecute(Integer result)
    {
        if (result == -1)
        {
            Toast.makeText(context, "连接服务器失败，请返回重试", Toast.LENGTH_SHORT).show();
        }
        else
        {
            tvShareWord.setVisibility(View.VISIBLE);
            tvSharePin.setText(String.valueOf(result));
            tvSharePin.setVisibility(View.VISIBLE);
        }
    }
}
