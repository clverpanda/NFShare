package com.clverpanda.nfshare;

import android.app.Application;

import com.bilibili.boxing.BoxingCrop;
import com.bilibili.boxing.BoxingMediaLoader;
import com.bilibili.boxing.loader.IBoxingMediaLoader;
import com.clverpanda.nfshare.util.ThumbnailLoader;

/**
 * Created by miaol on 2017/4/16 0016.
 */

public class NFShareApplication extends Application
{
    @Override
    public void onCreate()
    {
        super.onCreate();

        IBoxingMediaLoader loader = new ThumbnailLoader(this);
        BoxingMediaLoader.getInstance().init(loader);
    }
}
