package com.akatastroph.projectvelib.di;

import com.akatastroph.projectvelib.activity.MainActivity;
import com.akatastroph.projectvelib.activity.OnBoardingActivity;
import com.akatastroph.projectvelib.activity.SplashScreenActivity;
import com.akatastroph.projectvelib.activity.StationDetailActivity;
import com.akatastroph.projectvelib.activity.StationListActivity;
import com.akatastroph.projectvelib.di.module.AkatastrophModule;
import com.akatastroph.projectvelib.di.module.AppModule;
import com.akatastroph.projectvelib.di.module.NetModule;
import com.akatastroph.projectvelib.fragment.OnBoardingFragment;
import com.akatastroph.projectvelib.fragment.StationDetailsFragment;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by Genfinternet on 20/11/2016.
 */

@Singleton
@Component(modules={AppModule.class, NetModule.class, AkatastrophModule.class})
public interface AkatastrophComponent {
    void inject(SplashScreenActivity activity);
    void inject(MainActivity mainActivity);
    void inject(StationListActivity stationListActivity);
    void inject(StationDetailActivity stationDetailActivity);
    void inject(StationDetailsFragment stationDetailsFragment);
    void inject(OnBoardingActivity onBoardingActivity);
    void inject(OnBoardingFragment onBoardingFragment);
}
