package com.clverpanda.nfshare.util;


import com.clverpanda.nfshare.NFShareApplication;
import com.clverpanda.nfshare.dao.DaoSession;
import com.clverpanda.nfshare.dao.Device;
import com.clverpanda.nfshare.dao.DeviceDao;

/**
 * Created by miaol on 2017/5/30 0030.
 */

public class DbHelper
{
    private static DbHelper dbHelper;
    private DaoSession daoSession;

    private DbHelper()
    {
        daoSession = NFShareApplication.getInstance().getDaoSession();
    }

    public static DbHelper getInstance()
    {
        if (dbHelper == null)
            dbHelper = new DbHelper();
        return dbHelper;
    }

    public Long insertOrReplaceDevice(Device device2Add)
    {
        DeviceDao deviceDao = daoSession.getDeviceDao();
        Device deviceInDb = deviceDao.queryBuilder().where(DeviceDao.Properties.WiFiMac.eq(device2Add.getWiFiMac())).unique();
        if (deviceInDb == null)
            return deviceDao.insert(device2Add);
        else
            return deviceInDb.getId();
    }
}
