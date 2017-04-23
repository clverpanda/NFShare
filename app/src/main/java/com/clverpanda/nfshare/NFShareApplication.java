package com.clverpanda.nfshare;

import android.app.Application;

import com.bilibili.boxing.BoxingCrop;
import com.bilibili.boxing.BoxingMediaLoader;
import com.bilibili.boxing.loader.IBoxingMediaLoader;
import com.clverpanda.nfshare.dao.DaoMaster;
import com.clverpanda.nfshare.dao.DaoSession;
import com.clverpanda.nfshare.util.ThumbnailLoader;

import org.greenrobot.greendao.database.Database;

/**
 * Created by clverpanda on 2017/4/16 0016.
 */

public class NFShareApplication extends Application
{
    private DaoSession daoSession;
    private static NFShareApplication _instance;

    public static NFShareApplication getInstance()
    {
        return _instance;
    }

    @Override
    public void onCreate()
    {
        super.onCreate();

        _instance = this;

        //BiliBili Boxing Initialize
        IBoxingMediaLoader loader = new ThumbnailLoader(this);
        BoxingMediaLoader.getInstance().init(loader);

        //greenDao Initialize
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(this, "NFShare-db");
        Database db = helper.getWritableDb();
        daoSession = new DaoMaster(db).newSession();
    }

    public DaoSession getDaoSession() { return daoSession; }
}
