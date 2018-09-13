package com.xw.project.gracefulmovies.ui.activity.base;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.support.annotation.IntDef;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.content.res.AppCompatResources;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.FrameLayout;

import com.xw.project.gracefulmovies.R;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * <p>
 * Created by woxingxiao on 2018-08-23.
 */

public class StatusView extends ConstraintLayout {

    public static final int LOADING = 0;
    public static final int NO_DATA = 1;
    public static final int CONNECTION_ERROR = 2;
    public static final int CONNECTION_TIME_OUT = 3;

    @IntDef({LOADING, NO_DATA, CONNECTION_ERROR, CONNECTION_TIME_OUT})
    @Retention(RetentionPolicy.SOURCE)
    @interface Status {
    }

    private AppCompatImageView mLoadingIv;
    private AppCompatTextView mHintTv;
    private Button mReloadBtn;

    private int mBgColor;
    private Drawable mBgDrawable;
    private ObjectAnimator mAnimator;
    private OnReloadListener mOnReloadListener;
    private Handler mHandler = new Handler();

    public StatusView(Context context) {
        super(context);

        LayoutInflater.from(context).inflate(R.layout.layout_status_view, this, true);
        mLoadingIv = findViewById(R.id.status_loading_iv);
        mHintTv = findViewById(R.id.status_hint_tv);
        mReloadBtn = findViewById(R.id.status_reload_btn);
        mReloadBtn.setOnClickListener(v -> {
            if (mOnReloadListener != null)
                mOnReloadListener.onReconnect();
        });

        setClickable(true); // block other views' touch event who under this view

        TypedValue a = new TypedValue();
        context.getTheme().resolveAttribute(android.R.attr.windowBackground, a, true);
        if (a.type >= TypedValue.TYPE_FIRST_COLOR_INT && a.type <= TypedValue.TYPE_LAST_COLOR_INT) {
            // windowBackground is a color
            mBgColor = a.data;
            setBackgroundColor(mBgColor);
        } else {
            // windowBackground is not a color, probably a drawable
            mBgDrawable = context.getResources().getDrawable(a.resourceId);
            setBackground(mBgDrawable);
        }
    }

    public void show(@NonNull ViewGroup viewContainer, @Status int status, int marginDp) {
        if (getParent() == null) {
            MarginLayoutParams lp;
            if (viewContainer instanceof FrameLayout) {
                lp = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT);
            } else {
                lp = new MarginLayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT);
            }
            lp.topMargin = marginDp;
            viewContainer.addView(this, lp);
        }

        if (status == LOADING) {
            setBackgroundColor(Color.TRANSPARENT);

            mLoadingIv.setVisibility(View.VISIBLE);
            mHintTv.setVisibility(View.INVISIBLE);
            mReloadBtn.setVisibility(View.INVISIBLE);

            mAnimator = ObjectAnimator.ofFloat(mLoadingIv, View.ROTATION, 0, 360);
            mAnimator.setDuration(1000);
            mAnimator.setInterpolator(new LinearInterpolator());
            mAnimator.setRepeatCount(ValueAnimator.INFINITE);
            mAnimator.setRepeatMode(ValueAnimator.RESTART);
            mAnimator.start();
        } else {
            if (mBgColor != 0) {
                setBackgroundColor(mBgColor);
            } else {
                setBackground(mBgDrawable);
            }

            mLoadingIv.setVisibility(View.INVISIBLE);
            mHintTv.setVisibility(View.VISIBLE);

            if (mAnimator != null)
                mAnimator.cancel();

            Drawable drawable = null;
            if (status == NO_DATA) {
                mHintTv.setText("空空如也");
                drawable = AppCompatResources.getDrawable(getContext(), R.drawable.ic_status_no_data);

                mReloadBtn.setVisibility(View.INVISIBLE);
            } else if (status == CONNECTION_ERROR) {
                mHintTv.setText("网络连接错误");
                drawable = AppCompatResources.getDrawable(getContext(), R.drawable.ic_status_connection_error);

                mReloadBtn.setVisibility(View.VISIBLE);
            } else if (status == CONNECTION_TIME_OUT) {
                mHintTv.setText("网络请求超时");
                drawable = AppCompatResources.getDrawable(getContext(), R.drawable.ic_status_connection_time_out);

                mReloadBtn.setVisibility(View.VISIBLE);
            }
            mHintTv.setCompoundDrawablesWithIntrinsicBounds(null, drawable, null, null);
        }
    }

    public void dismiss() {
        if (getParent() != null) {
            ((ViewGroup) getParent()).removeView(this);
        }
    }

    public void setOnReloadListener(OnReloadListener listener) {
        mOnReloadListener = listener;
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();

        mHandler.removeCallbacksAndMessages(null);
    }

    public interface OnReloadListener {
        void onReconnect();
    }
}
