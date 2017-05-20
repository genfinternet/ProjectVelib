package com.akatastroph.projectvelib;

import android.app.Application;
import android.content.pm.PackageManager;
import android.util.Log;

import com.akatastroph.projectvelib.di.AkatastrophComponent;
import com.akatastroph.projectvelib.di.DaggerAkatastrophComponent;
import com.akatastroph.projectvelib.di.module.AkatastrophModule;
import com.akatastroph.projectvelib.di.module.AppModule;
import com.akatastroph.projectvelib.di.module.NetModule;
import com.facebook.stetho.Stetho;

/**
 * Created by Genfinternet on 20/11/2016.
 */

public class AkatastrophApplication extends Application {
    public static final int PERMISSIONS_REQUEST_LOCATION = 1337;

    private AkatastrophComponent mAkatastrophComponent;
    private static AkatastrophApplication sInstance;
    private static final String mApiKey ="f7fca902b1fdad3e17d102b5b47c4b3f9e046c44";

    @Override
    public void onCreate() {
        super.onCreate();
        sInstance = this;
        mAkatastrophComponent = DaggerAkatastrophComponent.builder()
                .appModule(new AppModule(this))
                .netModule(new NetModule("https://api.jcdecaux.com"))
                .akatastrophModule(new AkatastrophModule(mApiKey))
                .build();
        if (BuildConfig.DEBUG) {
            Stetho.initializeWithDefaults(this);
            try {
                Log.i("TEST", "" + getPackageManager().getPackageInfo("com.google.android.gms", 0 ).versionCode);
            } catch (PackageManager.NameNotFoundException e) {
                Log.i("TEST", "Not found");
            }
        }
    }

    public AkatastrophComponent getAkatastrophComponent() {
        return mAkatastrophComponent;
    }

    public static AkatastrophApplication getInstance() {
        return sInstance;
    }
}
