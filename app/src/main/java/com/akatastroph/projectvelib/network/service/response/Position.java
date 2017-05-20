package com.akatastroph.projectvelib.network.service.response;

import com.google.gson.annotations.SerializedName;

/**
 * Created by genfinternet on 12/05/2017.
 */

public class Position {
    @SerializedName("lat")
    protected double mLatitude;
    @SerializedName("lng")
    protected double mLongitude;

    public double getLatitude() {
        return mLatitude;
    }

    public double getLongitude() {
        return mLongitude;
    }
}
