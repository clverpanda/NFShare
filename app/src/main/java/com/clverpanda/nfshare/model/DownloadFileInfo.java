package com.clverpanda.nfshare.model;

import java.io.Serializable;

/**
 * Created by clverpanda on 2017/4/12 0012.
 * It's the file for NFShare.
 */

public class DownloadFileInfo implements Serializable
{
    private  int id;
    private String url;
    private String fileName;
    private long length;
    private long finish;

    public DownloadFileInfo() {
    }

    public DownloadFileInfo(int id, String url, String fileName, long length, long finish) {
        this.id = id;
        this.url = url;
        this.fileName = fileName;
        this.length = length;
        this.finish = finish;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public long getLength() {
        return length;
    }

    public void setLength(long length) {
        this.length = length;
    }

    public long getFinish() {
        return finish;
    }

    public void setFinish(long finish) {
        this.finish = finish;
    }
}
