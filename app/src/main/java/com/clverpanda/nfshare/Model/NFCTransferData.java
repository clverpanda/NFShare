package com.clverpanda.nfshare.Model;

/**
 * Created by clverpanda on 2017/4/5 0005.
 * It's the file for NFShare.
 */

public class NFCTransferData
{
    private DataType dataType;
    private String payload;

    public NFCTransferData(DataType dataType, String payload)
    {
        this.dataType = dataType;
        this.payload = payload;
    }

    public DataType getDataType() {
        return dataType;
    }

    public void setDataType(DataType dataType) {
        this.dataType = dataType;
    }

    public String getPayload() {
        return payload;
    }

    public void setPayload(String payload) {
        this.payload = payload;
    }
}
