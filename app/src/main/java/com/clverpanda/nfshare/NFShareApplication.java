package com.clverpanda.nfshare;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.wifi.p2p.WifiP2pManager;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVInstallation;
import com.avos.avoscloud.AVOSCloud;
import com.avos.avoscloud.PushService;
import com.avos.avoscloud.SaveCallback;
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

        //TencenCloud Initialize
        AVOSCloud.initialize(this, "4pg1MkOjOpccIENtzRijjjd3-9Nh9j0Va", "Sp4SdCvYaXtzbafMufLC5IzT");
        //log开关
        AVOSCloud.setDebugLogEnabled(true);

        AVInstallation.getCurrentInstallation().saveInBackground(new SaveCallback() {
            public void done(AVException e) {
                if (e == null) {
                    // 保存成功
                    String installationId = AVInstallation.getCurrentInstallation().getInstallationId();
                    //将获取的installationId存入sharedpreferences
                    SharedPreferences sp = getSharedPreferences("leancloud", Context.MODE_PRIVATE);
                    sp.edit().putString("installationId", installationId).apply();
                } else {
                    // 保存失败，输出错误信息
                }
            }
        });
        PushService.setDefaultPushCallback(this, MainActivity.class);

        _instance = this;

        //BiliBili Boxing Initialize
        IBoxingMediaLoader loader = new ThumbnailLoader(this);
        BoxingMediaLoader.getInstance().init(loader);

        //greenDao Initialize
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(this, "NFShare-db");
        Database db = helper.getWritableDb();
        daoSession = new DaoMaster(db).newSession();
    }

    //终止时关闭p2p连接
    @Override
    public void onTerminate()
    {
        WifiP2pManager p2pManager = (WifiP2pManager) getSystemService(Context.WIFI_P2P_SERVICE);
        WifiP2pManager.Channel channel = p2pManager.initialize(this, getMainLooper(), null);
        p2pManager.removeGroup(channel, null);
        super.onTerminate();
    }

    public DaoSession getDaoSession() { return daoSession; }
}
