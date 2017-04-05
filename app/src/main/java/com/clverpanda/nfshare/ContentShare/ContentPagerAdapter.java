package com.clverpanda.nfshare.ContentShare;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;

/**
 * Created by miaol on 2017/3/28 0028.
 */

public class ContentPagerAdapter extends FragmentPagerAdapter
{
    final int PAGE_COUNT = 2;
    private String tabTitles[] = new String[] {"应用", "联系人"};
    private Context context;
    private Fragment mCurrentFragment;

    public ContentPagerAdapter(FragmentManager fm, Context context)
    {
        super(fm);
        this.context = context;
    }

    @Override
    public Fragment getItem(int position)
    {
        if (position == 0)
            return AppShareFrag.newInstance(position);
        else
            return ContactShareFrag.newInstance("", "");
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

    @Override
    public void setPrimaryItem(ViewGroup container, int position, Object object) {
        mCurrentFragment = (Fragment) object;
        super.setPrimaryItem(container, position, object);
    }


    public Fragment getCurrentFragment()
    {
        return mCurrentFragment;
    }

}
