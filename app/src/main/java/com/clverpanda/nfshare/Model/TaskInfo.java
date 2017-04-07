package com.clverpanda.nfshare.Model;

import java.sql.Time;
import java.util.Date;

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
    private int From;
    private int IsDone;
    private String ReceiveTime;

    public TaskInfo(int id, String name, String description, int type, int from, int isDone, String receiveTime) {
        Id = id;
        Name = name;
        Description = description;
        Type = type;
        From = from;
        IsDone = isDone;
        ReceiveTime = receiveTime;
    }

    public TaskInfo(String name, String description, int type, int from, int isDone) {
        Name = name;
        Description = description;
        Type = type;
        From = from;
        IsDone = isDone;
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

    public int getFrom() {
        return From;
    }

    public void setFrom(int from) {
        From = from;
    }

    public int getIsDone() {
        return IsDone;
    }

    public void setIsDone(int isDone) {
        IsDone = isDone;
    }

    public String getReceiveTime() {
        return ReceiveTime;
    }

    public void setReceiveTime(String receiveTime) {
        ReceiveTime = receiveTime;
    }
}
