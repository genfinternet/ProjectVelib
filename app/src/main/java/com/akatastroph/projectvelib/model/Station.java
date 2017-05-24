package com.akatastroph.projectvelib.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.akatastroph.projectvelib.view.recyclerview.adapter.searchadapter.SortedListModel;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

/**
 * Created by Genfinternet on 20/11/2016.
 */

public class Station implements SortedListModel<Station>, Parcelable {

    public enum Status {
        OPEN,
        CLOSED
    }

    private int mNumber;
    private String mContractName;
    private String mName;
    private String mAddress;
    private LatLng mPosition;
    private boolean mBanking;
    private boolean mBonus;
    private Status mStatus;
    private int mBikeStands;
    private int mAvailableBikeStands;
    private int mAvailableBike;
    private Date mLastUpdate;

    public Station(com.akatastroph.projectvelib.network.service.response.Station station) {
        mNumber = station.getNumber();
        mName = station.getName();
        mContractName = station.getContractName();
        mAddress = station.getAddress();
        mPosition = new LatLng(station.getPosition().getLatitude(), station.getPosition().getLongitude());
        mBanking = station.isBanking();
        mBonus = station.isBonus();
        mStatus = station.getStatus().compareTo("OPEN") == 0 ? Status.OPEN : Status.CLOSED;
        mBikeStands = station.getBikeStands();
        mAvailableBikeStands = station.getAvailableBikeStands();
        mAvailableBike = station.getAvailableBike();
        mLastUpdate = new Date(station.getLastUpdate());
    }

    public Station(String name) {
        mName = name;
    }

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

    public LatLng getPosition() {
        return mPosition;
    }

    public boolean isBanking() {
        return mBanking;
    }

    public boolean isBonus() {
        return mBonus;
    }

    public boolean isOpen() {
        return mStatus == Status.OPEN;
    }

    public Status getStatus() {
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

    public Date getLastUpdate() {
        return mLastUpdate;
    }

    public static ArrayList<Station> fromResponse(List<com.akatastroph.projectvelib.network.service.response.Station> stations) {
        ArrayList<Station> res = new ArrayList<>();
        for (com.akatastroph.projectvelib.network.service.response.Station station : stations) {
            res.add(new Station(station));
        }
        return res;
    }

    @Override
    public boolean isTheSameItem(Station item) {
        return mNumber == item.getNumber();
    }

    public static class StationComparator implements Comparator<Station> {
        @Override
        public int compare(Station o1, Station o2) {
            return o1.getName().compareTo(o2.getName());
        }
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel out, int flags) {
        out.writeInt(mNumber);
        out.writeString(mContractName);
        out.writeString(mName);
        out.writeString(mAddress);
        out.writeParcelable(mPosition, flags);
        out.writeInt(mBanking ? 1 : 0);
        out.writeInt(mBonus ? 1 : 0);
        out.writeInt(mStatus == Status.OPEN ? 1 : 0);
        out.writeInt(mBikeStands);
        out.writeInt(mAvailableBikeStands);
        out.writeInt(mAvailableBike);
        out.writeLong(mLastUpdate != null ? mLastUpdate.getTime() : 0);
    }

    public static final Parcelable.Creator<Station> CREATOR
            = new Parcelable.Creator<Station>() {
        public Station createFromParcel(Parcel in) {
            return new Station(in);
        }

        public Station[] newArray(int size) {
            return new Station[size];
        }
    };

    private Station(Parcel in) {
        mNumber = in.readInt();
        mContractName = in.readString();
        mName = in.readString();
        mAddress = in.readString();
        mPosition = in.readParcelable(LatLng.class.getClassLoader());
        mBanking = (in.readInt() == 1);
        mBonus = (in.readInt() == 1);
        mStatus = (in.readInt() == 1 ? Status.OPEN : Status.CLOSED);
        mBikeStands = in.readInt();
        mAvailableBikeStands = in.readInt();
        mAvailableBike = in.readInt();
        mLastUpdate = new Date(in.readLong());
    }
}
