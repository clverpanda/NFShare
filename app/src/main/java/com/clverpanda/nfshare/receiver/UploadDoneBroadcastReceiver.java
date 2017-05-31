package com.clverpanda.nfshare.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.clverpanda.nfshare.MainActivity;

import org.json.JSONObject;

import java.util.Iterator;

/**
 * Created by clverpanda on 2017/5/31 0031.
 * It's the file for NFShare.
 */

public class UploadDoneBroadcastReceiver extends BroadcastReceiver
{
    private static final String TAG = "UploadDone";
    public static final String ACTION_UPLOAD_DONE = "com.clverpanda.nfshare.uploaddone";

    private MainActivity activity;

    public UploadDoneBroadcastReceiver(MainActivity activity)
    {
        super();
        this.activity = activity;
    }
    @Override
    public void onReceive(Context context, Intent intent)
    {
        Log.d(TAG, "Get Broadcat");
        try {
            String action = intent.getAction();
            if (ACTION_UPLOAD_DONE.equals(action))
            {
                JSONObject json = new JSONObject(intent.getExtras().getString("com.avos.avoscloud.Data"));

                Log.d(TAG, "got action " + action + " with:");
                Iterator itr = json.keys();
                while (itr.hasNext())
                {
                    String key = (String) itr.next();
                    if ("description".equals(key))
                    {
                        String task_description = json.getString(key);
                        Log.d(TAG, "TASK_DESC:" + task_description);
                        activity.addTaskFromMessage(task_description);
                        break;
                    }
                }
            }
        } catch (Exception e) {
            Log.d(TAG, "Exception: " + e.getMessage());
        }
    }
}
