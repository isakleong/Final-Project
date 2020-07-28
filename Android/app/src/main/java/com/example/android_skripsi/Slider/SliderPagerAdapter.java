package com.example.android_skripsi.Slider;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.ArrayList;
import java.util.List;

public class SliderPagerAdapter extends FragmentStatePagerAdapter {

    private static final String TAG = "SliderPagerAdapter";

    List<Fragment> mFrags = new ArrayList<>();

    public SliderPagerAdapter(FragmentManager fm, List<Fragment> frags) {
        super(fm);
        mFrags = frags;
    }

//    @Override
//    public Fragment getItem(int position) {
//        int index = position % mFrags.size();
//        return FragmentSlider.newInstance(mFrags.get(index).getArguments().getString("params"));
//    }
    @Override
    public Fragment getItem(int position) {
        int index = position % mFrags.size();
        //return FragmentSlider.newInstance(mFrags.get(index).getArguments().getString("params"));
        return this.mFrags.get(index);
    }
    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

    @Override
    public int getCount() {
        return Integer.MAX_VALUE;
    }

}
