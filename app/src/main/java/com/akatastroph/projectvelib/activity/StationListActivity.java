package com.akatastroph.projectvelib.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import com.akatastroph.projectvelib.AkatastrophApplication;
import com.akatastroph.projectvelib.R;
import com.akatastroph.projectvelib.manager.DataManager;
import com.akatastroph.projectvelib.model.Station;
import com.akatastroph.projectvelib.utils.events.Event;
import com.akatastroph.projectvelib.view.recyclerview.adapter.searchadapter.BaseSearchAdapter;
import com.akatastroph.projectvelib.view.recyclerview.adapter.searchadapter.StationSearchAdapter;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnEditorAction;
import butterknife.OnTextChanged;

public class StationListActivity extends BaseActivity implements BaseSearchAdapter.OnItemClickListener {

    public static final String BUNDLE_RECYCLER_STATE = "BUNDLE_RECYCLER_STATE";
    public static final String BUNDLE_LIST_FILTER = "BUNDLE_LIST_FILTER";

    @Inject protected DataManager mDataManager;
    private StationSearchAdapter mStationAdapter;

    @BindView(R.id.list_stations) protected RecyclerView recyclerView;
    @BindView(R.id.toolbar) protected Toolbar mToolbar;
    @BindView(R.id.search_station) protected EditText mSearchView;
    @BindView(R.id.swipeContainer) protected SwipeRefreshLayout mRefreshLayout;

    private MenuItem mMenuItem;
    private Animation mRotateAnimation;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        AkatastrophApplication.getInstance().getAkatastrophComponent().inject(this);
        setContentView(R.layout.activity_station_list);
        mStationAdapter = new StationSearchAdapter(mDataManager.getStations(), Station.class, new Station.StationComparator(), this, !mDataManager.isReady());
        recyclerView.setAdapter(mStationAdapter);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        setSupportActionBar(mToolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        if (savedInstanceState != null) {
            recyclerView.getLayoutManager().onRestoreInstanceState(savedInstanceState.getParcelable(BUNDLE_RECYCLER_STATE));
            mSearchView.setText(savedInstanceState.getString(BUNDLE_LIST_FILTER));
        }
        mRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refresh();
            }
        });
        mRotateAnimation = AnimationUtils.loadAnimation(this, R.anim.rotate);
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
                refresh();
                return true;
        }
        return super.onOptionsItemSelected(menuItem);
    }
    private void refresh() {
        mRefreshLayout.setRefreshing(true);
        mStationAdapter.replaceAll(new ArrayList<Station>());
        mDataManager.refresh();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.stay, R.anim.slide_out);
    }

    @OnEditorAction(R.id.search_station)
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        boolean handled = false;
        if (actionId == EditorInfo.IME_ACTION_DONE) {
            if(getCurrentFocus()!=null) {
                InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
            }
            handled = true;
        }
        return handled;
    }

    @OnTextChanged(value = R.id.search_station, callback = OnTextChanged.Callback.AFTER_TEXT_CHANGED)
    public void onTextChanged(Editable s) {
        mDataManager.filterAsync(s.toString());
        mStationAdapter.setLoading(false);
    }

    @Override
    public void onItemClick(int position) {
        Intent intent = new Intent(this, StationDetailActivity.class);
        intent.putExtra(StationDetailActivity.ARG_STATION_INDEX, position);
        ActivityCompat.startActivity(this, intent, null);
        overridePendingTransition(R.anim.slide_in, R.anim.stay);
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

    @Subscribe
    public void onDataFilteredChanged(Event.FilteredStationListUpdatedEvent event) {
        mRefreshLayout.setRefreshing(false);
        if (mMenuItem != null) {
            mMenuItem.getActionView().clearAnimation();
            mMenuItem.setActionView(null);
            mMenuItem = null;
        }
        if (mStationAdapter != null) {
            mStationAdapter.setLoading(false);
            mStationAdapter.replaceAll(event.getStations());
            recyclerView.scrollToPosition(0);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.refresh_menu, menu);
        return true;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putParcelable(BUNDLE_RECYCLER_STATE, recyclerView.getLayoutManager().onSaveInstanceState());
        outState.putString(BUNDLE_LIST_FILTER, mSearchView.getText().toString());
        super.onSaveInstanceState(outState);
    }
}
