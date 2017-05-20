package com.akatastroph.projectvelib.activity;

import android.os.Bundle;
import android.support.v4.view.ViewPager;

import com.akatastroph.projectvelib.AkatastrophApplication;
import com.akatastroph.projectvelib.R;
import com.akatastroph.projectvelib.manager.DataManager;
import com.akatastroph.projectvelib.view.viewpager.adapter.StationAdapter;

import javax.inject.Inject;

import butterknife.BindView;

/**
 * Created by genfinternet on 13/05/2017.
 */

public class StationDetailActivity extends BaseActivity {
    public static final String ARG_STATION_INDEX = "ARG_STATION_INDEX";
    @Inject DataManager mDataManager;

    @BindView(R.id.pager) protected ViewPager mPager;
    private int mStartIndex;
    private boolean mFirstStart;
    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        AkatastrophApplication.getInstance().getAkatastrophComponent().inject(this);
        setContentView(R.layout.activity_station_detail);
        mFirstStart = true;
        mStartIndex = getIntent().getIntExtra(ARG_STATION_INDEX, 0);
    }

    @Override
    public void onStart() {
        super.onStart();
        if (mFirstStart) {
            mFirstStart = false;
            StationAdapter mPagerAdapter = new StationAdapter(getSupportFragmentManager(), mDataManager.getFilteredStations());
            mPager.setAdapter(mPagerAdapter);
            mPager.setCurrentItem(mStartIndex);
            mPager.setOffscreenPageLimit(2);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.stay, R.anim.slide_out);
    }
}
