package com.xw.project.gracefulmovies.view.activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Process;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatImageView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.LinearInterpolator;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.flaviofaria.kenburnsview.KenBurnsView;
import com.flaviofaria.kenburnsview.RandomTransitionGenerator;
import com.xw.project.gracefulmovies.R;
import com.xw.project.gracefulmovies.server.ApiHelper;
import com.xw.project.gracefulmovies.util.Logy;
import com.xw.project.gracefulmovies.util.SharedPrefHelper;

import org.polaric.colorful.Colorful;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 启动页
 * <p/>
 * Created by woxingxiao on 2017-02-09.
 */
public class SplashActivity extends AppCompatActivity {

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
        // transparent status bar
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS |
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN |
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
        }

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

        /**
         * 三方初始化放入工作线程，加速App启动
         */
        new Thread() {
            @Override
            public void run() {
                super.run();
                Process.setThreadPriority(Process.THREAD_PRIORITY_BACKGROUND);

                Colorful.defaults()
                        .primaryColor(Colorful.ThemeColor.DARK)
                        .accentColor(Colorful.ThemeColor.DEEP_ORANGE)
                        .translucent(false)
                        .night(false);
                Colorful.init(getApplicationContext());

                SharedPrefHelper.init(getApplicationContext());
                Logy.init(true);
                String s1 = ""; // 聚合Api Key
                String s2 = ""; // 易源App Id
                String s3 = ""; // 易源Api Key
                ApiHelper.init(s1, s2, s3); // TODO: 2017-02-24 add your api key to request data
//                CrashHandler.getInstance().init(this);
            }
        }.start();
    }

    private void jumpToMain() {
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                MainActivity.navigation(SplashActivity.this);
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                finish();
            }
        }, 300);
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
