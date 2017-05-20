package com.akatastroph.projectvelib.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Genfinternet on 20/11/2016.
 */

public class UserPreferences {
    private static final String SETTINGS = "SETTINGS";
    private static final String FIRST_LAUNCH = "FIRST_LAUNCH";
    Context mContext;

    public UserPreferences(Context context) {
        mContext = context;
    }

    public boolean isFirstStart() {
        SharedPreferences settings = mContext.getSharedPreferences(SETTINGS, Context.MODE_PRIVATE);
        return settings.getBoolean(FIRST_LAUNCH, true);
    }

    public void completeFirstStart() {
        SharedPreferences settings = mContext.getSharedPreferences(SETTINGS, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putBoolean(FIRST_LAUNCH, false);
        editor.apply();
    }
}
