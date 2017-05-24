package com.akatastroph.projectvelib.model;

import android.support.annotation.DrawableRes;

/**
 * Created by genfinternet on 22/05/2017.
 */

public class Developer {
    private String mFullName;
    private String mLogin;
    private @DrawableRes int mIdPortrait;

    public Developer(String mFullName, String mLogin, int mIdPortrait) {
        this.mFullName = mFullName;
        this.mLogin = mLogin;
        this.mIdPortrait = mIdPortrait;
    }

    public String getFullName() {
        return mFullName;
    }

    public int getIdPortrait() {
        return mIdPortrait;
    }

    public String getLogin() {
        return mLogin;
    }
}
