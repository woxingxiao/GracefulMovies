package com.xw.project.gracefulmovies.view.fragment;

import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Environment;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.SwitchPreference;
import android.support.annotation.ColorRes;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.TextView;

import com.xw.project.gracefulmovies.R;
import com.xw.project.gracefulmovies.util.PrefUtil;
import com.xw.project.gracefulmovies.util.SharedPrefHelper;
import com.xw.project.gracefulmovies.view.activity.ThemeActivity;
import com.xw.repo.BubbleSeekBar;

import org.polaric.colorful.Colorful;

import java.io.File;
import java.math.BigDecimal;

/**
 * 设置
 * <p/>
 * Created by woxingxiao on 2017-02-13.
 */
public class SettingsFragment extends PreferenceFragment implements Preference.OnPreferenceChangeListener,
        Preference.OnPreferenceClickListener {

    private Preference mDayTimePref;
    private Preference mNightTimePref;
    private Preference mCachePreference;

    public static SettingsFragment newInstance() {
        return new SettingsFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.settings);

        SwitchPreference switchPref = (SwitchPreference) findPreference(getString(R.string.key_auto_day_night));
        mDayTimePref = findPreference(getString(R.string.key_day_mode_time));
        mNightTimePref = findPreference(getString(R.string.key_night_mode_time));
        mCachePreference = findPreference(getString(R.string.key_cache));

        if (!switchPref.isChecked()) {
            mDayTimePref.setEnabled(false);
            mNightTimePref.setEnabled(false);
        }
        String cache = getCacheSizeString();
        mCachePreference.setSummary(cache.equals("0.0B") ? "无缓存" : cache);
        int[] time = PrefUtil.getDayNightModeStartTime(getActivity(), true);
        int h = time[0];
        int m = time[1];
        mDayTimePref.setSummary(getString(R.string.format_hour_min2, formatTime(h), formatTime(m)));
        time = PrefUtil.getDayNightModeStartTime(getActivity(), false);
        h = time[0];
        m = time[1];
        mNightTimePref.setSummary(getString(R.string.format_hour_min2, formatTime(h), formatTime(m)));

        switchPref.setOnPreferenceChangeListener(this);

        mDayTimePref.setOnPreferenceClickListener(this);
        mNightTimePref.setOnPreferenceClickListener(this);
        findPreference(getString(R.string.key_theme)).setOnPreferenceClickListener(this);
        mCachePreference.setOnPreferenceClickListener(this);
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object obj) {
        switch (preference.getOrder()) {
            case 0:
                if (obj instanceof Boolean) {
                    mDayTimePref.setEnabled((boolean) obj);
                    mNightTimePref.setEnabled((boolean) obj);
                }
                break;
        }
        return true;
    }

    @Override
    public boolean onPreferenceClick(Preference preference) {
        switch (preference.getOrder()) {
            case 1:
                showPickTimeDialog(true);
                break;
            case 2:
                showPickTimeDialog(false);
                break;
            case 3:
                ThemeActivity.navigation(getActivity());
                break;
            case 4:
                if (!"无缓存".equals(mCachePreference.getSummary()) && clearCache()) {
                    mCachePreference.setSummary("无缓存");
                }
                break;
        }
        return true;
    }

    private void showPickTimeDialog(final boolean isDay) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        View view = getActivity().getLayoutInflater().inflate(R.layout.layout_pick_time_dialog, null);
        final TextView hourMinText = (TextView) view.findViewById(R.id.dialog_hour_min_text);
        final BubbleSeekBar hourBar = (BubbleSeekBar) view.findViewById(R.id.dialog_hour_seek_bar);
        final BubbleSeekBar minBar = (BubbleSeekBar) view.findViewById(R.id.dialog_min_seek_bar);

        int[] time = PrefUtil.getDayNightModeStartTime(getActivity(), isDay);
        int h = time[0];
        int m = time[1];
        hourMinText.setText(getString(R.string.format_hour_min, formatTime(h), formatTime(m)));
        hourBar.setProgress(h);
        minBar.setProgress(m);
        hourBar.setSecondTrackColor(resolveColor(Colorful.getThemeDelegate().getAccentColor().getColorRes()));
        minBar.setSecondTrackColor(resolveColor(Colorful.getThemeDelegate().getAccentColor().getColorRes()));
        hourBar.setThumbColor(resolveColor(Colorful.getThemeDelegate().getAccentColor().getColorRes()));
        minBar.setThumbColor(resolveColor(Colorful.getThemeDelegate().getAccentColor().getColorRes()));
        hourBar.setBubbleColor(resolveColor(Colorful.getThemeDelegate().getAccentColor().getColorRes()));
        minBar.setBubbleColor(resolveColor(Colorful.getThemeDelegate().getAccentColor().getColorRes()));
        if (Colorful.getThemeDelegate().isNight()) {
            hourBar.setTrackColor(resolveColor(android.R.color.darker_gray));
            minBar.setTrackColor(resolveColor(android.R.color.darker_gray));
            hourBar.setTextColor(resolveColor(android.R.color.white));
            minBar.setTextColor(resolveColor(android.R.color.white));
        }
        hourBar.setOnProgressChangedListener(new BubbleSeekBar.OnProgressChangedListenerAdapter() {
            @Override
            public void onProgressChanged(int progress) {
                hourMinText.setText(getString(R.string.format_hour_min, formatTime(progress),
                        formatTime(minBar.getProgress())));
            }
        });
        minBar.setOnProgressChangedListener(new BubbleSeekBar.OnProgressChangedListenerAdapter() {
            @Override
            public void onProgressChanged(int progress) {
                hourMinText.setText(getString(R.string.format_hour_min,
                        formatTime(hourBar.getProgress()), formatTime(progress)));
            }
        });
        builder.setView(view);
        builder.setTitle(isDay ? "日间模式开始时间" : "夜间模式开始时间");
        builder.setNegativeButton("取消", null);
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String s = formatTime(hourBar.getProgress()) + ":" + formatTime(minBar.getProgress());
                if (isDay) {
                    SharedPrefHelper.putString(getString(R.string.key_day_mode_time), s);
                    mDayTimePref.setSummary(s);
                } else {
                    SharedPrefHelper.putString(getString(R.string.key_night_mode_time), s);
                    mNightTimePref.setSummary(s);
                }
            }
        });
        builder.show();
    }

    private String formatTime(int time) {
        return time < 10 ? "0" + time : "" + time;
    }

    private int resolveColor(@ColorRes int res) {
        return ContextCompat.getColor(getActivity(), res);
    }

    private String getCacheSizeString() {
        long size = 0;
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            if (getActivity().getExternalCacheDir() != null) {
                size = getFolderSize(new File(getActivity().getExternalCacheDir().getAbsolutePath()));
            }
        } else {
            if (getActivity().getCacheDir() != null) {
                size = getFolderSize(new File(getActivity().getCacheDir().getAbsolutePath()));
            }
        }

        return formatFileSize(size);
    }

    private boolean clearCache() {
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            if (getActivity().getExternalCacheDir() != null) {
                return deleteDir(new File(getActivity().getExternalCacheDir().getAbsolutePath()));
            }
        } else {
            return deleteDir(new File(getActivity().getCacheDir().getAbsolutePath()));
        }
        return true;
    }

    private long getFolderSize(File file) {
        if (!file.exists()) {
            return 0;
        }

        long size = 0;
        try {
            File[] fileList = file.listFiles();
            for (File aFileList : fileList) {
                if (aFileList.isDirectory()) {
                    size = size + getFolderSize(aFileList);
                } else {
                    size = size + aFileList.length();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return size;
    }

    private String formatFileSize(double size) {
        double kByte = size / 1024;
        if (kByte < 1) {
            return size + "B";
        }
        double mByte = kByte / 1024;
        if (mByte < 1) {
            BigDecimal result1 = new BigDecimal(Double.toString(kByte));
            return result1.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + "KB";
        }
        double gByte = mByte / 1024;
        if (gByte < 1) {
            BigDecimal result2 = new BigDecimal(Double.toString(mByte));
            return result2.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + "MB";
        }
        double tByte = gByte / 1024;
        if (tByte < 1) {
            BigDecimal result3 = new BigDecimal(Double.toString(gByte));
            return result3.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + "GB";
        }
        BigDecimal result4 = new BigDecimal(tByte);
        return result4.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + "TB";
    }

    private boolean deleteDir(File dir) {
        if (dir == null || !dir.exists())
            return false;

        if (dir.isDirectory()) {
            String[] children = dir.list();
            for (String aChildren : children) {
                boolean success = deleteDir(new File(dir, aChildren));
                if (!success) {
                    return false;
                }
            }
        }
        return dir.delete();
    }
}
