package com.clverpanda.nfshare.tasks;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.clverpanda.nfshare.model.communicate.receive.GetShareRec;
import com.clverpanda.nfshare.model.communicate.receive.StartShareRec;
import com.clverpanda.nfshare.model.communicate.send.GetShareSend;
import com.clverpanda.nfshare.model.communicate.send.StartShareSend;
import com.clverpanda.nfshare.util.PropertiesGetter;

import java.io.IOException;
import java.net.URL;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static com.clverpanda.nfshare.tasks.PostShareAsyncTask.JSON_TYPE;

/**
 * Created by clverpanda on 2017/5/27 0027.
 * It's the file for NFShare.
 */

public class PostGetShareAsyncTask extends AsyncTask<GetShareSend, Void, GetShareRec>
{
    private Context context;
    private static final String TAG = "PostGetShareAsyncTask";
    private AsyncResponse<GetShareRec> asyncResponse;


    public PostGetShareAsyncTask(Context context)
    {
        this.context = context;
    }

    public void setAsyncResponse(AsyncResponse<GetShareRec> asyncResponse)
    {
        this.asyncResponse = asyncResponse;
    }

    @Override
    protected GetShareRec doInBackground(GetShareSend... params)
    {
        if (params.length <= 0) return null;
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(3, TimeUnit.SECONDS)
                .build();
        try
        {
            URL url = new URL(PropertiesGetter.getGetShareUrl(context));
            GetShareSend gsInfo = params[0];
            RequestBody requestBody = RequestBody.create(JSON_TYPE, JSON.toJSONString(gsInfo));
            Request request = new Request.Builder()
                    .url(url)
                    .post(requestBody)
                    .build();
            Response response = client.newCall(request).execute();
            if (response.isSuccessful())
            {
                String serverResult = response.body().string();
                Log.i(TAG, serverResult);
                return JSON.parseObject(serverResult, GetShareRec.class);
            }
            else
                return null;
        }
        catch (IOException e)
        {
            Log.e(TAG, "error: connect to get_share");
            return null;
        }
    }

    @Override
    protected void onPostExecute(GetShareRec result)
    {
        if (result == null)
        {
            asyncResponse.onDataReceivedFailed();
        }
        else
        {
            asyncResponse.onDataReceivedSuccess(result);
        }
    }
}
