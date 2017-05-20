package com.akatastroph.projectvelib.view.recyclerview.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.akatastroph.projectvelib.view.recyclerview.viewholder.BaseViewHolder;

import java.util.ArrayList;

/**
 * Created by genfinternet on 12/05/2017.
 */

public abstract class BaseAdapter<T, VH extends BaseViewHolder<T>> extends RecyclerView.Adapter<VH> {
    private static final int NORMAL = 0;
    private static final int LOADING = 1;

    private boolean mLoading;
    private ArrayList<T> mItems;


    public BaseAdapter(ArrayList<T> items) {
        mItems = items;
        mLoading = false;
    }

    public BaseAdapter(ArrayList<T> items, boolean loading) {
        mItems = items;
        mLoading = loading;
    }

    @Override
    public int getItemViewType(int position) {
        if (mItems.size() == position) {
            return LOADING;
        }
        return NORMAL;
    }

    @Override
    public VH onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case LOADING:
                return onCreateLoadingViewHolder(parent);
            case NORMAL:
            default:
                return onCreateContentViewHolder(parent);
        }
    }

    abstract VH onCreateContentViewHolder(ViewGroup parent);

    abstract VH onCreateLoadingViewHolder(ViewGroup parent);

    @Override
    public void onBindViewHolder(VH holder, int position) {
        holder.bind(mItems.size() > position ? mItems.get(position) : null);
    }

    @Override
    public int getItemCount() {
        return mItems.size() + (mLoading ? 1 : 0);
    }

    public ArrayList<T> getStations() {
        return mItems;
    }

    public void setItems(ArrayList<T> items) {
        mItems = items;
        notifyDataSetChanged();
    }

    public boolean isLoading() {
        return mLoading;
    }

    public void setLoading(boolean loading) {
        mLoading = loading;
    }
}
