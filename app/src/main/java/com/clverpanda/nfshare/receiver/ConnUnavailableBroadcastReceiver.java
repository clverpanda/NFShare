package com.clverpanda.nfshare.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.clverpanda.nfshare.CloudSendActivity;

import org.json.JSONObject;

import java.util.Iterator;

/**
 * Created by clverpanda on 2017/5/31 0031.
 * It's the file for NFShare.
 */

public class ConnUnavailableBroadcastReceiver extends BroadcastReceiver
{
    private static final String TAG = "ConnUnavailable";
    public static final String ACTION_CONN_UNAVAILABLE = "com.clverpanda.nfshare.connunavailable";

    private CloudSendActivity activity;

    public ConnUnavailableBroadcastReceiver(CloudSendActivity activity)
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
            if (ACTION_CONN_UNAVAILABLE.equals(action))
            {
                JSONObject json = new JSONObject(intent.getExtras().getString("com.avos.avoscloud.Data"));

                Log.d(TAG, "got action " + action + " with:");
                Iterator itr = json.keys();
                while (itr.hasNext())
                {
                    String key = (String) itr.next();
                    if ("id".equals(key))
                    {
                        int theId = json.getInt(key);
                        activity.confirmUseCloudUpdate(theId);
                        break;
                    }
                }
            }
        } catch (Exception e) {
            Log.d(TAG, "Exception: " + e.getMessage());
        }
    }
}
