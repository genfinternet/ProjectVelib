package com.akatastroph.projectvelib.view.recyclerview.viewholder;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.akatastroph.projectvelib.R;
import com.akatastroph.projectvelib.model.Developer;
import com.akatastroph.projectvelib.utils.transformation.CircleTransform;
import com.squareup.picasso.Picasso;

import butterknife.BindView;

/**
 * Created by Genfinternet on 20/11/2016.
 */

public class DeveloperViewHolder extends BaseViewHolder<Developer> {
    @BindView(R.id.name) protected TextView mNameTextView;
    @BindView(R.id.login) protected TextView mLoginTextView;
    @BindView(R.id.portrait) protected ImageView mPortraitImageView;

    public DeveloperViewHolder(View itemView, OnViewHolderClick listener, boolean shouldBind) {
        super(itemView, listener, shouldBind);
    }

    public void configure() {
        mNameTextView.setText(mItem.getFullName());
        mLoginTextView.setText(mItem.getLogin());
        Picasso.with(itemView.getContext()).load(mItem.getIdPortrait())
                .transform(new CircleTransform())
                .resize(0, 300)
                .into(mPortraitImageView);
    }

    public static DeveloperViewHolder onCreateView(ViewGroup parent, OnViewHolderClick listerner) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.developer_item, parent, false);
        return new DeveloperViewHolder(view, listerner, true);
    }
}
