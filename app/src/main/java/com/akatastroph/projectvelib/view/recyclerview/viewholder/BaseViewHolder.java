package com.akatastroph.projectvelib.view.recyclerview.viewholder;

import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.akatastroph.projectvelib.R;

import butterknife.ButterKnife;

public abstract class BaseViewHolder<T> extends RecyclerView.ViewHolder {
    public interface OnViewHolderClick {
        void onItemClick(int position);
    }
    T mItem;
    private boolean mShouldBind;
    private OnViewHolderClick mListener;

    BaseViewHolder(View itemView, OnViewHolderClick listener, boolean shouldBind) {
        super(itemView);
        mShouldBind = shouldBind;
        mListener = listener;
        if (mShouldBind) {
            ButterKnife.bind(this, itemView);
            itemView.findViewById(R.id.content_layout).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mListener != null) {
                        mListener.onItemClick(getAdapterPosition());
                    }
                }
            });
        }
    }

    public void bind(@Nullable T item) {
        mItem = item;
        if (mShouldBind) {
            configure();
        }
    }

    abstract void configure();
}
