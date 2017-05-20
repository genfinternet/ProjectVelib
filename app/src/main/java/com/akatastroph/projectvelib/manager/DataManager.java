package com.akatastroph.projectvelib.manager;

import com.akatastroph.projectvelib.AkatastrophApplication;
import com.akatastroph.projectvelib.model.Station;
import com.akatastroph.projectvelib.network.ApiManager;

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
    public interface DataStateListener {
        void onDataReady(ArrayList<Station> stations);
    }

    public interface FilteredDataStateListener {
        void onDataFilteredAsync(ArrayList<Station> stationsFiltered);
    }

    private enum State {
        Loading,
        Ready,
        Error
    }

    private String mQuery;
    private ArrayList<Station> mStations;
    private ArrayList<Station> mFilteredStations;

    private State mState;
    private ArrayList<DataStateListener> mDataStateListeners;
    private ArrayList<FilteredDataStateListener> mFilteredDataStateListeners;
    private Date mLastUpdate;

    public DataManager(ApiManager apiManager) {
        mState= State.Loading;
        mDataStateListeners = new ArrayList<>();
        mFilteredDataStateListeners = new ArrayList<>();

        mStations = new ArrayList<>();
        mFilteredStations = new ArrayList<>();
        mQuery = "";
        AkatastrophApplication.getInstance().getAkatastrophComponent().inject(this);
        apiManager.stationList("Paris")
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Observer<List<com.akatastroph.projectvelib.network.service.response.Station>>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        //TODO Do something
                        mState = State.Error;
                    }

                    @Override
                    public void onNext(List<com.akatastroph.projectvelib.network.service.response.Station> stations) {
                        mStations = Station.fromResponse(stations);
                        filter();
                        Collections.sort(mStations, new Station.StationComparator());
                        mState = State.Ready;
                        notifyListener();
                        mLastUpdate = Calendar.getInstance().getTime();
                    }
                });
    }

    private void notifyListener() {
        for (DataStateListener listener : mDataStateListeners) {
            listener.onDataReady(mStations);
        }
        mDataStateListeners.clear();
        for (FilteredDataStateListener listener : mFilteredDataStateListeners) {
            listener.onDataFilteredAsync(mFilteredStations);
        }
        mFilteredDataStateListeners.clear();
    }

    public void getStationsAsync(final DataStateListener listener) {
        if (mState == State.Ready) {
            Runnable task = new Runnable() {
                @Override
                public void run() {
                    listener.onDataReady(getStations());
                }
            };
            task.run();
        } else {
            mDataStateListeners.add(listener);
        }
    }

    public ArrayList<Station> getStations()  {
        return mStations;
    }


    public void filterAsync(FilteredDataStateListener listener, String query) {
        mQuery = query.toLowerCase();
        if (mState == State.Ready) {
            filter();
            listener.onDataFilteredAsync(mFilteredStations);
        } else {
            mFilteredDataStateListeners.add(listener);
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
}
