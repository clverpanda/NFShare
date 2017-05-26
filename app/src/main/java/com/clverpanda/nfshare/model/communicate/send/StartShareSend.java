package com.clverpanda.nfshare.model.communicate.send;

import android.content.Context;

import com.clverpanda.nfshare.model.DataType;
import com.clverpanda.nfshare.util.DeviceInfoGetter;
import com.clverpanda.nfshare.util.TokenConstructor;

/**
 * Created by clverpanda on 2017/5/23 0023.
 * It's the file for NFShare.
 */

public class StartShareSend
{
    private String origin_phone;
    private String token;
    private DataType data_type;
    private String related_data;
    private String ip;
    private int port;
    private String installation;

    public StartShareSend(DataType data_type, String related_data,
                          int port, Context context)
    {
        this.data_type = data_type;
        this.related_data = related_data;
        this.ip = DeviceInfoGetter.getInstance(context).getWifiIpAddress();
        this.port = port;
        this.token = TokenConstructor.getToken();
        this.origin_phone = DeviceInfoGetter.getInstance(context).getMacAddr();
        this.installation = DeviceInfoGetter.getInstance(context).getTencentInstallationId();
    }

    public String getOrigin_phone() {
        return origin_phone;
    }

    public void setOrigin_phone(String origin_phone) {
        this.origin_phone = origin_phone;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public DataType getData_type() {
        return data_type;
    }

    public void setData_type(DataType data_type) {
        this.data_type = data_type;
    }

    public String getRelated_data() {
        return related_data;
    }

    public void setRelated_data(String related_data) {
        this.related_data = related_data;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getInstallation() {
        return installation;
    }

    public void setInstallation(String installation) {
        this.installation = installation;
    }
}
