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

    @ToOne
    private Task task;

    /** Used to resolve relations */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;

    /** Used for active entity operations. */
    @Generated(hash = 1083568804)
    private transient TransferProgressDao myDao;

    @Generated(hash = 900254049)
    public TransferProgress(Long Id, Long transferredPosition, Long endPosition) {
        this.Id = Id;
        this.transferredPosition = transferredPosition;
        this.endPosition = endPosition;
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

    @Generated(hash = 524156212)
    private transient boolean task__refreshed;

    /** To-one relationship, resolved on first access. */
    @Generated(hash = 1288792359)
    public Task getTask() {
        if (task != null || !task__refreshed) {
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            TaskDao targetDao = daoSession.getTaskDao();
            targetDao.refresh(task);
            task__refreshed = true;
        }
        return task;
    }

    /** To-one relationship, returned entity is not refreshed and may carry only the PK property. */
    @Generated(hash = 1173059015)
    public Task peakTask() {
        return task;
    }

    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 780247324)
    public void setTask(Task task) {
        synchronized (this) {
            this.task = task;
            task__refreshed = true;
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
    @Generated(hash = 892159749)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getTransferProgressDao() : null;
    }
}
