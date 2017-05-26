package com.clverpanda.nfshare.util;

import android.content.ContentResolver;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.provider.Settings;

import com.clverpanda.nfshare.dao.Device;

import java.net.NetworkInterface;
import java.util.Collections;
import java.util.List;

/**
 * Created by clverpanda on 2017/4/10 0010.
 * It's the file for NFShare.
 */

public class DeviceInfoGetter
{
    private static DeviceInfoGetter deviceInfoGetter;
    private ContentResolver contentResolver;
    private SharedPreferences spLeanCloud;
    private WifiManager wifiManager;
    private ConnectivityManager connectivityManager;


    private DeviceInfoGetter(Context context)
    {
        contentResolver = context.getContentResolver();
        spLeanCloud = context.getSharedPreferences("leancloud", Context.MODE_PRIVATE);
        wifiManager = (WifiManager) context.getApplicationContext()
                .getSystemService(Context.WIFI_SERVICE);
        connectivityManager = (ConnectivityManager) context.getApplicationContext()
                .getSystemService(Context.CONNECTIVITY_SERVICE);
    }

    public static DeviceInfoGetter getInstance(Context context)
    {
        if (deviceInfoGetter == null)
            deviceInfoGetter = new DeviceInfoGetter(context);
        return deviceInfoGetter;
    }

    public Device getDeviceInfo()
    {
        return new Device(getDeviceName(), getMacAddr(), "");
    }


    public String getMacAddr() {
        try {
            List<NetworkInterface> all = Collections.list(NetworkInterface.getNetworkInterfaces());
            for (NetworkInterface nif : all) {
                if (!nif.getName().equalsIgnoreCase("wlan0")) continue;

                byte[] macBytes = nif.getHardwareAddress();
                if (macBytes == null) {
                    return "";
                }

                StringBuilder res1 = new StringBuilder();
                for (byte b : macBytes) {
                    res1.append(String.format("%02X:",b));
                }

                if (res1.length() > 0) {
                    res1.deleteCharAt(res1.length() - 1);
                }
                return res1.toString();
            }
        } catch (Exception ex) {
        }
        return "02:00:00:00:00:00";
    }

    public String getDeviceName()
    {
        String result = Settings.Secure.getString(contentResolver, "bluetooth_name");
        if (result == null)
            result = Build.MODEL;
        return result;
    }

    public String getTencentInstallationId()
    {
        return spLeanCloud.getString("installationId", null);
    }

    public boolean isUsingWifi()
    {
        NetworkInfo activeNetInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetInfo != null
                && activeNetInfo.getType() == ConnectivityManager.TYPE_WIFI;
    }

    public String getWifiIpAddress()
    {
        if (isUsingWifi())
        {
            WifiInfo wifiInfo = wifiManager.getConnectionInfo();
            return intToIp(wifiInfo.getIpAddress());
        }
        else
            return null;
    }

    private String intToIp(int i)
    {
        return (i & 0xFF) + "." + ((i >> 8) & 0xFF) + "." + ((i >> 16) & 0xFF)
                + "." + (i >> 24 & 0xFF);
    }
}
