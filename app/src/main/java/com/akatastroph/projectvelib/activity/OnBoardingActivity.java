package com.akatastroph.projectvelib.activity;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;

import com.akatastroph.projectvelib.AkatastrophApplication;
import com.akatastroph.projectvelib.R;
import com.akatastroph.projectvelib.utils.UserPreferences;
import com.akatastroph.projectvelib.view.viewpager.adapter.OnBoardingAdapter;

import javax.inject.Inject;

import butterknife.BindView;

/**
 * Created by genfinternet on 13/05/2017.
 */

public class OnBoardingActivity extends BaseActivity {
    @BindView(R.id.pager) protected ViewPager mPager;
    @BindView(R.id.tab_layout) protected TabLayout mTabLayout;
    @Inject UserPreferences mUserPreferences;
    private boolean mFirstStart;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        AkatastrophApplication.getInstance().getAkatastrophComponent().inject(this);
        mFirstStart = true;
        setContentView(R.layout.activity_on_boarding);
    }

    @Override
    public void onStart() {
        super.onStart();
        if (mFirstStart) {
            mFirstStart = false;
            OnBoardingAdapter mPagerAdapter = new OnBoardingAdapter(getSupportFragmentManager());
            mPager.setAdapter(mPagerAdapter);
            mTabLayout.setupWithViewPager(mPager);
        }
    }

    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }
}
