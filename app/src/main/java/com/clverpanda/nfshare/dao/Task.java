package com.clverpanda.nfshare.dao;

import com.clverpanda.nfshare.dao.converter.DataTypeConverter;
import com.clverpanda.nfshare.dao.converter.TaskStatusConverter;
import com.clverpanda.nfshare.model.DataType;
import com.clverpanda.nfshare.model.TaskStatus;

import org.greenrobot.greendao.annotation.Convert;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.NotNull;
import org.greenrobot.greendao.annotation.ToOne;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.DaoException;

import java.util.Date;

/**
 * Created by clverpanda on 2017/4/21 0021.
 * It's the file for NFShare.
 */

@Entity
public class Task
{
    @Id(autoincrement = true)
    private Long Id;

    @NotNull
    private String Name;

    @NotNull
    private String Description;

    @NotNull
    @Convert(converter = DataTypeConverter.class, columnType = Integer.class)
    private DataType Type;

    @NotNull
    @Convert(converter = TaskStatusConverter.class, columnType = Integer.class)
    private TaskStatus Status;

    @NotNull
    private java.util.Date receiveTime;

    @ToOne
    private Device originDevice;

    public Task(String name, String description, DataType type, TaskStatus status, Date receiveTime, Device originDevice)
    {
        Name = name;
        Description = description;
        Type = type;
        Status = status;
        this.receiveTime = receiveTime;
        this.originDevice = originDevice;
    }

    /** Used to resolve relations */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;

    /** Used for active entity operations. */
    @Generated(hash = 1469429066)
    private transient TaskDao myDao;

    @Generated(hash = 733564585)
    public Task(Long Id, @NotNull String Name, @NotNull String Description,
            @NotNull DataType Type, @NotNull TaskStatus Status,
            @NotNull java.util.Date receiveTime) {
        this.Id = Id;
        this.Name = Name;
        this.Description = Description;
        this.Type = Type;
        this.Status = Status;
        this.receiveTime = receiveTime;
    }

    @Generated(hash = 733837707)
    public Task() {
    }

    public Long getId() {
        return this.Id;
    }

    public void setId(Long Id) {
        this.Id = Id;
    }

    public String getName() {
        return this.Name;
    }

    public void setName(String Name) {
        this.Name = Name;
    }

    public String getDescription() {
        return this.Description;
    }

    public void setDescription(String Description) {
        this.Description = Description;
    }

    public DataType getType() {
        return this.Type;
    }

    public void setType(DataType Type) {
        this.Type = Type;
    }

    public TaskStatus getStatus() {
        return this.Status;
    }

    public void setStatus(TaskStatus Status) {
        this.Status = Status;
    }

    public java.util.Date getReceiveTime() {
        return this.receiveTime;
    }

    public void setReceiveTime(java.util.Date receiveTime) {
        this.receiveTime = receiveTime;
    }

    @Generated(hash = 1453090739)
    private transient boolean originDevice__refreshed;

    /** To-one relationship, resolved on first access. */
    @Generated(hash = 1598599123)
    public Device getOriginDevice() {
        if (originDevice != null || !originDevice__refreshed) {
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            DeviceDao targetDao = daoSession.getDeviceDao();
            targetDao.refresh(originDevice);
            originDevice__refreshed = true;
        }
        return originDevice;
    }

    /** To-one relationship, returned entity is not refreshed and may carry only the PK property. */
    @Generated(hash = 917089999)
    public Device peakOriginDevice() {
        return originDevice;
    }

    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 356732557)
    public void setOriginDevice(Device originDevice) {
        synchronized (this) {
            this.originDevice = originDevice;
            originDevice__refreshed = true;
        }
    }

    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#delete(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 128553479)
    public void delete() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.delete(this);
    }

    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#refresh(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 1942392019)
    public void refresh() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.refresh(this);
    }

    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#update(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 713229351)
    public void update() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.update(this);
    }

    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 1442741304)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getTaskDao() : null;
    }

}
