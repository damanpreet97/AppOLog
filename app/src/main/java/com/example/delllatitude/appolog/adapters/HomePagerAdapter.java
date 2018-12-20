package com.example.delllatitude.appolog.adapters;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.delllatitude.appolog.fragments.FavouriteFragment;
import com.example.delllatitude.appolog.fragments.RecentTopFragment;

public class HomePagerAdapter extends FragmentPagerAdapter {

    public HomePagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                return RecentTopFragment.newInstance("Recent");
            case 1:
                return RecentTopFragment.newInstance("Top");
            case 2:
                return new FavouriteFragment();
        }
        return null;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return "Recent";
            case 1:
                return "Top";
            case 2:
                return "Favourite";
        }
        return "";
    }

    @Override
    public int getCount() {
        return 3;
    }
}
