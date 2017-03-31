package com.xw.project.gracefulmovies.view.activity;

import android.os.Bundle;

import com.xw.project.gracefulmovies.R;
import com.xw.project.gracefulmovies.view.fragment.SettingsFragment;

/**
 * 设置
 * <p/>
 * Created by woxingxiao on 2017-02-13.
 */
public class SettingsActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        initializeToolbar();
        if (getSupportActionBar() != null)
            getSupportActionBar().setTitle("设置");

        getFragmentManager().beginTransaction()
                .replace(R.id.settings_container, SettingsFragment.newInstance())
                .commit();
    }

}
