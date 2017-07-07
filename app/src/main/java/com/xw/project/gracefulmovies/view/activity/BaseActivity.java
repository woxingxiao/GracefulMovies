package com.xw.project.gracefulmovies.view.activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.ColorRes;
import android.support.annotation.FloatRange;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

import com.oubowu.slideback.SlideBackHelper;
import com.oubowu.slideback.SlideConfig;
import com.oubowu.slideback.callbak.OnSlideListener;
import com.xw.project.gracefulmovies.GMApplication;
import com.xw.project.gracefulmovies.R;

import org.polaric.colorful.ColorfulActivity;

import java.io.Serializable;

/**
 * BaseActivity
 * <p/>
 * Created by woxingxiao on 2017-02-06.
 */

public abstract class BaseActivity extends ColorfulActivity {

    static {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    }

    protected static final String PARAM_1 = "param_1";
    protected static final String PARAM_2 = "param_2";
    protected static final String OBJ_1 = "obj_1";
    protected static final int REQUEST_CODE_1 = 0x22;

    private static final String[] PARAMS = {PARAM_1, PARAM_2};
    private static final String[] OBJECTS = {OBJ_1};

    protected Activity mActivity;

    private boolean hasIntentionToSlideBack; // 具有触发滑动返回的意图

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mActivity = this;

        initializeSlideBack();
    }

    /**
     * 初始化滑动返回
     */
    private void initializeSlideBack() {
        SlideBackHelper.attach(
                this, // 当前Activity
                GMApplication.getActivityHelper(), // Activity栈管理工具
                new SlideConfig.Builder() // 参数的配置
                        .rotateScreen(false) // 屏幕是否旋转
                        .edgeOnly(true) // 是否侧滑
                        .lock(false) // 是否禁止侧滑
                        .edgePercent(0.2f) // 边缘滑动的响应阈值，0~1，对应屏幕宽度*percent
                        .slideOutPercent(0.5f) // 关闭页面的阈值，0~1，对应屏幕宽度*percent
                        .create(),
                new OnSlideListener() { // 滑动的监听
                    @Override
                    public void onSlide(@FloatRange(from = 0.0, to = 1.0) float percent) {
                        if (!hasIntentionToSlideBack && percent > 0.0) {
                            hasIntentionToSlideBack = true;
                            onSlideBackIntention();
                        }
                    }

                    @Override
                    public void onOpen() {
                        hasIntentionToSlideBack = false;
                    }

                    @Override
                    public void onClose() {

                    }
                }
        );
    }

    protected void navigate(Class toActivity) {
        startActivity(new Intent(this, toActivity));
    }

    protected void navigate(Class toActivity, @NonNull Object... params) {
        startActivity(assembleIntentWithParam(new Intent(this, toActivity), params));
    }

    protected void navigateForResult(int requestCode, Class toActivity) {
        startActivityForResult(new Intent(this, toActivity), requestCode);
    }

    protected void navigateForResult(int requestCode, Class toActivity, @NonNull Object... params) {
        startActivityForResult(assembleIntentWithParam(new Intent(this, toActivity), params), requestCode);
    }

    public static void navigate(Context activity, Class toActivity) {
        activity.startActivity(new Intent(activity, toActivity));
    }

    public static void navigate(Context activity, Class toActivity, @NonNull Object... params) {
        activity.startActivity(assembleIntentWithParam(new Intent(activity, toActivity), params));
    }

    private static Intent assembleIntentWithParam(Intent intent, @NonNull Object... params) {
        int p_i = 0;
        int o_i = 0;

        for (Object obj : params) {
            if (obj instanceof Integer) {
                intent.putExtra(PARAMS[p_i], (int) obj);
            } else if (obj instanceof Boolean) {
                intent.putExtra(PARAMS[p_i], (boolean) obj);
            } else if (obj instanceof Float) {
                intent.putExtra(PARAMS[p_i], (float) obj);
            } else if (obj instanceof Double) {
                intent.putExtra(PARAMS[p_i], (double) obj);
            } else if (obj instanceof String) {
                intent.putExtra(PARAMS[p_i], (String) obj);
            } else if (obj instanceof Long) {
                intent.putExtra(PARAMS[p_i], (long) obj);
            } else if (obj instanceof Parcelable) {
                intent.putExtra(OBJECTS[o_i], (Parcelable) obj);
            } else if (obj instanceof Serializable) {
                intent.putExtra(OBJECTS[o_i], (Serializable) obj);
            }

            if (obj instanceof Integer || obj instanceof Boolean || obj instanceof Float
                    || obj instanceof Double || obj instanceof String || obj instanceof Long) {
                p_i++;
            } else if (obj instanceof Parcelable || obj instanceof Serializable) {
                o_i++;
            }
        }

        return intent;
    }

    /**
     * 带水波动画的Activity跳转
     */
    @SuppressLint("NewApi")
    protected void navigateWithRippleCompat(final Activity activity, final Intent intent,
                                            final View triggerView, @ColorRes int color) {

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            ActivityOptionsCompat option = ActivityOptionsCompat.makeClipRevealAnimation(triggerView, 0, 0,
                    triggerView.getMeasuredWidth(), triggerView.getMeasuredHeight());
            ActivityCompat.startActivity(activity, intent, option.toBundle());

            return;
        }

        int[] location = new int[2];
        triggerView.getLocationInWindow(location);
        final int cx = location[0] + triggerView.getWidth() / 2;
        final int cy = location[1] + triggerView.getHeight() / 2;
        final ImageView view = new ImageView(activity);
        view.setImageResource(color);
        final ViewGroup decorView = (ViewGroup) activity.getWindow().getDecorView();
        int w = decorView.getWidth();
        int h = decorView.getHeight();
        decorView.addView(view, w, h);
        int finalRadius = (int) Math.sqrt(w * w + h * h) + 1;
        Animator anim = ViewAnimationUtils.createCircularReveal(view, cx, cy, 0, finalRadius);
        anim.setDuration(500);
        anim.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);

                activity.startActivity(intent);
                activity.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                decorView.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        decorView.removeView(view);
                    }
                }, 500);
            }
        });
        anim.start();
    }

    /**
     * 检测系统版本并使状态栏全透明
     */
    protected void transparentStatusBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
//            window.requestFeature(Window.FEATURE_CONTENT_TRANSITIONS); // 新增滑动返回，舍弃过渡动效

            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS |
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN |
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
        }
    }

    /**
     * 初始化Toolbar的功能
     */
    protected void initializeToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    protected void showToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    /**
     * 当有滑动返回的意图时
     */
    protected void onSlideBackIntention() {
    }
}
