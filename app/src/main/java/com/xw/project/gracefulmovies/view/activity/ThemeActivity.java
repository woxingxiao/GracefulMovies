package com.xw.project.gracefulmovies.view.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.MenuItem;

import com.xw.project.gracefulmovies.R;
import com.xw.project.gracefulmovies.view.fragment.DayModeFragment;
import com.xw.project.gracefulmovies.view.fragment.NightModeFragment;

import org.polaric.colorful.Colorful;

import java.util.List;

/**
 * 主题选择
 * <p/>
 * Created by XiaoWei on 2017-01-30.
 */
public class ThemeActivity extends BaseActivity {

    public static void navigation(Activity activity) {
        activity.startActivity(new Intent(activity, ThemeActivity.class));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        transparentStatusBar();

        setContentView(R.layout.activity_theme);

        if (savedInstanceState == null) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            if (Colorful.getThemeDelegate().isNight()) {
                ft.add(R.id.container, NightModeFragment.newInstance(), NightModeFragment.TAG);
            } else {
                ft.add(R.id.container, DayModeFragment.newInstance(), DayModeFragment.TAG);
            }
            ft.commit();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    public void switchModeFragment(int cx, int cy, boolean appBarExpanded, String which) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        Fragment fragment;
        switch (which) {
            case DayModeFragment.TAG:
                fragment = DayModeFragment.newInstance(cx, cy, appBarExpanded);
                break;
            case NightModeFragment.TAG:
                fragment = NightModeFragment.newInstance(cx, cy, appBarExpanded);
                break;
            default:
                throw new IllegalStateException();
        }
        ft.add(R.id.container, fragment, which).commit();
    }

    public void removeAllFragmentExcept(String tag) {
        List<Fragment> frags = getSupportFragmentManager().getFragments();
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        Fragment frag;
        for (int i = 0; i < frags.size(); i++) {
            frag = frags.get(i);
            if (frag == null) {
                continue;
            }
            if (tag == null || !tag.equals(frag.getTag())) {
                ft.remove(frag);
            }
        }
        ft.commit();
    }
}
