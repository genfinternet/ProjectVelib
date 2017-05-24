package com.akatastroph.projectvelib.view.viewpager.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.akatastroph.projectvelib.fragment.OnBoardingFragment;

/**
 * Created by genfinternet on 13/05/2017.
 * Project Velib
 */

public class OnBoardingAdapter extends FragmentStatePagerAdapter {

    public OnBoardingAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return OnBoardingFragment.newInstance(OnBoardingFragment.PAGE_ONE);
            case 1:
                return OnBoardingFragment.newInstance(OnBoardingFragment.PAGE_TWO);
            case 2:
            default:
                return OnBoardingFragment.newInstance(OnBoardingFragment.PAGE_THREE);
        }
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return "";
    }
}
