package com.xw.project.gracefulmovies.view.activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.AppCompatImageView;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.flaviofaria.kenburnsview.KenBurnsView;
import com.flaviofaria.kenburnsview.RandomTransitionGenerator;
import com.xw.project.gracefulmovies.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 启动页
 * <p/>
 * Created by woxingxiao on 2017-02-09.
 */
public class SplashActivity extends BaseActivity {

    @BindView(R.id.splash_root_layout)
    RelativeLayout mRootLayout;
    @BindView(R.id.splash_bg_img)
    KenBurnsView mBgImg;
    @BindView(R.id.splash_logo_img)
    AppCompatImageView mLogoImg;
    @BindView(R.id.splash_app_name_text)
    TextView mAppNameText;
    @BindView(R.id.splash_app_slogan_text)
    TextView mAppSloganText;

    private Handler mHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        transparentStatusBar();

        setContentView(R.layout.activity_splash);
        ButterKnife.bind(this);

        RandomTransitionGenerator generator = new RandomTransitionGenerator(20000, new LinearInterpolator());
        mBgImg.setTransitionGenerator(generator);
        Glide.with(this).load(R.drawable.pic_cinema).into(mBgImg);
        mRootLayout.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View view, int i, int i1, int i2, int i3, int i4, int i5, int i6, int i7) {
                mRootLayout.removeOnLayoutChangeListener(this);

                final int transX = mAppSloganText.getMeasuredWidth() >> 1;
                final int transY = mAppSloganText.getMeasuredHeight();

                mLogoImg.animate()
                        .rotation(-90)
                        .translationX(-transX)
                        .setDuration(500)
                        .setStartDelay(500)
                        .setInterpolator(new LinearInterpolator());

                mAppNameText.setAlpha(0);
                mAppNameText.animate()
                        .alpha(1)
                        .translationX(transX)
                        .setDuration(500)
                        .setStartDelay(500)
                        .setInterpolator(new LinearInterpolator());

                mAppSloganText.setAlpha(0);
                mAppSloganText.animate()
                        .translationX(transX)
                        .setDuration(500)
                        .setStartDelay(500)
                        .setInterpolator(new LinearInterpolator())
                        .setListener(new AnimatorListenerAdapter() {
                            @Override
                            public void onAnimationEnd(Animator animation) {
                                mAppNameText.animate()
                                        .translationY(-transY >> 1)
                                        .setDuration(300)
                                        .setInterpolator(new LinearInterpolator());

                                mAppSloganText.animate()
                                        .alpha(1)
                                        .translationY(transY)
                                        .setDuration(300)
                                        .setInterpolator(new LinearInterpolator())
                                        .setListener(new AnimatorListenerAdapter() {
                                            @Override
                                            public void onAnimationEnd(Animator animation) {
                                                jumpToMain();
                                            }
                                        });
                            }
                        });
            }
        });

        mHandler = new Handler();
    }

    private void jumpToMain() {
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                MainActivity.navigation(SplashActivity.this);
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                finish();
            }
        }, 500);
    }

    @Override
    protected void onPause() {
        super.onPause();

        mBgImg.pause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        mHandler.removeCallbacksAndMessages(null);
    }

}
