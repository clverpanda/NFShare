package com.clverpanda.nfshare.model;

/**
 * Created by clverpanda on 2017/4/7 0007.
 * It's the file for NFShare.
 */

public class TaskInfo
{
    private int Id;
    private String Name;
    private String Description;
    private int Type;
    private String From;
    private int Status;
    private String ReceiveTime;
    private long finish;

    public TaskInfo() {}

    public TaskInfo(String name, String description, int type, String from, int status) {
        Name = name;
        Description = description;
        Type = type;
        From = from;
        Status = status;
    }

    public TaskInfo(int id, String name, String description, int type, String from, int status, String receiveTime) {
        Id = id;
        Name = name;
        Description = description;
        Type = type;
        From = from;
        Status = status;
        ReceiveTime = receiveTime;
    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public int getType() {
        return Type;
    }

    public void setType(int type) {
        Type = type;
    }

    public String getFrom() {
        return From;
    }

    public void setFrom(String from) {
        From = from;
    }

    public int getStatus() {
        return Status;
    }

    public void setStatus(int status) {
        Status = status;
    }

    public String getReceiveTime() {
        return ReceiveTime;
    }

    public void setReceiveTime(String receiveTime) {
        ReceiveTime = receiveTime;
    }

    public long getFinish() {
        return finish;
    }

    public void setFinish(long finish) {
        this.finish = finish;
    }
}
