package com.akatastroph.projectvelib.manager;

import com.akatastroph.projectvelib.model.Station;
import com.akatastroph.projectvelib.network.ApiManager;
import com.akatastroph.projectvelib.utils.events.Event;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by genfinternet on 15/05/2017.
 */

public class DataManager {

    private enum State {
        Init,
        Loading,
        Ready,
        Error
    }

    private State mState;
    private Date mLastUpdate;

    private ArrayList<Station> mStations;
    private ArrayList<Station> mFilteredStations;
    private String mQuery;

    private ApiManager mApiManager;

    public DataManager(ApiManager apiManager) {
        mStations = new ArrayList<>();
        mFilteredStations = new ArrayList<>();
        mState= State.Init;
        mQuery = "";
        mApiManager = apiManager;
        updateStationList();
    }

    public boolean isReady() {
        return mState == State.Ready;
    }

    private void updateStationList() {
        if (mState == State.Loading) {
            return;
        }
        mState= State.Loading;
        mApiManager.stationList("Paris")
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Observer<List<com.akatastroph.projectvelib.network.service.response.Station>>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        mState = State.Error;
                    }

                    @Override
                    public void onNext(List<com.akatastroph.projectvelib.network.service.response.Station> stations) {
                        mStations = Station.fromResponse(stations);
                        Collections.sort(mStations, new Station.StationComparator());
                        filter();
                        mState = State.Ready;
                        mLastUpdate = Calendar.getInstance().getTime();
                        notifyListener();
                    }
                });
    }

    private void notifyListener() {
        EventBus.getDefault().post(new Event.StationListUpdatedEvent(mStations));
        EventBus.getDefault().post(new Event.FilteredStationListUpdatedEvent(mFilteredStations));
    }

    public ArrayList<Station> getStations()  {
        return mStations;
    }


    public void filterAsync(String query) {
        mQuery = query.toLowerCase();
        if (mState == State.Ready) {
            filter();
            EventBus.getDefault().post(new Event.FilteredStationListUpdatedEvent(mFilteredStations));
        }
    }

    public ArrayList<Station> getFilteredStations() {
        return mFilteredStations;
    }

    private void filter() {
        final ArrayList<Station> filteredModelList = new ArrayList<>();
        for (Station model : mStations) {
            final String text = model.getAddress().toLowerCase();
            if (text.contains(mQuery)) {
                filteredModelList.add(model);
            }
        }
        mFilteredStations = filteredModelList;
    }

    public Date getLastUpdate() {
        return mLastUpdate;
    }

    public void refresh() {
        updateStationList();
    }
}
