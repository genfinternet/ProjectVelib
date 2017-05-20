package com.akatastroph.projectvelib.activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.view.View;

import com.akatastroph.projectvelib.AkatastrophApplication;
import com.akatastroph.projectvelib.R;
import com.akatastroph.projectvelib.manager.DataManager;
import com.akatastroph.projectvelib.model.Station;
import com.akatastroph.projectvelib.utils.Tools;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;
import butterknife.OnFocusChange;

import static com.akatastroph.projectvelib.R.id.map;

/**
 * Created by genfinternet on 01/02/2017.
 */

public class MainActivity extends BaseActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, GoogleMap.OnCameraIdleListener, DataManager.DataStateListener, GoogleMap.OnMarkerClickListener, GoogleMap.OnCameraMoveListener {
    @Inject protected DataManager mDataManager;
    @BindView(map) protected MapView mMapView;
    @BindView(R.id.floatingActionButton) protected FloatingActionButton mFab;
    private GoogleApiClient mGoogleApiClient;
    private GoogleMap mGmap;
    private Circle mCircle;
    private ArrayList<Marker> mMarkers;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        mMarkers = new ArrayList<>();
        AkatastrophApplication.getInstance().getAkatastrophComponent().inject(this);
        setContentView(R.layout.activity_main);
        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }
        mMapView.onCreate(savedInstanceState);
        mMapView.onResume(); // needed to get the map to display immediately


        try {
            MapsInitializer.initialize(getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }
        mMapView.getMapAsync(this);
    }

    @OnFocusChange(R.id.search_station)
    public void OpenSearch(View v, boolean b) {
        if (b) {
            Intent intent = new Intent(MainActivity.this, StationListActivity.class);
            ActivityCompat.startActivity(MainActivity.this, intent, null);
            overridePendingTransition(R.anim.slide_in, R.anim.stay);
            v.clearFocus();
        }
    }

    @OnClick(R.id.floatingActionButton)
    public void GoToLocatIon() {
        ZoomToPosition();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mGmap = googleMap;
        ZoomToPosition();
        googleMap.setOnCameraIdleListener(this);
        googleMap.setOnCameraMoveListener(this);
        googleMap.setOnMarkerClickListener(this);
        googleMap.getUiSettings().setMyLocationButtonEnabled(false);
        mDataManager.getStationsAsync(this);
    }

    public void ZoomToPosition() {
        // For showing a move to my location button
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            mGmap.setMyLocationEnabled(true);
            LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            List<String> providers = locationManager.getProviders(true);
            Location location = null;
            for (String provider : providers) {
                Location l = locationManager.getLastKnownLocation(provider);
                if (l == null) {
                    continue;
                }
                if (location == null || l.getAccuracy() < location.getAccuracy()) {
                    location = l;
                }
            }
            if (location != null)
            {
                LatLng currentPosition = new LatLng(location.getLatitude(), location.getLongitude());
                CameraPosition cameraPosition = new CameraPosition.Builder()
                        .target(currentPosition)      // Sets the center of the map to location user
                        .zoom(16)                   // Sets the zoom
                        .bearing(0)                // Sets the orientation of the camera to north
                        .tilt(0)                   // Sets the tilt of the camera to 0 degrees
                        .build();                   // Creates a CameraPosition from the builder
                mGmap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                moveCircle(currentPosition);
            }
        } else {
            mFab.setVisibility(View.GONE);
        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onCameraIdle() {
        moveCircle(mGmap.getCameraPosition().target);
        updateMarker(mGmap.getCameraPosition().target);
    }


    @Override
    public void onCameraMove() {
        moveCircle(mGmap.getCameraPosition().target);
    }

    private void moveCircle(LatLng currentPosition) {
        if (mCircle != null) {
            mCircle.remove();
        }
        CircleOptions circleOptions = new CircleOptions()
                .center(currentPosition)
                .strokeColor(ResourcesCompat.getColor(getResources(), R.color.blue_bike, null))
                .radius(500);
        mCircle = mGmap.addCircle(circleOptions);
    }

    private void updateMarker(LatLng currentPosition) {
        for (Marker marker : mMarkers) {
            boolean showMarker = (calculationByDistance(marker.getPosition(), currentPosition) < 500);
            marker.setVisible(showMarker);
        }
    }

    public double calculationByDistance(LatLng StartP, LatLng EndP) {
        Location locationA = new Location("Start");
        locationA.setLatitude(StartP.latitude);
        locationA.setLongitude(StartP.longitude);
        Location locationB = new Location("End");
        locationB.setLatitude(EndP.latitude);
        locationB.setLongitude(EndP.longitude);
        return locationA.distanceTo(locationB);
    }

    @Override
    public void onDataReady(final ArrayList<Station> stations) {
        for (Station station : stations) {
            mMarkers.add(mGmap.addMarker(new MarkerOptions()
                    .position(station.getPosition())
                    .visible(true)
                    .icon(Tools.makeBitmap(MainActivity.this, "" + station.getAvailableBike()))//, R.drawable.ic_pin_drop))
                    .title(station.getName().substring(station.getName().indexOf("-") + 1).trim())));
        }
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        marker.showInfoWindow();
        return true;
    }
}
