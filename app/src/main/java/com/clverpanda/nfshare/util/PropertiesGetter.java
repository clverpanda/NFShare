package com.clverpanda.nfshare.util;

import android.content.Context;

import java.io.InputStream;
import java.util.Properties;

/**
 * Created by clverpanda on 2017/5/23 0023.
 * It's the file for NFShare.
 */

public class PropertiesGetter
{
    public static Properties getProperties(Context context)
    {
        Properties urlProps;
        Properties props = new Properties();
        try
        {
            InputStream in = context.getAssets().open("appConfig");
            props.load(in);
        } catch (Exception ex)
        {
            ex.printStackTrace();
        }

        urlProps = props;
        return urlProps;
    }

    public static String getServerUrl(Context context)
    {
        return getProperties(context).getProperty("serverUrl");
    }

    public static String getStartShareUrl(Context context)
    {
        return getServerUrl(context) + "share/";
    }

    public static String getGetShareUrl(Context context)
    {
        return getServerUrl(context) + "get/";
    }

    public static String getConnErrCallbackUrl(Context context)
    {
        return getServerUrl(context) + "callback/conn_err/";
    }
}
