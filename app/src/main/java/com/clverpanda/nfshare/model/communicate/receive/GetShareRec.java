package com.clverpanda.nfshare.model.communicate.receive;

import com.clverpanda.nfshare.model.DataType;

/**
 * Created by clverpanda on 2017/5/27 0027.
 * It's the file for NFShare.
 */

public class GetShareRec
{
    private String status;
    private int id;
    private String origin_phone;
    private String token;
    private DataType data_type;
    private String related_data;
    private String ip;
    private int port;

    public GetShareRec() {
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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
}
