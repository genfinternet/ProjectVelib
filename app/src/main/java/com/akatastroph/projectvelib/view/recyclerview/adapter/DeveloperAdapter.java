package com.akatastroph.projectvelib.view.recyclerview.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.akatastroph.projectvelib.R;
import com.akatastroph.projectvelib.model.Developer;
import com.akatastroph.projectvelib.view.recyclerview.viewholder.BaseViewHolder;
import com.akatastroph.projectvelib.view.recyclerview.viewholder.DeveloperViewHolder;

import java.util.ArrayList;

/**
 * Created by genfinternet on 22/05/2017.
 */

public class DeveloperAdapter extends BaseAdapter<Developer, DeveloperViewHolder> implements BaseViewHolder.OnViewHolderClick {
    public DeveloperAdapter(ArrayList<Developer> items) {
        super(items);
    }

    @Override
    DeveloperViewHolder onCreateContentViewHolder(ViewGroup parent) {
        return DeveloperViewHolder.onCreateView(parent, this);
    }

    @Override
    DeveloperViewHolder onCreateLoadingViewHolder(ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.loading_item, parent, false);
        return new DeveloperViewHolder(view, this, false);
    }

    @Override
    public void onItemClick(int position) {

    }
}
