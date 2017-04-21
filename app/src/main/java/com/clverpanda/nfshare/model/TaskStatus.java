package com.clverpanda.nfshare.model;

/**
 * Created by clverpanda on 2017/4/21 0021.
 * It's the file for NFShare.
 */

public enum TaskStatus
{
    PAUSED(0), DONE(1), FAILED(-1), RUNNING(2), UNKNOWN(-100);

    final int index;
    TaskStatus(int index)
    {
        this.index = index;
    }

    public int getIndex() {
        return index;
    }
}
