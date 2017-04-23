package com.clverpanda.nfshare.dao;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Index;
import org.greenrobot.greendao.annotation.NotNull;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by clverpanda on 2017/4/21 0021.
 * It's the file for NFShare.
 */

@Entity
public class Device
{
    @Id(autoincrement = true)
    private Long Id;

    @NotNull
    private String Name;

    @NotNull
    @Index(unique = true)
    private String WiFiMac;

    private String PublicKey;

    public Device(String name, String wiFiMac, String publicKey)
    {
        Name = name;
        WiFiMac = wiFiMac;
        PublicKey = publicKey;
    }

    @Generated(hash = 1333542263)
    public Device(Long Id, @NotNull String Name, @NotNull String WiFiMac,
            String PublicKey) {
        this.Id = Id;
        this.Name = Name;
        this.WiFiMac = WiFiMac;
        this.PublicKey = PublicKey;
    }
    @Generated(hash = 1469582394)
    public Device() {
    }
    public Long getId() {
        return this.Id;
    }
    public void setId(Long Id) {
        this.Id = Id;
    }
    public String getName() {
        return this.Name;
    }
    public void setName(String Name) {
        this.Name = Name;
    }
    public String getWiFiMac() {
        return this.WiFiMac;
    }
    public void setWiFiMac(String WiFiMac) {
        this.WiFiMac = WiFiMac;
    }
    public String getPublicKey() {
        return this.PublicKey;
    }
    public void setPublicKey(String PublicKey) {
        this.PublicKey = PublicKey;
    }
}
