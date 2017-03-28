package com.clverpanda.nfshare.ResourceShare;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;


/**
 * Created by miaol on 2017/3/28 0028.
 */

public class ResourcePagerAdapter extends FragmentPagerAdapter
{
    final int PAGE_COUNT = 3;
    private String tabTitles[] = new String[] {"文档", "多媒体", "文件"};
    private Context context;

    public ResourcePagerAdapter(FragmentManager fm, Context context)
    {
        super(fm);
        this.context = context;
    }

    @Override
    public Fragment getItem(int position)
    {
        if (position == 0)
            return DocShareFrag.newInstance("", "");
        else if (position == 1)
            return MediaShareFrag.newInstance("", "");
        else
            return FileShareFrag.newInstance("", "");
    }

    @Override
    public int getCount()
    {
        return PAGE_COUNT;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return tabTitles[position];
    }
}
