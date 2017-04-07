package com.clverpanda.nfshare.Model;

/**
 * Created by clverpanda on 2017/4/7 0007.
 * It's the file for NFShare.
 */

public class DeviceInfo
{
    private int Id;
    private String Name;
    private String WifiMac;
    private String PublicKey;

    public DeviceInfo(int id, String name, String wifiMac, String publicKey) {
        Id = id;
        Name = name;
        WifiMac = wifiMac;
        PublicKey = publicKey;
    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getWifiMac() {
        return WifiMac;
    }

    public void setWifiMac(String wifiMac) {
        WifiMac = wifiMac;
    }

    public String getPublicKey() {
        return PublicKey;
    }

    public void setPublicKey(String publicKey) {
        PublicKey = publicKey;
    }
}
