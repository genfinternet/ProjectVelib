package com.akatastroph.projectvelib.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.akatastroph.projectvelib.AkatastrophApplication;
import com.akatastroph.projectvelib.R;
import com.akatastroph.projectvelib.manager.DataManager;
import com.akatastroph.projectvelib.model.Station;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class StationDetailsFragment extends Fragment {
    private static final String STATION_ARG = "STATION_ARG";
    @Inject protected DataManager mDataManager;
    @BindView(R.id.name) protected TextView mNameView;
    @BindView(R.id.address) protected TextView mAddressView;
    @BindView(R.id.status) protected TextView mStatusView;
    @BindView(R.id.available_bikes) protected TextView mAvailableBikesView;
    @BindView(R.id.available_stands) protected TextView mAvailableStandsView;
    @BindView(R.id.last_update) protected TextView mLastUpdate;

    private Station mStation;

    public StationDetailsFragment() {}

    public static StationDetailsFragment newInstance(Station station) {
        StationDetailsFragment fragment = new StationDetailsFragment();
        Bundle args = new Bundle();
        args.putParcelable(STATION_ARG, station);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AkatastrophApplication.getInstance().getAkatastrophComponent().inject(this);
        if (getArguments() != null) {
            mStation = getArguments().getParcelable(STATION_ARG);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_station_details, container, false);
        ButterKnife.bind(this, rootView);
        mNameView.setText(mStation.getName().substring(mStation.getName().indexOf("-") + 1).trim());
        mAddressView.setText(mStation.getAddress());
        mStatusView.setText(mStation.isOpen() ? R.string.station_open : R.string.station_close);
        mLastUpdate.setText(getString(R.string.last_update, DateFormat.getDateFormat(getContext()).format(mStation.getLastUpdate()), DateFormat.getTimeFormat(getContext()).format(mStation.getLastUpdate())));
        mAvailableBikesView.setText(String.valueOf(mStation.getAvailableBike()));
        mAvailableStandsView.setText(String.valueOf(mStation.getAvailableBikeStands()));
        return rootView;
    }
}
