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
import org.greenrobot.greendao.annotation.Transient;

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

    private Long deviceId;
    @ToOne(joinProperty = "deviceId")
    private Device originDevice;

    private Long progressId;
    @ToOne(joinProperty = "progressId")
    private TransferProgress transferProgress;

    @Transient
    private int progress;


    public Task(String name, String description, DataType type, TaskStatus status, Date receiveTime, Long deviceId) {
        Name = name;
        Description = description;
        Type = type;
        Status = status;
        this.receiveTime = receiveTime;
        this.deviceId = deviceId;
    }

    /** Used to resolve relations */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;

    /** Used for active entity operations. */
    @Generated(hash = 1469429066)
    private transient TaskDao myDao;

    @Generated(hash = 1252811447)
    public Task(Long Id, @NotNull String Name, @NotNull String Description, @NotNull DataType Type,
            @NotNull TaskStatus Status, @NotNull java.util.Date receiveTime, Long deviceId, Long progressId) {
        this.Id = Id;
        this.Name = Name;
        this.Description = Description;
        this.Type = Type;
        this.Status = Status;
        this.receiveTime = receiveTime;
        this.deviceId = deviceId;
        this.progressId = progressId;
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

    public int getProgress() {
        return progress;
    }

    public void setProgress(int progress) {
        this.progress = progress;
    }

    @Generated(hash = 836965034)
    private transient Long originDevice__resolvedKey;

    @Generated(hash = 538125673)
    private transient Long transferProgress__resolvedKey;

    /** To-one relationship, resolved on first access. */
    @Generated(hash = 1454397888)
    public Device getOriginDevice() {
        Long __key = this.deviceId;
        if (originDevice__resolvedKey == null || !originDevice__resolvedKey.equals(__key)) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            DeviceDao targetDao = daoSession.getDeviceDao();
            Device originDeviceNew = targetDao.load(__key);
            synchronized (this) {
                originDevice = originDeviceNew;
                originDevice__resolvedKey = __key;
            }
        }
        return originDevice;
    }

    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 448134710)
    public void setOriginDevice(Device originDevice) {
        synchronized (this) {
            this.originDevice = originDevice;
            deviceId = originDevice == null ? null : originDevice.getId();
            originDevice__resolvedKey = deviceId;
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

    /** To-one relationship, resolved on first access. */
    @Generated(hash = 294628259)
    public TransferProgress getTransferProgress() {
        Long __key = this.progressId;
        if (transferProgress__resolvedKey == null || !transferProgress__resolvedKey.equals(__key)) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            TransferProgressDao targetDao = daoSession.getTransferProgressDao();
            TransferProgress transferProgressNew = targetDao.load(__key);
            synchronized (this) {
                transferProgress = transferProgressNew;
                transferProgress__resolvedKey = __key;
            }
        }
        return transferProgress;
    }

    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 1547815997)
    public void setTransferProgress(TransferProgress transferProgress) {
        synchronized (this) {
            this.transferProgress = transferProgress;
            progressId = transferProgress == null ? null : transferProgress.getId();
            transferProgress__resolvedKey = progressId;
        }
    }

    public Long getDeviceId() {
        return this.deviceId;
    }

    public void setDeviceId(Long deviceId) {
        this.deviceId = deviceId;
    }

    public Long getProgressId() {
        return this.progressId;
    }

    public void setProgressId(Long progressId) {
        this.progressId = progressId;
    }

    public void setId(long Id) {
        this.Id = Id;
    }

    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 1442741304)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getTaskDao() : null;
    }

}
