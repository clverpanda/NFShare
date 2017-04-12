package com.clverpanda.nfshare.util;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.util.Log;

import com.clverpanda.nfshare.model.AppInfo;

import java.text.Collator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by clverpanda on 2017/3/30 0030.
 * It's the file for NFShare.
 */

public class AppInfoGetter
{
    private static AppInfoGetter infoGetter;
    private PackageManager pManager;


    private AppInfoGetter(Context context) {
        pManager = context.getPackageManager();
    }

    public static AppInfoGetter getInstance(Context context) {
        if (infoGetter == null) {
            infoGetter = new AppInfoGetter(context);
        }
        return infoGetter;
    }

    public List<AppInfo> getInstalledApps()
    {
        List<AppInfo> result = new ArrayList<>();
        List<PackageInfo> allPackageList = pManager.getInstalledPackages(0);
        if (allPackageList == null)
        {
            Log.e("AppInfoGetter类", "getInstalledApps()方法中的allPackageList为空");
            return null;
        }
        Collections.sort(allPackageList, new PackageInfoComparator(pManager));

        for (PackageInfo info : allPackageList) {
            if ((info.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) == 0)
            {
                AppInfo appInfo = new AppInfo();
                appInfo.setAppName(pManager.getApplicationLabel(info.applicationInfo).toString());
                appInfo.setAppIcon(pManager.getApplicationIcon(info.applicationInfo));
                appInfo.setPkgName(info.applicationInfo.packageName);
                appInfo.setAppVersion(info.versionName);
                result.add(appInfo);
            }
        }
        return result;
    }


        // 获取单个App图标
    public Drawable getAppIcon(String packageName) throws PackageManager.NameNotFoundException {
        return pManager.getApplicationIcon(packageName);
    }

    // 获取单个App名称
    public String getAppName(String packageName) throws PackageManager.NameNotFoundException {
        ApplicationInfo appInfo = pManager.getApplicationInfo(packageName, 0);
        return pManager.getApplicationLabel(appInfo).toString();
    }

    // 获取单个App版本号
    public String getAppVersion(String packageName)
            throws PackageManager.NameNotFoundException {
        PackageInfo packageInfo = pManager.getPackageInfo(packageName, 0);
        return packageInfo.versionName;
    }



    private static class PackageInfoComparator implements Comparator<PackageInfo>
    {
        PackageInfoComparator(PackageManager pm) {
            mPM = pm;
        }

        public final int compare(PackageInfo a, PackageInfo b) {
            CharSequence sa = mPM.getApplicationLabel(a.applicationInfo);
            CharSequence sb = mPM.getApplicationLabel(b.applicationInfo);
            return sCollator.compare(sa.toString(), sb.toString());
        }

        private final Collator sCollator = Collator.getInstance();
        private PackageManager mPM;
    }

}
