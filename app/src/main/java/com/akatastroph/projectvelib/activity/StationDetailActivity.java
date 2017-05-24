package com.akatastroph.projectvelib.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.akatastroph.projectvelib.AkatastrophApplication;
import com.akatastroph.projectvelib.R;
import com.akatastroph.projectvelib.manager.DataManager;
import com.akatastroph.projectvelib.model.Station;
import com.akatastroph.projectvelib.view.viewpager.adapter.StationAdapter;

import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.BindView;

/**
 * Created by genfinternet on 13/05/2017.
 */

public class StationDetailActivity extends BaseActivity {
    public static final String ARG_STATION_INDEX = "ARG_STATION_INDEX";
    @Inject DataManager mDataManager;
    @BindView(R.id.toolbar) protected Toolbar mToolbar;
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
        setSupportActionBar(mToolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.details_activity_menu, menu);
        return true;
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

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            case R.id.about_us_menu:
                Intent intent = new Intent(this, AboutUsActivity.class);
                ContextCompat.startActivity(this, intent, null);
                overridePendingTransition(R.anim.slide_in, R.anim.stay);
                return true;
            case R.id.share_menu:
                ArrayList<Station> stations = mDataManager.getFilteredStations();
                Station station = stations.get(mPager.getCurrentItem());
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, station.getName() + "\n" + station.getAddress());
                sendIntent.setType("text/plain");
                ContextCompat.startActivity(this, sendIntent, null);
                return true;
        }
        return super.onOptionsItemSelected(menuItem);
    }
}
