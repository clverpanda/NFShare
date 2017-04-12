package com.clverpanda.nfshare.model;

import android.graphics.drawable.Drawable;

/**
 * Created by clverpanda on 2017/3/30 0030.
 * It's the file for NFShare.
 */

public class AppInfo
{
    private String appName;
    private String pkgName;
    private Drawable appIcon;
    private String appVersion;

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getPkgName() {
        return pkgName;
    }

    public void setPkgName(String pkgName) {
        this.pkgName = pkgName;
    }

    public Drawable getAppIcon() {
        return appIcon;
    }

    public void setAppIcon(Drawable appIcon) {
        this.appIcon = appIcon;
    }

    public String getAppVersion() {
        return appVersion;
    }

    public void setAppVersion(String appVersion) {
        this.appVersion = appVersion;
    }
}
