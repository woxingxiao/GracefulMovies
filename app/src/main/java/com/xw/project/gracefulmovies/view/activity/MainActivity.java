package com.xw.project.gracefulmovies.view.activity;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.SwitchCompat;
import android.support.v7.widget.Toolbar;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.xw.project.gracefulmovies.R;
import com.xw.project.gracefulmovies.presenter.IMainActivityPresenter;
import com.xw.project.gracefulmovies.presenter.impl.MainActivityPresenterImpl;
import com.xw.project.gracefulmovies.util.PrefUtil;
import com.xw.project.gracefulmovies.util.Util;
import com.xw.project.gracefulmovies.view.adapter.TabPagerAdapter;
import com.xw.project.gracefulmovies.view.fragment.MovieListFragment;
import com.xw.project.gracefulmovies.view.iview.IMainActivity;
import com.xw.project.gracefulmovies.view.service.LocationService;
import com.xw.project.gracefulmovies.view.widget.UnScrollableViewPager;

import org.polaric.colorful.Colorful;

import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 首页
 * <p/>
 * Created by woxingxiao on 2017-01-25.
 */
public class MainActivity extends CheckPermissionsActivity implements NavigationView
        .OnNavigationItemSelectedListener, IMainActivity {

    @BindView(R.id.drawer_layout)
    DrawerLayout mDrawerLayout;
    @BindView(R.id.view_pager)
    UnScrollableViewPager mViewPager;
    @BindView(R.id.radio_group)
    RadioGroup mRadioGroup;
    @BindView(R.id.now_radio)
    RadioButton mNowBtn;
    @BindView(R.id.coming_radio)
    RadioButton mComingBtn;

    private SwitchCompat mSwitch;
    private TextView mCityText;

    private MyReceiver mReceiver;
    private String mAutoSwitchedHint;

    private IMainActivityPresenter mPresenter;

    public static void navigation(Activity activity) {
        activity.startActivity(new Intent(activity, MainActivity.class));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        transparentStatusBar();
        if (Build.VERSION.SDK_INT == Build.VERSION_CODES.KITKAT) {
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

        getPresenter().loadMovieData();

        mReceiver = new MyReceiver();
        LocalBroadcastManager.getInstance(this).registerReceiver(mReceiver, new IntentFilter(getString(R.string.action_locate_succeed)));

        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        int resId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        int statusBarHeight = getResources().getDimensionPixelSize(resId);

        FrameLayout.LayoutParams lp1 = (FrameLayout.LayoutParams) toolbar.getLayoutParams();
        lp1.topMargin = statusBarHeight;
        toolbar.setLayoutParams(lp1);

        FrameLayout.LayoutParams lp2 = (FrameLayout.LayoutParams) mRadioGroup.getLayoutParams();
        lp2.topMargin = Util.dp2px(56) + statusBarHeight;
        mRadioGroup.setLayoutParams(lp2);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, mDrawerLayout, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mDrawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navView = (NavigationView) findViewById(R.id.nav_view);
        navView.setNavigationItemSelectedListener(this);
        mSwitch = (SwitchCompat) navView.getHeaderView(0).findViewById(R.id.day_night_mode_switch);
        mCityText = (TextView) navView.getHeaderView(0).findViewById(R.id.nav_city_text);
        ImageView img = (ImageView) navView.getHeaderView(0).findViewById(R.id.nav_header_img);
        Glide.with(this).load(R.drawable.pic_movies).into(img);

        MovieListFragment[] fragments = new MovieListFragment[2];
        fragments[0] = MovieListFragment.newInstance(0);
        fragments[1] = MovieListFragment.newInstance(1);

        TabPagerAdapter adapter = new TabPagerAdapter(getSupportFragmentManager(), fragments);
        adapter.setTabTitles(new String[]{getString(R.string.has_released), getString(R.string.going_to_release)});
        mViewPager.setAdapter(adapter);

        mRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                int position = checkedId == R.id.now_radio ? 0 : 1;
                if (mViewPager.getCurrentItem() == position)
                    return;

                mViewPager.setCurrentItem(position);
            }
        });
        String font = "font.ttf";
        Typeface typeface = Typeface.createFromAsset(getAssets(), font);
        mNowBtn.setTypeface(typeface);
        mComingBtn.setTypeface(typeface);

        mCityText.setText(PrefUtil.getCityShort());
        mCityText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showLocatedCityDialog(false, false);
            }
        });

        if (savedInstanceState == null) {
            LocationService.start(this);

            checkAutoDayNightMode();
        } else {
            String hint = savedInstanceState.getString("hint", null);
            if (hint != null) {
                Snackbar.make(mDrawerLayout, hint, 2000)
                        .setAction(getString(R.string.revoke), new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                PrefUtil.setAutoDayNightMode(false);
                                switchDayNightModeSmoothly(!Colorful.getThemeDelegate().isNight(), false);
                            }
                        })
                        .show();
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        mSwitch.setChecked(Colorful.getThemeDelegate().isNight());
        mSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                mDrawerLayout.addDrawerListener(new MyDrawerListener());
                mDrawerLayout.closeDrawer(GravityCompat.START);

                if (PrefUtil.isAutoDayNightMode()) {
                    Toast.makeText(MainActivity.this, getString(R.string.hint_auto_day_night_disabled),
                            Toast.LENGTH_LONG).show();
                    PrefUtil.setAutoDayNightMode(false);
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        final MenuItem item1 = menu.findItem(R.id.action_search);
        item1.getActionView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onOptionsItemSelected(item1);
            }
        });
        final MenuItem item2 = menu.findItem(R.id.action_box_office);
        item2.getActionView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onOptionsItemSelected(item2);
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_search) {
            navigateWithRippleCompat(
                    this,
                    new Intent(this, SearchActivity.class),
                    item.getActionView(),
                    Colorful.getThemeDelegate().getPrimaryColor().getColorRes()
            );
            return true;
        } else if (item.getItemId() == R.id.action_box_office) {
            navigateWithRippleCompat(
                    this,
                    new Intent(this, BoxOfficeActivity.class),
                    item.getActionView(),
                    Colorful.getThemeDelegate().getPrimaryColor().getColorRes()
            );
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_theme:
                navigateTo(ThemeActivity.class);
                break;
            case R.id.nav_settings:
                navigateTo(SettingsActivity.class);
                break;
            case R.id.nav_share: // 分享
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_SUBJECT, "分享");
                intent.putExtra(Intent.EXTRA_TEXT, getString(R.string.share_out_description));
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(Intent.createChooser(intent, "分享"));
                break;
            case R.id.nav_evaluate: // 评分
                Uri uri = Uri.parse("market://details?id=" + getPackageName());
                Intent intent2 = new Intent(Intent.ACTION_VIEW, uri);
                intent2.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(Intent.createChooser(intent2, "评价"));
                break;
            case R.id.nav_about:
                navigateTo(AboutActivity.class);
                break;
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    /**
     * 检测是否自动日夜模式，如果是自动将根据时间判断是否切换
     */
    private void checkAutoDayNightMode() {
        boolean firstTime = PrefUtil.checkFirstTime();
        if (firstTime)
            PrefUtil.setNotFirstTime();
        boolean auto = PrefUtil.isAutoDayNightMode();
        if (firstTime || !auto)
            return;

        int[] dayTime = PrefUtil.getDayNightModeStartTime(true);
        int[] nightTime = PrefUtil.getDayNightModeStartTime(false);
        Calendar cal = Calendar.getInstance();
        int h = cal.get(Calendar.HOUR_OF_DAY);
        int m = cal.get(Calendar.MINUTE);
        if (Colorful.getThemeDelegate().isNight()) {
            if ((dayTime[0] < h && h < nightTime[0]) || (dayTime[0] == h && dayTime[1] <= m)) {
                switchDayNightModeSmoothly(false, true);
            }
        } else {
            if ((nightTime[0] < h) || (nightTime[0] == h && nightTime[1] <= m)) {
                switchDayNightModeSmoothly(true, true);
            }
        }
    }

    private void switchDayNightModeSmoothly(final boolean isDark, boolean delay) {
        if (delay) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Colorful.config(MainActivity.this).night(isDark).apply();
                    getWindow().setWindowAnimations(R.style.WindowAnimationFadeInOut);
                    mAutoSwitchedHint = "已自动切换为" +
                            (isDark ? getString(R.string.night_mode) : getString(R.string.day_mode));
                    recreate();
                }
            }, 1000);
        } else {
            Colorful.config(MainActivity.this).night(isDark).apply();
            getWindow().setWindowAnimations(R.style.WindowAnimationFadeInOut);
            recreate();
        }
    }

    @Override
    protected void onAllPermissionsGranted() {
        LocationService.start(this);
    }

    @Override
    public IMainActivityPresenter getPresenter() {
        if (mPresenter == null) {
            mPresenter = new MainActivityPresenterImpl(this);
        }
        return mPresenter;
    }

    @Override
    public void onFragmentRefreshDataReady(int fragmentId) {
        String tag = "android:switcher:" + mViewPager.getId() + ":" + fragmentId;
        Fragment fragment = getSupportFragmentManager().findFragmentByTag(tag);
        if (fragment != null && fragment instanceof MovieListFragment) {
            ((MovieListFragment) fragment).onDataReady(mPresenter.getMovieModels(fragmentId));
        }
    }

    @Override
    public void onDataError(int code, String msg) {
        if (code == 209405) { // "查询不到热映电影相关信息"，以上一级城市名进行查询
            showLocatedCityDialog(false, true);
            return;
        }

        List<Fragment> fragments = getSupportFragmentManager().getFragments();
        if (fragments != null) {
            for (Fragment fragment : fragments) {
                if (fragment != null && fragment instanceof MovieListFragment) {
                    ((MovieListFragment) fragment).onDataError();
                }
            }
        }
        showToast(msg);
    }

    long mStartMills;

    @Override
    public void onBackPressed() {
        if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
        } else {
            if (System.currentTimeMillis() - mStartMills > 1000) {
                showToast("再按一次，退出程序");
                mStartMills = System.currentTimeMillis();
            } else {
                super.onBackPressed();
            }
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("hint", mAutoSwitchedHint);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        mPresenter.unregister();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mReceiver);
    }

    private class MyDrawerListener implements DrawerLayout.DrawerListener {

        @Override
        public void onDrawerClosed(View drawerView) {
            switchDayNightModeSmoothly(mSwitch.isChecked(), false);
        }

        @Override
        public void onDrawerSlide(View drawerView, float slideOffset) {
        }

        @Override
        public void onDrawerOpened(View drawerView) {
        }

        @Override
        public void onDrawerStateChanged(int newState) {
        }
    }

    /**
     * 监听定位完成广播
     */
    private class MyReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (getString(R.string.action_locate_succeed).equals(intent.getAction())) {
                mCityText.setText(PrefUtil.getCityShort());

                showLocatedCityDialog(true, intent.getBooleanExtra(getString(R.string.param_is_upper_city), false));
            }
        }
    }

    private void showLocatedCityDialog(final boolean refresh, final boolean upperCity) {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle(getString(R.string.location_default));
        final View view = getLayoutInflater().inflate(R.layout.layout_location_dialog, null);
        final TextView msgText = (TextView) view.findViewById(R.id.dialog_loc_msg_text);
        final TextView manualText = (TextView) view.findViewById(R.id.dialog_loc_input_manually_text);
        final View inputLayout = view.findViewById(R.id.dialog_loc_input_layout);
        final EditText inputEdit = (EditText) view.findViewById(R.id.dialog_loc_edit);

        if (upperCity) {
            msgText.setText(getString(R.string.hint_query_by_upper_city, PrefUtil.getCity(),
                    PrefUtil.getUpperCity()));
        } else {
            msgText.setText(getString(R.string.hint_located_city, PrefUtil.getCity()));
        }
        String hint = getString(R.string.hint_input_city_manually);
        SpannableStringBuilder span = new SpannableStringBuilder(hint);
        span.setSpan(new ForegroundColorSpan(
                        ContextCompat.getColor(this, Colorful.getThemeDelegate().getAccentColor().getColorRes())),
                hint.length() - 4, hint.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        manualText.setText(span);
        manualText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (inputLayout.getVisibility() != View.VISIBLE)
                    inputLayout.setVisibility(View.VISIBLE);
            }
        });

        builder.setView(view);
        builder.setNegativeButton(getString(R.string.locate_again), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                PrefUtil.clearCity();
                LocationService.start(MainActivity.this);
            }
        });
        builder.setPositiveButton(getString(R.string.confirm), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (inputLayout.getVisibility() == View.VISIBLE) {
                    if (inputEdit.getText().toString().trim().isEmpty()) {
                        showToast(getString(R.string.hint_distract_input_empty));
                    } else {
                        PrefUtil.setInputtedCity(true);
                        PrefUtil.setCity(inputEdit.getText().toString().trim());
                        mCityText.setText(PrefUtil.getCityShort());
                        getPresenter().loadMovieData();
                    }
                    return;
                }

                if (upperCity) {
                    PrefUtil.setCity(PrefUtil.getUpperCity());
                    mCityText.setText(PrefUtil.getCityShort());
                }
                List<Fragment> fragments = getSupportFragmentManager().getFragments();
                if (fragments != null) {
                    for (Fragment fragment : fragments) {
                        if (fragment != null && fragment instanceof MovieListFragment) {
                            if (refresh) {
                                ((MovieListFragment) fragment).onRefresh();
                            }
                            if (upperCity) {
                                ((MovieListFragment) fragment).onDataError();
                            }
                        }
                    }
                }
            }
        });
        builder.show();
    }
}
