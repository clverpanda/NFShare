package com.clverpanda.nfshare.tasks;

/**
 * Created by clverpanda on 2017/3/31 0031.
 * It's the file for NFShare.
 */

public interface AsyncResponse<T>
{
    void onDataReceivedSuccess(T listData);
    void onDataReceivedFailed();
}
