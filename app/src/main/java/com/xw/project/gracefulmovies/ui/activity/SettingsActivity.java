package com.xw.project.gracefulmovies.ui.activity;

import com.xw.project.gracefulmovies.R;
import com.xw.project.gracefulmovies.ui.activity.base.BaseActivity;
import com.xw.project.gracefulmovies.ui.fragment.SettingsFragment;

/**
 * 设置
 * <p>
 * Created by woxingxiao on 2018-08-24.
 */
public class SettingsActivity extends BaseActivity {

    @Override
    protected int contentLayoutRes() {
        return R.layout.activity_settings;
    }

    @Override
    protected void afterSetContentView() {
        initializeToolbar();

        getFragmentManager().beginTransaction()
                .replace(R.id.settings_container, SettingsFragment.newInstance())
                .commit();
    }
}
