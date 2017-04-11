package com.clverpanda.nfshare.Model;

/**
 * Created by clverpanda on 2017/4/5 0005.
 * It's the file for NFShare.
 */

public class NFCTransferData
{
    private DataType dataType;
    private DeviceInfo deviceInfo;
    private Object payload;

    public NFCTransferData(DataType dataType, DeviceInfo deviceInfo, Object payload) {
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

    public Object getPayload() {
        return payload;
    }

    public void setPayload(Object payload) {
        this.payload = payload;
    }
}
