package com.xw.project.gracefulmovies.view.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.xw.project.gracefulmovies.R;
import com.xw.project.gracefulmovies.server.ApiHelper;
import com.xw.project.gracefulmovies.view.adapter.TabPagerAdapter;
import com.xw.project.gracefulmovies.view.fragment.BoxOfficeListFragment;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 票房
 * <p/>
 * Created by xoxingxiao on 2017-03-07.
 */
public class BoxOfficeActivity extends BaseActivity {

    /**
     * 每日票房
     */
    public static final int DAY = 0;
    /**
     * 周末票房
     */
    public static final int WEEKEND = 1;
    /**
     * 每周票房
     */
    public static final int WEEK = 2;
    /**
     * 每月票房
     */
    public static final int MONTH = 3;

    @BindView(R.id.tab_layout)
    TabLayout mTabLayout;
    @BindView(R.id.view_pager)
    ViewPager mViewPager;

    public static void navigation(Activity activity) {
        activity.startActivity(new Intent(activity, BoxOfficeActivity.class));
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_box_office);
        ButterKnife.bind(this);

        initializeToolbar();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String tag = "android:switcher:" + mViewPager.getId() + ":" + mViewPager.getCurrentItem();
                Fragment fragment = getSupportFragmentManager().findFragmentByTag(tag);
                if (fragment != null && fragment instanceof BoxOfficeListFragment) {
                    ((BoxOfficeListFragment) fragment).scrollToTop();
                }
            }
        });

        Fragment[] fragments = new Fragment[4];
        fragments[0] = BoxOfficeListFragment.newInstance(DAY);
        fragments[1] = BoxOfficeListFragment.newInstance(WEEKEND);
        fragments[2] = BoxOfficeListFragment.newInstance(WEEK);
        fragments[3] = BoxOfficeListFragment.newInstance(MONTH);

        TabPagerAdapter adapter = new TabPagerAdapter(getSupportFragmentManager(), fragments);
        adapter.setTabTitles(new String[]{
                getString(R.string.box_office_day), getString(R.string.box_office_weekend),
                getString(R.string.box_office_week), getString(R.string.box_office_month)
        });
        mViewPager.setOffscreenPageLimit(3);
        mViewPager.setAdapter(adapter);
        mTabLayout.setTabMode(TabLayout.MODE_FIXED);
        mTabLayout.setupWithViewPager(mViewPager);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.box_office, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_attention) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("温馨提示");
            builder.setView(R.layout.layout_box_office_attention_dialog);
            builder.setPositiveButton("好的", null);
            builder.show();

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        ApiHelper.releaseBoxOfficeApi();
    }
}
