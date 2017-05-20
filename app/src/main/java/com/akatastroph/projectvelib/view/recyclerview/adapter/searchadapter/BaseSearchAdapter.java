package com.akatastroph.projectvelib.view.recyclerview.adapter.searchadapter;

import android.support.v7.util.SortedList;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.akatastroph.projectvelib.view.recyclerview.viewholder.BaseViewHolder;

import java.util.Comparator;
import java.util.List;

/**
 * Created by genfinternet on 13/05/2017.
 */

public abstract class BaseSearchAdapter<T extends SortedListModel<T>, VH extends BaseViewHolder<T>> extends RecyclerView.Adapter<VH> implements BaseViewHolder.OnViewHolderClick {
    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    private static final int NORMAL = 0;
    private static final int LOADING = 1;

    private boolean mLoading;
    private OnItemClickListener mListener;

    private final Comparator<T> mComparator;

    private final SortedList<T> mItems;

    public BaseSearchAdapter(List<T> items, Class<T> klass, Comparator<T> comparator, OnItemClickListener listener) {
        this(items, klass, comparator, listener, false);
    }

    public BaseSearchAdapter(List<T> items, Class<T> klass, Comparator<T> comparator, OnItemClickListener listener, boolean loading) {
        this.mItems = new SortedList<>(klass, new SortedList.Callback<T>() {
            @Override
            public int compare(T a, T b) {
                return mComparator.compare(a, b);
            }

            @Override
            public void onInserted(int position, int count) {
                notifyItemRangeInserted(position, count);
            }

            @Override
            public void onRemoved(int position, int count) {
                notifyItemRangeRemoved(position, count);
            }

            @Override
            public void onMoved(int fromPosition, int toPosition) {
                notifyItemMoved(fromPosition, toPosition);
            }

            @Override
            public void onChanged(int position, int count) {
                notifyItemRangeChanged(position, count);
            }

            @Override
            public boolean areContentsTheSame(T oldItem, T newItem) {
                return oldItem.equals(newItem);
            }

            @Override
            public boolean areItemsTheSame(T item1, T item2) {
                return item1.isTheSameItem(item2);
            }
        });
        this.mLoading = loading;
        this.mItems.clear();
        this.mItems.addAll(items);
        this.mComparator = comparator;
        this.mListener = listener;
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

    public boolean isLoading() {
        return mLoading;
    }

    public void setLoading(boolean loading) {
        mLoading = loading;
    }

    public void replaceAll(List<T> models) {
        mItems.beginBatchedUpdates();
        for (int i = mItems.size() - 1; i >= 0; i--) {
            final T model = mItems.get(i);
            if (!models.contains(model)) {
                mItems.remove(model);
            }
        }
        mItems.addAll(models);
        mItems.endBatchedUpdates();
    }

    @Override
    public void onItemClick(int position) {
        if (mListener != null) {
            mListener.onItemClick(position);
        }
    }
}