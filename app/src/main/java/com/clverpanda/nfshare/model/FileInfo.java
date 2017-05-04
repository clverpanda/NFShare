package com.clverpanda.nfshare.model;

/**
 * Created by clverpanda on 2017/4/18 0018.
 * It's the file for NFShare.
 */

public class FileInfo
{
    private String fileName;
    private String filePath;
    private long space;
    private String hashStr;
    private String downloadUrl;

    public FileInfo() {
    }

    public FileInfo(String fileName, String filePath, long space) {
        this.fileName = fileName;
        this.filePath = filePath;
        this.space = space;
    }

    public FileInfo(String fileName, String filePath, long space, String hashStr) {
        this.fileName = fileName;
        this.filePath = filePath;
        this.space = space;
        this.hashStr = hashStr;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public long getSpace() {
        return space;
    }

    public void setSpace(long space) {
        this.space = space;
    }

    public String getHashStr() {
        return hashStr;
    }

    public void setHashStr(String hashStr) {
        this.hashStr = hashStr;
    }

    public String getDownloadUrl() {
        return downloadUrl;
    }

    public void setDownloadUrl(String downloadUrl) {
        this.downloadUrl = downloadUrl;
    }
}
