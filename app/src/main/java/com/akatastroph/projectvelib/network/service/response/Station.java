package com.akatastroph.projectvelib.network.service.response;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Genfinternet on 20/11/2016.
 */

public class Station {
    @SerializedName("number")
    protected int mNumber;

    @SerializedName("contract_name")
    protected String mContractName;

    @SerializedName("name")
    protected String mName;

    @SerializedName("address")
    protected String mAddress;

    @SerializedName("position")
    protected Position mPosition;

    @SerializedName("banking")
    protected boolean mBanking;

    @SerializedName("bonus")
    protected boolean mBonus;

    @SerializedName("status")
    protected String mStatus;

    @SerializedName("bike_stands")
    protected int mBikeStands;

    @SerializedName("available_bike_stands")
    protected int mAvailableBikeStands;

    @SerializedName("available_bikes")
    protected int mAvailableBike;

    @SerializedName("last_update")
    protected Long mLastUpdate;

    public int getNumber() {
        return mNumber;
    }

    public String getContractName() {
        return mContractName;
    }

    public String getName() {
        return mName;
    }

    public String getAddress() {
        return mAddress;
    }

    public Position getPosition() {
        return mPosition;
    }

    public boolean isBanking() {
        return mBanking;
    }

    public boolean isBonus() {
        return mBonus;
    }

    public String getStatus() {
        return mStatus;
    }

    public int getBikeStands() {
        return mBikeStands;
    }

    public int getAvailableBikeStands() {
        return mAvailableBikeStands;
    }

    public int getAvailableBike() {
        return mAvailableBike;
    }

    public Long getLastUpdate() {
        return mLastUpdate;
    }
}
