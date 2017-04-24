package com.clverpanda.nfshare.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

/**
 * Created by clverpanda on 2017/4/12 0012.
 * It's the file for NFShare.
 */

public class DownloadFileInfo implements Parcelable
{
    private long id;
    private String url;
    private String fileName;
    private long length;
    private long finish;

    public DownloadFileInfo() {
    }

    public DownloadFileInfo(long id, String url, String fileName, long length, long finish) {
        this.id = id;
        this.url = url;
        this.fileName = fileName;
        this.length = length;
        this.finish = finish;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
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


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags)
    {
        dest.writeLong(id);
        dest.writeString(url);
        dest.writeString(fileName);
        dest.writeLong(length);
        dest.writeLong(finish);
    }

    public static final Parcelable.Creator<DownloadFileInfo> CREATOR = new Creator<DownloadFileInfo>()
    {
        @Override
        public DownloadFileInfo createFromParcel(Parcel source)
        {
            DownloadFileInfo fileInfo = new DownloadFileInfo();
            fileInfo.setId(source.readLong());
            fileInfo.setUrl(source.readString());
            fileInfo.setFileName(source.readString());
            fileInfo.setLength(source.readLong());
            fileInfo.setFinish(source.readLong());
            return fileInfo;
        }

        @Override
        public DownloadFileInfo[] newArray(int size)
        {
            return new DownloadFileInfo[size];
        }
    };
}
