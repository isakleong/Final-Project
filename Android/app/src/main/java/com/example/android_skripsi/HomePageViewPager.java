package com.example.android_skripsi;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android_skripsi.TaskServer.TaskServer;

public class HomePageViewPager extends FragmentPagerAdapter
{
    final int PAGE_COUNT = 5;
    private  String tabTitles[] = new String[]{"Home","History","Trending","Biro / Unit","Profile"};
    private Context context;
    HomePageFragment frHP;
    HistoryFragment frH;
    ShareFragment frS;

    FragmentTransaction trans;
    FragmentManager fm;
    HomePageFragment frDJ              = new HomePageFragment();

    public HomePageViewPager(FragmentManager fm, Context context) {
        super(fm);
        this.context = context;
    }

    @Override
    public Fragment getItem(int position) {
        TextView navigationToolbar = MainActivity.navigationToolbar;
        if(position == 0){
            return new HomePageFragment();
        }
        else if(position == 1){
            return new HistoryFragment();
        }
        else if(position == 2){
            return new TrendingFragment();
        }
        else if(position == 3){
            return new BiroUnitFragment();
        }
        else if(position == 4){
            return new ProfileFragment();
        }
        else
        {
            return  null;
        }
    }
    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

    @Override
    public int getCount() {
        return PAGE_COUNT;
    }

    @Override
    public CharSequence getPageTitle(int position) { return tabTitles[position]; }
}
