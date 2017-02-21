package com.xw.project.gracefulmovies.view.fragment;

import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.View;

import com.bumptech.glide.Glide;
import com.xw.project.gracefulmovies.R;

import org.polaric.colorful.Colorful;

/**
 * <p/>
 * Created by Artem Kholodnyi on 11/17/15.
 */
public class DayModeFragment extends SideFragment {

    public static final String TAG = "day";

    public static DayModeFragment newInstance(int centerX, int centerY, boolean appBarExpanded) {
        Bundle args = new Bundle();
        args.putInt(ARG_CX, centerX);
        args.putInt(ARG_CY, centerY);
        args.putBoolean(ARG_SHOULD_EXPAND, appBarExpanded);

        DayModeFragment frag = newInstance();
        frag.setArguments(args);

        return frag;
    }

    public static DayModeFragment newInstance() {
        return new DayModeFragment();
    }

    @Override
    int getTheme() {
        return Colorful.getThemeDelegate().getStyleResPrimary();
    }

    @Override
    void displayUI() {
        mCollapsingToolbarLayout.setContentScrimResource(Colorful.getThemeDelegate().getPrimaryColor().getColorRes());

        mToolbar.setTitle(getString(R.string.day_mode));

        mFab.setBackgroundTintList(createColorStateList(Colorful.getThemeDelegate().getPrimaryColor().getColorRes()));

        mHeaderImg.postDelayed(new Runnable() {
            @Override
            public void run() {
                Glide.with(getActivity()).load(R.drawable.pic_day).into(mHeaderImg);
                mHeaderImg.setAlpha(0f);
                mHeaderImg.animate().alpha(1.0f).setDuration(500);
            }
        }, 500);

        mSwitch.setChecked(false);

        mNestedScrollView.setBackgroundResource(android.R.color.white);

        mPrimaryColorImg.setVisibility(View.VISIBLE);

        mDayNightModeText.setTextColor(ContextCompat.getColor(getContext(), R.color.color_text_primary_dark));
        mPrimaryColorText.setTextColor(ContextCompat.getColor(getContext(), R.color.color_text_primary_dark));
        mAccentColorText.setTextColor(ContextCompat.getColor(getContext(), R.color.color_text_primary_dark));
        mQuoteText.setNight(false);
    }

    @Override
    public String getTagString() {
        return TAG;
    }
}
