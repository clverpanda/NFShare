package com.clverpanda.nfshare.model;

import java.io.Serializable;

/**
 * Created by clverpanda on 2017/4/5 0005.
 * It's the file for NFShare.
 */

public class NFCTransferData implements Serializable
{
    private DataType dataType;
    private DeviceInfo deviceInfo;
    private String payload;

    public NFCTransferData() {}

    public NFCTransferData(DataType dataType, DeviceInfo deviceInfo, String payload) {
        this.dataType = dataType;
        this.deviceInfo = deviceInfo;
        this.payload = payload;
    }

    public DataType getDataType() {
        return dataType;
    }

    public void setDataType(DataType dataType) {
        this.dataType = dataType;
    }

    public DeviceInfo getDeviceInfo() {
        return deviceInfo;
    }

    public void setDeviceInfo(DeviceInfo deviceInfo) {
        this.deviceInfo = deviceInfo;
    }

    public String getPayload() {
        return payload;
    }

    public void setPayload(String payload) {
        this.payload = payload;
    }
}
