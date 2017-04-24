package com.clverpanda.nfshare.dao;

import android.os.Parcel;
import android.os.Parcelable;

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
public class Device implements Parcelable
{
    @Id(autoincrement = true)
    private long Id;

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

    @Generated(hash = 529381642)
    public Device(long Id, @NotNull String Name, @NotNull String WiFiMac,
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
    public void setId(long Id) {
        this.Id = Id;
    }




    /////实现 parcelable
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags)
    {
        dest.writeLong(Id);
        dest.writeString(Name);
        dest.writeString(WiFiMac);
        dest.writeString(PublicKey);
    }



    public static final Parcelable.Creator<Device> CREATOR = new Creator<Device>()
    {
        @Override
        public Device createFromParcel(Parcel source)
        {
            Device device = new Device();
            device.setId(source.readLong());
            device.setName(source.readString());
            device.setWiFiMac(source.readString());
            device.setPublicKey(source.readString());
            return device;
        }

        @Override
        public Device[] newArray(int size) {
            return new Device[size];
        }
    };
}
