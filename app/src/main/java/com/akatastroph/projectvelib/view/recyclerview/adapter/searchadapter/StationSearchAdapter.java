package com.akatastroph.projectvelib.view.recyclerview.adapter.searchadapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.akatastroph.projectvelib.R;
import com.akatastroph.projectvelib.model.Station;
import com.akatastroph.projectvelib.view.recyclerview.viewholder.StationViewHolder;

import java.util.Comparator;
import java.util.List;

/**
 * Created by Genfinternet on 20/11/2016.
 */

public class StationSearchAdapter extends BaseSearchAdapter<Station, StationViewHolder> {

    public StationSearchAdapter(List<Station> items, Class<Station> klass, Comparator<Station> comparator, OnItemClickListener listener) {
        super(items, klass, comparator, listener);
    }

    public StationSearchAdapter(List<Station> items, Class<Station> klass, Comparator<Station> comparator, OnItemClickListener listener, boolean loading) {
        super(items, klass, comparator, listener, loading);
    }

    @Override
    StationViewHolder onCreateContentViewHolder(ViewGroup parent) {
        return StationViewHolder.onCreateView(parent, this);
    }

    @Override
    StationViewHolder onCreateLoadingViewHolder(ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.loading_item, parent, false);
        return new StationViewHolder(view, this, false);
    }
}
