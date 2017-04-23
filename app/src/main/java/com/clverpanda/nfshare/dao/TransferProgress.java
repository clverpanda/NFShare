package com.clverpanda.nfshare.dao;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.ToOne;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.DaoException;

/**
 * Created by clverpanda on 2017/4/21 0021.
 * It's the file for NFShare.
 */
@Entity
public class TransferProgress
{
    @Id(autoincrement = true)
    private Long Id;

    private Long transferredPosition;

    private Long endPosition;

    private Long taskId;
    @ToOne(joinProperty = "taskId")
    private Task task;

    public TransferProgress(Long transferredPosition, Long endPosition) {
        this.transferredPosition = transferredPosition;
        this.endPosition = endPosition;
    }

    /** Used to resolve relations */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;

    /** Used for active entity operations. */
    @Generated(hash = 1083568804)
    private transient TransferProgressDao myDao;

    @Generated(hash = 587985618)
    public TransferProgress(Long Id, Long transferredPosition, Long endPosition, Long taskId) {
        this.Id = Id;
        this.transferredPosition = transferredPosition;
        this.endPosition = endPosition;
        this.taskId = taskId;
    }

    @Generated(hash = 1487612377)
    public TransferProgress() {
    }

    public Long getId() {
        return this.Id;
    }

    public void setId(Long Id) {
        this.Id = Id;
    }

    public Long getTransferredPosition() {
        return this.transferredPosition;
    }

    public void setTransferredPosition(Long transferredPosition) {
        this.transferredPosition = transferredPosition;
    }

    public Long getEndPosition() {
        return this.endPosition;
    }

    public void setEndPosition(Long endPosition) {
        this.endPosition = endPosition;
    }

    @Generated(hash = 100676365)
    private transient Long task__resolvedKey;

    /** To-one relationship, resolved on first access. */
    @Generated(hash = 410192089)
    public Task getTask() {
        Long __key = this.taskId;
        if (task__resolvedKey == null || !task__resolvedKey.equals(__key)) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            TaskDao targetDao = daoSession.getTaskDao();
            Task taskNew = targetDao.load(__key);
            synchronized (this) {
                task = taskNew;
                task__resolvedKey = __key;
            }
        }
        return task;
    }

    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 711336367)
    public void setTask(Task task) {
        synchronized (this) {
            this.task = task;
            taskId = task == null ? null : task.getId();
            task__resolvedKey = taskId;
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

    public Long getTaskId() {
        return this.taskId;
    }

    public void setTaskId(Long taskId) {
        this.taskId = taskId;
    }

    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 892159749)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getTransferProgressDao() : null;
    }
}
