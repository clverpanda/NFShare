package com.clverpanda.nfshare.model;

import com.clverpanda.nfshare.dao.Device;

import java.io.Serializable;

/**
 * Created by clverpanda on 2017/4/5 0005.
 * It's the file for NFShare.
 */

public class TransferData implements Serializable
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
}
