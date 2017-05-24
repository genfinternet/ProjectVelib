package com.akatastroph.projectvelib.activity;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.akatastroph.projectvelib.AkatastrophApplication;
import com.akatastroph.projectvelib.R;
import com.akatastroph.projectvelib.utils.UserPreferences;

import javax.inject.Inject;

import butterknife.BindView;

import static com.akatastroph.projectvelib.AkatastrophApplication.PERMISSIONS_REQUEST_LOCATION;

public class SplashScreenActivity extends BaseActivity {
    @Inject UserPreferences mUserPreferences;
    @BindView(R.id.logo) View logo;
    @BindView(R.id.company_name) View name;
    private Animation mGrowth;
    private Animation mSlideUpAndFadeIn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splashscreen);
        AkatastrophApplication.getInstance().getAkatastrophComponent().inject(this);
        init();
    }

    public void init() {
        initActionBar();
        initAnimation();
    }

    public void initActionBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(getColor(R.color.white));
        }
    }

    public void initAnimation () {
        mGrowth = AnimationUtils.loadAnimation(this, R.anim.splashscreen_growth);
        mSlideUpAndFadeIn = AnimationUtils.loadAnimation(this, R.anim.slide_up_and_fade_in);
        mGrowth.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                checkPermission();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    public void reset() {
        logo.startAnimation(mGrowth);
        name.startAnimation(mSlideUpAndFadeIn);
    }

    @Override
    public void onStart() {
        super.onStart();
        reset();
    }

    private void startNextActivity() {
        if (mUserPreferences.isFirstStart()) {
            startActivity(new Intent(this, OnBoardingActivity.class));
            finish();
        } else {
            startActivity(new Intent(this, MainActivity.class));
            finish();
        }
    }

    private void checkPermission() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage(R.string.dialog_message);
                builder.setPositiveButton(R.string.dialog_ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        requestPermission();
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
            } else {
                requestPermission();
            }
        } else {
            startNextActivity();
        }
    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                PERMISSIONS_REQUEST_LOCATION);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[], @NonNull int[] grantResults) {
        switch (requestCode) {
            case PERMISSIONS_REQUEST_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    startNextActivity();
                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setMessage(R.string.dialog_permission_denied);
                    builder.setNeutralButton(R.string.dialog_ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            startNextActivity();
                        }
                    });
                    AlertDialog dialog = builder.create();
                    dialog.show();
                }
                break;
            }
        }
    }
}
