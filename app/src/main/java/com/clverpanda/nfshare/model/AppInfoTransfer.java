package com.clverpanda.nfshare.model;


/**
 * Created by clverpanda on 2017/4/5 0005.
 * It's the file for NFShare.
 */

public class AppInfoTransfer
{
    private String appName;
    private String pkgName;
    private String appVersion;

    public AppInfoTransfer() {}

    public AppInfoTransfer(String appName, String pkgName, String appVersion) {
        this.appName = appName;
        this.pkgName = pkgName;
        this.appVersion = appVersion;
    }

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

    public String getAppVersion() {
        return appVersion;
    }

    public void setAppVersion(String appVersion) {
        this.appVersion = appVersion;
    }
}
