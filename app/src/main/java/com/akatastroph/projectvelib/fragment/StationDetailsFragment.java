package com.akatastroph.projectvelib.fragment;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.akatastroph.projectvelib.AkatastrophApplication;
import com.akatastroph.projectvelib.R;
import com.akatastroph.projectvelib.manager.DataManager;
import com.akatastroph.projectvelib.model.Station;

import java.util.Locale;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

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

    @OnClick(R.id.show_map)
    public void showMap() {
        Toast.makeText(getContext(), "test show map", Toast.LENGTH_SHORT).show();
    }

    @OnClick(R.id.show_directions)
    public void showDirections() {
        String uri = String.format(Locale.ENGLISH, "http://maps.google.com/maps?daddr=%f,%f", mStation.getPosition().latitude, mStation.getPosition().longitude);
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
        intent.setPackage("com.google.android.apps.maps");
        try
        {
            startActivity(intent);
        }
        catch(ActivityNotFoundException ex)
        {
            try
            {
                Intent unrestrictedIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                startActivity(unrestrictedIntent);
            }
            catch(ActivityNotFoundException innerEx)
            {
                Toast.makeText(getContext(), "Please install a maps application", Toast.LENGTH_LONG).show();
            }
        }
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
