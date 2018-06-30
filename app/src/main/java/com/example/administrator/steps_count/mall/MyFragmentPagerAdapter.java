package com.example.administrator.steps_count.mall;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

/**
 * Created by 59476 on 2017/12/12.
 */

public class MyFragmentPagerAdapter extends FragmentPagerAdapter {
    private List<Fragment> fragList;
    public MyFragmentPagerAdapter(FragmentManager fm, List<Fragment> fragList) {
        super(fm);
        this.fragList=fragList;

    }

    @Override
    public Fragment getItem(int arg0) {
        return fragList.get(arg0);
    }

    @Override
    public int getCount() {
        return fragList.size();
    }

}
