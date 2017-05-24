package com.akatastroph.projectvelib.fragment;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.akatastroph.projectvelib.AkatastrophApplication;
import com.akatastroph.projectvelib.R;
import com.akatastroph.projectvelib.activity.MainActivity;
import com.akatastroph.projectvelib.utils.UserPreferences;
import com.squareup.picasso.Picasso;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class OnBoardingFragment extends Fragment {
    @Inject UserPreferences mUserPreferences;
    private static final String POSITION_ARG = "POSITION_ARG";

    @BindView(R.id.title) protected TextView mTitleView;
    @BindView(R.id.description) protected TextView mDescriptionView;
    @BindView(R.id.image) protected ImageView mImageView;
    @BindView(R.id.button) protected Button mButton;
    @Nullable @BindView(R.id.anchor) protected View mAnchor;

    public static final int PAGE_ONE = 0;
    public static final int PAGE_TWO = 1;
    public static final int PAGE_THREE = 2;

    private int mPageNumber;

    public OnBoardingFragment() {}

    public static OnBoardingFragment newInstance(int nbPage) {
        OnBoardingFragment fragment = new OnBoardingFragment();
        Bundle args = new Bundle();
        args.putInt(POSITION_ARG, nbPage);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AkatastrophApplication.getInstance().getAkatastrophComponent().inject(this);
        if (getArguments() != null) {
            mPageNumber = getArguments().getInt(POSITION_ARG, 0);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_on_boarding, container, false);
        ButterKnife.bind(this, rootView);
        switch (mPageNumber) {
            case PAGE_ONE:
                mTitleView.setText(R.string.on_boarding_title_1);
                mDescriptionView.setText(R.string.on_boarding_description_1);
                Picasso.with(getContext())
                        .load(R.drawable.on_boarding_image_1)
                        .resize(0, 900)
                        .into(mImageView);
                mButton.setVisibility(View.GONE);
                mImageView.setVisibility(View.VISIBLE);
                if (mAnchor != null) {
                    mAnchor.setVisibility(View.VISIBLE);
                }
                break;
            case PAGE_TWO:
                mTitleView.setText(R.string.on_boarding_title_2);
                mDescriptionView.setText(R.string.on_boarding_description_2);
                Picasso.with(getContext())
                        .load(R.drawable.on_boarding_image_2)
                        .resize(0, 900)
                        .into(mImageView);
                mButton.setVisibility(View.GONE);
                mImageView.setVisibility(View.VISIBLE);
                if (mAnchor != null) {
                    mAnchor.setVisibility(View.VISIBLE);
                }
                break;
            case PAGE_THREE:
                mTitleView.setText(R.string.on_boarding_title_3);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    mDescriptionView.setText(Html.fromHtml(getString(R.string.on_boarding_description_3), Html.FROM_HTML_MODE_LEGACY));
                } else {
                    mDescriptionView.setText(Html.fromHtml(getString(R.string.on_boarding_description_3)));
                }
                mDescriptionView. setMovementMethod(LinkMovementMethod.getInstance());
                mButton.setVisibility(View.VISIBLE);
                mImageView.setVisibility(View.GONE);
                if (mAnchor != null) {
                    mAnchor.setVisibility(View.GONE);
                }
                break;
        }
        return rootView;
    }

    @OnClick(R.id.button)
    public void finishTutorial() {
        mUserPreferences.completeFirstStart();
        startActivity(new Intent(getActivity(), MainActivity.class));
        getActivity().finish();
    }
}
