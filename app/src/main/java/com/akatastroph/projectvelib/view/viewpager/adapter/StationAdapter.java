package com.akatastroph.projectvelib.view.viewpager.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;

import com.akatastroph.projectvelib.fragment.StationDetailsFragment;
import com.akatastroph.projectvelib.model.Station;

import java.util.List;

/**
 * Created by genfinternet on 13/05/2017.
 */

public class StationAdapter extends FragmentStatePagerAdapter {
    private List<Station> mStations;

    public StationAdapter(FragmentManager fm, List<Station> stations) {
        super(fm);
        mStations = stations;
    }

    @Override
    public int getCount() {
        return mStations.size();
    }

    @Override
    public int getItemPosition(Object object){
        return PagerAdapter.POSITION_NONE;
    }

    @Override
    public Fragment getItem(int position) {
        return StationDetailsFragment.newInstance(mStations.get(position));
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mStations.get(position).getName();
    }

    public List<Station> getStations() {
        return mStations;
    }

    public void setStations(List<Station> stations) {
        this.mStations = stations;
        notifyDataSetChanged();
    }
}
