package com.akatastroph.projectvelib.activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import com.akatastroph.projectvelib.AkatastrophApplication;
import com.akatastroph.projectvelib.R;
import com.akatastroph.projectvelib.manager.DataManager;
import com.akatastroph.projectvelib.model.Station;
import com.akatastroph.projectvelib.utils.Tools;
import com.akatastroph.projectvelib.utils.events.Event;
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

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;
import butterknife.OnFocusChange;

import static com.akatastroph.projectvelib.R.id.map;

public class MainActivity extends BaseActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, GoogleMap.OnCameraIdleListener, GoogleMap.OnMarkerClickListener, GoogleMap.OnCameraMoveListener {
    @Inject protected DataManager mDataManager;

    @BindView(map) protected MapView mMapView;
    @BindView(R.id.floatingActionButton) protected FloatingActionButton mFab;
    @BindView(R.id.toolbar) protected Toolbar mToolbar;

    @BindView(R.id.mode_bike_textview) protected TextView mBikeTextView;
    @BindView(R.id.mode_stand_textview) protected TextView mStandTextView;
    @BindView(R.id.mode_bike_layout) protected CardView mBikeLayout;
    @BindView(R.id.mode_stand_layout) protected CardView mStandLayout;
    private boolean mBikeMode;

    private GoogleApiClient mGoogleApiClient;
    private GoogleMap mGmap;
    private Circle mCircle;
    private ArrayList<Marker> mMarkersBikes;
    private ArrayList<Marker> mMarkersStands;

    private Animation mRotateAnimation;
    private MenuItem mMenuItem;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mMarkersBikes = new ArrayList<>();
        mMarkersStands = new ArrayList<>();
        AkatastrophApplication.getInstance().getAkatastrophComponent().inject(this);
        setContentView(R.layout.activity_main);
        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }
        initMap(savedInstanceState);
        initToolbar();
        initUI();
    }

    private void initMap(Bundle savedInstanceState) {
        mMapView.onCreate(savedInstanceState);
        mMapView.onResume(); // needed to get the map to display immediately
        try {
            MapsInitializer.initialize(getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }
        mMapView.getMapAsync(this);
    }

    private void initToolbar() {
        setSupportActionBar(mToolbar);
    }

    private void initUI() {
        mBikeMode = true;
        updateCardColor();
        mRotateAnimation = AnimationUtils.loadAnimation(this, R.anim.rotate);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.refresh_menu, menu);
        return true;
    }

    @OnClick(R.id.mode_bike_layout)
    public void OnBikeModeClick() {
        if (mBikeMode) {
            return;
        }
        mBikeMode = true;
        updateCardColor();
        for (Marker marker : mMarkersStands) {
            marker.setVisible(false);
        }
        onCameraIdle();
    }

    @OnClick(R.id.mode_stand_layout)
    public void OnStandModeClick() {
        if (!mBikeMode) {
            return;
        }
        mBikeMode = false;
        updateCardColor();
        for (Marker marker : mMarkersBikes) {
            marker.setVisible(false);
        }
        onCameraIdle();
    }

    private void updateCardColor() {
        int white = ResourcesCompat.getColor(getResources(), R.color.white, null);
        if (mBikeMode) {
            int blue = ResourcesCompat.getColor(getResources(), R.color.blue_bike, null);
            mBikeLayout.setCardBackgroundColor(blue);
            mStandLayout.setCardBackgroundColor(white);
            mBikeTextView.setTextColor(white);
            mStandTextView.setTextColor(blue);
            setTextViewDrawableColor(mBikeTextView, R.drawable.ic_bike_colored, white);
            setTextViewDrawableColor(mStandTextView, R.drawable.ic_stands_colored, blue);
            mFab.setColorFilter(blue, PorterDuff.Mode.SRC_ATOP);
        } else {
            int purple = ResourcesCompat.getColor(getResources(), R.color.purple_parking, null);
            mBikeLayout.setCardBackgroundColor(white);
            mStandLayout.setCardBackgroundColor(purple);
            mBikeTextView.setTextColor(purple);
            mStandTextView.setTextColor(white);
            setTextViewDrawableColor(mBikeTextView, R.drawable.ic_bike_colored, purple);
            setTextViewDrawableColor(mStandTextView, R.drawable.ic_stands_colored, white);
            mFab.setColorFilter(purple, PorterDuff.Mode.SRC_ATOP);
        }
    }

    private void setTextViewDrawableColor(TextView textView, @DrawableRes int idDrawable, @ColorInt int color) {
        Drawable drawable = ResourcesCompat.getDrawable(getResources(), idDrawable, null);
        if (drawable != null) {
            drawable.mutate();
            drawable.setColorFilter(color, PorterDuff.Mode.SRC_ATOP);
        }
        textView.setCompoundDrawablesWithIntrinsicBounds(drawable, null, null, null);
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
        mDataManager.refresh();
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
            if (location != null) {
                LatLng currentPosition = new LatLng(location.getLatitude(), location.getLongitude());
                CameraPosition cameraPosition = new CameraPosition.Builder()
                        .target(currentPosition)      // Sets the center of the map to location user
                        .zoom(15)                   // Sets the zoom
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
                .strokeColor(ResourcesCompat.getColor(getResources(), mBikeMode ? R.color.blue_bike : R.color.purple_parking, null))
                .radius(500);
        mCircle = mGmap.addCircle(circleOptions);
    }

    private void updateMarker(LatLng currentPosition) {
        if (mBikeMode) {
            for (Marker marker : mMarkersBikes) {
                boolean showMarker = (calculationByDistance(marker.getPosition(), currentPosition) < 500);
                marker.setVisible(showMarker);
            }
        } else {
            for (Marker marker : mMarkersStands) {
                boolean showMarker = (calculationByDistance(marker.getPosition(), currentPosition) < 500);
                marker.setVisible(showMarker);
            }
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
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.about_us_menu:
                Intent intent = new Intent(this, AboutUsActivity.class);
                ContextCompat.startActivity(this, intent, null);
                overridePendingTransition(R.anim.slide_in, R.anim.stay);
                return true;
            case R.id.refresh_menu:
                refresh(menuItem);
                return true;
        }
        return super.onOptionsItemSelected(menuItem);
    }

    private void refresh(final MenuItem menuItem) {
        menuItem.setActionView(R.layout.actionbar_refresh);
        menuItem.getActionView().startAnimation(mRotateAnimation);
        mMenuItem = menuItem;
        mDataManager.refresh();
        for (Marker marker : mMarkersBikes) {
            marker.remove();
        }
        for (Marker marker : mMarkersStands) {
            marker.remove();
        }
        mMarkersBikes.clear();
        mMarkersStands.clear();
    }

    @Subscribe
    public void onDataChanged(final Event.StationListUpdatedEvent event) {
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                final List<MarkerOptions> bikeMarkerOptions = new ArrayList<>();
                final List<MarkerOptions> standMarkerOptions = new ArrayList<>();
                for (Station station : event.getStations()) {
                    bikeMarkerOptions.add(new MarkerOptions()
                            .position(station.getPosition())
                            .visible(false)
                            .icon(Tools.makeBitmap(MainActivity.this, R.color.blue_bike, R.drawable.ic_pin_full, "" + station.getAvailableBike()))
                            .title(station.getName().substring(station.getName().indexOf("-") + 1).trim()));
                    standMarkerOptions.add(new MarkerOptions()
                            .position(station.getPosition())
                            .visible(false)
                            .icon(Tools.makeBitmap(MainActivity.this, R.color.purple_parking, R.drawable.ic_pin_full, "" + station.getAvailableBikeStands()))
                            .title(station.getName().substring(station.getName().indexOf("-") + 1).trim()));
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        for (MarkerOptions options : bikeMarkerOptions) {
                            mMarkersBikes.add(mGmap.addMarker(options));
                        }
                        for (MarkerOptions options : standMarkerOptions) {
                            mMarkersStands.add(mGmap.addMarker(options));
                        }
                        onCameraIdle();
                        if (mMenuItem != null) {
                            mMenuItem.getActionView().clearAnimation();
                            mMenuItem.setActionView(null);
                            mMenuItem = null;
                        }
                    }
                });
            }
        });
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        marker.showInfoWindow();
        return true;
    }
}
