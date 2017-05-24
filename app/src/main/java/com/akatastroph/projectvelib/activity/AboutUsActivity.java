package com.akatastroph.projectvelib.activity;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.akatastroph.projectvelib.R;
import com.akatastroph.projectvelib.model.Developer;
import com.akatastroph.projectvelib.view.recyclerview.adapter.DeveloperAdapter;

import java.util.ArrayList;

import butterknife.BindView;

public class AboutUsActivity extends BaseActivity {
    @BindView(R.id.lists_developers) protected RecyclerView recyclerView;
    private DeveloperAdapter mDeveloperAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);
        ArrayList<Developer> developers = new ArrayList<>();
        developers.add(new Developer("Antoine PIRIOU", "piriou_a", R.drawable.piriou_a));
        developers.add(new Developer("Elise SCHRAVENDEEL", "schrav_e", R.drawable.schrav_e));
        mDeveloperAdapter = new DeveloperAdapter(developers);
        recyclerView.setAdapter(mDeveloperAdapter);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        if (menuItem.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(menuItem);
    }
}
