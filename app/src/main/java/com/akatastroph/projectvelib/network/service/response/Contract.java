package com.akatastroph.projectvelib.network.service.response;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by genfinternet on 12/05/2017.
 */

public class Contract {
    @SerializedName("name")
    protected String mName;

    @SerializedName("commercial_name")
    protected String mCommercialName;

    @SerializedName("country_code")
    protected String mCountryCode;

    @SerializedName("cities")
    protected ArrayList<String> mCities;

    public String getName() {
        return mName;
    }

    public String getCommercialName() {
        return mCommercialName;
    }

    public String getCountryCode() {
        return mCountryCode;
    }

    public ArrayList<String> getCities() {
        return mCities;
    }
}
