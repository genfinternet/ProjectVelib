package com.akatastroph.projectvelib.view.recyclerview.viewholder;

import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.akatastroph.projectvelib.R;
import com.akatastroph.projectvelib.model.Station;

import butterknife.BindView;

/**
 * Created by Genfinternet on 20/11/2016.
 */

public class StationViewHolder extends BaseViewHolder<Station> {
    @BindView(R.id.name) protected TextView mNameView;
    @BindView(R.id.address) protected TextView mAddressView;
    @BindView(R.id.available_bikes) protected TextView mAvailableBikesView;
    @BindView(R.id.available_stands) protected TextView mAvailableStandsView;
    @BindView(R.id.card_view) protected CardView mCardView;

    public StationViewHolder(View itemView, OnViewHolderClick listener, boolean shouldBind) {
        super(itemView, listener, shouldBind);
    }

    public void configure() {
        mNameView.setText(mItem.getName());
        mAddressView.setText(mItem.getAddress());
        mAvailableBikesView.setText(String.valueOf(mItem.getAvailableBike()));
        mAvailableStandsView.setText(String.valueOf(mItem.getAvailableBikeStands()));
        mCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    public static StationViewHolder onCreateView(ViewGroup parent, OnViewHolderClick listerner) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.station_item, parent, false);
        return new StationViewHolder(view, listerner, true);
    }
}
