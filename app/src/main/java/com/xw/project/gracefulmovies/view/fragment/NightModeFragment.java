package com.xw.project.gracefulmovies.view.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.view.View;

import com.bumptech.glide.Glide;
import com.xw.project.gracefulmovies.R;

import org.polaric.colorful.Colorful;

/**
 * <p/>
 * Created by Artem Kholodnyi on 11/17/15.
 */
public class NightModeFragment extends SideFragment {

    public static final String TAG = "night";

    public static NightModeFragment newInstance(int centerX, int centerY, boolean appBarExpanded) {
        Bundle args = new Bundle();
        args.putInt(ARG_CX, centerX);
        args.putInt(ARG_CY, centerY);
        args.putBoolean(ARG_SHOULD_EXPAND, appBarExpanded);

        NightModeFragment frag = newInstance();
        frag.setArguments(args);

        return frag;
    }

    public static NightModeFragment newInstance() {
        return new NightModeFragment();
    }

    @Override
    int getTheme() {
        return Colorful.getThemeDelegate().getStyleResPrimary();
    }

    @Override
    void displayUI() {
        mCollapsingToolbarLayout.setContentScrimResource(R.color.md_night_primary);

        mToolbar.setTitle(getString(R.string.night_mode));

        mFab.setBackgroundTintList(createColorStateList(R.color.md_night_primary));

        if (mHandler == null) {
            mHandler = new Handler();
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    Glide.with(getActivity()).load(R.drawable.pic_night).into(mHeaderImg);
                    mHeaderImg.setAlpha(0f);
                    mHeaderImg.animate().alpha(1.0f).setDuration(500);
                }
            }, 500);
        }

        mSwitch.setChecked(true);

        mNestedScrollView.setBackgroundResource(R.color.color_window_background_night);

        mPrimaryColorImg.setVisibility(View.INVISIBLE);

        mDayNightModeText.setTextColor(Color.WHITE);
        mPrimaryColorText.setTextColor(ContextCompat.getColor(getContext(), R.color.color_disable));
        mAccentColorText.setTextColor(Color.WHITE);
        mQuoteText.setNight(true);
    }

    @Override
    public String getTagString() {
        return TAG;
    }
}
