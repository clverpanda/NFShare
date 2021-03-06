package com.clverpanda.nfshare.fragments.taskslist;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by clverpanda on 2017/4/7 0007.
 * It's the file for NFShare.
 */

public class TasksPagerAdapter extends FragmentPagerAdapter
{
    private final int PAGE_COUNT = 2;
    private String tabTitles[] = new String[] {"进行中", "已完成"};
    private Fragment mCurrentFragment;
    private List<Fragment> mFragments;


    public TasksPagerAdapter(FragmentManager fm, List<Fragment> fragments)
    {
        super(fm);
        mFragments = fragments;
    }

    @Override
    public Fragment getItem(int position)
    {
        return mFragments.get(position);
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
