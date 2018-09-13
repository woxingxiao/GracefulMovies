package com.xw.project.gracefulmovies.ui.fragment;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.widget.Toast;

import com.tencent.bugly.beta.Beta;
import com.xw.project.gracefulmovies.R;

import java.io.File;
import java.math.BigDecimal;
import java.util.Locale;

/**
 * 设置
 * <p>
 * Created by woxingxiao on 2018-08-18.
 */
public class SettingsFragment extends PreferenceFragment implements Preference.OnPreferenceClickListener {

    private Preference mCachePreference;

    public static SettingsFragment newInstance() {
        return new SettingsFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.settings);

        mCachePreference = findPreference(getString(R.string.key_cache));
        Preference updatePreference = findPreference(getString(R.string.key_update));

        String cache = getCacheSizeString();
        mCachePreference.setSummary(cache.equals("0.0B") ? "无缓存" : cache);
        try {
            PackageManager manager = getActivity().getPackageManager();
            PackageInfo info = manager.getPackageInfo(getActivity().getPackageName(), 0);
            String s = String.format(Locale.CHINA, "当前版本：%s（Build %d）", info.versionName, info.versionCode);
            updatePreference.setSummary(s);
        } catch (Exception e) {
            e.printStackTrace();
            updatePreference.setSummary("");
        }

        mCachePreference.setOnPreferenceClickListener(this);
        updatePreference.setOnPreferenceClickListener(this);
        findPreference(getString(R.string.key_feedback)).setOnPreferenceClickListener(this);
    }

    @Override
    public boolean onPreferenceClick(Preference preference) {
        switch (preference.getOrder()) {
            case 0:
                if (!"无缓存".equals(mCachePreference.getSummary().toString()) && clearCache()) {
                    mCachePreference.setSummary("无缓存");
                    Toast.makeText(getActivity(), "已清除", Toast.LENGTH_SHORT).show();
                }
                break;
            case 1:
                Beta.checkUpgrade(true, false);
                break;
            case 2:
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.setData(Uri.parse(getString(R.string.questionnaire_url)));
                startActivity(Intent.createChooser(intent, "请选择浏览器"));
                break;
        }
        return true;
    }

    private String getCacheSizeString() {
        long size = getFolderSize(new File(getActivity().getCacheDir().getAbsolutePath()));
        return formatFileSize(size);
    }

    private boolean clearCache() {
        return deleteDir(new File(getActivity().getCacheDir().getAbsolutePath()));
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
