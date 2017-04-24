package com.clverpanda.nfshare.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.clverpanda.nfshare.dao.Device;

import java.io.Serializable;

/**
 * Created by clverpanda on 2017/4/5 0005.
 * It's the file for NFShare.
 */

public class TransferData implements Parcelable
{
    private DataType dataType;
    private Device device;
    private String payload;

    public TransferData() {}

    public TransferData(DataType dataType, Device device, String payload) {
        this.dataType = dataType;
        this.device = device;
        this.payload = payload;
    }

    public DataType getDataType() {
        return dataType;
    }

    public void setDataType(DataType dataType) {
        this.dataType = dataType;
    }

    public Device getDevice() {
        return device;
    }

    public void setDevice(Device device) {
        this.device = device;
    }

    public String getPayload() {
        return payload;
    }

    public void setPayload(String payload) {
        this.payload = payload;
    }

    @Override
    public int describeContents()
    {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags)
    {
        dest.writeSerializable(dataType);
        dest.writeParcelable(device, flags);
        dest.writeString(payload);
    }

    public static final Parcelable.Creator<TransferData> CREATOR=  new Creator<TransferData>()
    {
        @Override
        public TransferData createFromParcel(Parcel source)
        {
            TransferData data = new TransferData();
            data.setDataType((DataType) source.readSerializable());
            data.setDevice((Device) source.readParcelable(Device.class.getClassLoader()));
            data.setPayload(source.readString());
            return data;
        }

        @Override
        public TransferData[] newArray(int size)
        {
            return new TransferData[size];
        }
    };
}
