package com.clverpanda.nfshare.Util;

import android.content.Context;

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

    private DeviceInfoGetter() {}

    public static DeviceInfoGetter getInstance()
    {
        if (deviceInfoGetter == null)
            deviceInfoGetter = new DeviceInfoGetter();
        return deviceInfoGetter;
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
}
