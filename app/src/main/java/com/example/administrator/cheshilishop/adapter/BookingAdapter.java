package com.example.administrator.cheshilishop.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * 预约管理适配器
 * Created by Administrator on 2017/8/2.
 */
public class BookingAdapter extends FragmentPagerAdapter {
    List<Fragment> fragmentList = new ArrayList<Fragment>();
    FragmentManager manager;

    public BookingAdapter(FragmentManager fm, List<Fragment> fragmentList) {
        super(fm);
        this.fragmentList = fragmentList;
        manager = fm;
    }


    @Override
    public Fragment getItem(int position) {
        return fragmentList.get(position);
    }

    @Override
    public int getCount() {
        return fragmentList.size();
    }

    @Override
    public Fragment instantiateItem(ViewGroup container, int position) {
        Fragment fragment = (Fragment) super.instantiateItem(container,
                position);
        manager.beginTransaction().show(fragment).commit();
        return fragment;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        Fragment fragment = fragmentList.get(position);
        manager.beginTransaction().hide(fragment).commit();
    }

}


