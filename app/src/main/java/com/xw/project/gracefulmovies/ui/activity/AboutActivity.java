package com.xw.project.gracefulmovies.ui.activity;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.text.Html;
import android.view.View;

import com.xw.project.gracefulmovies.R;
import com.xw.project.gracefulmovies.databinding.ActivityAboutBinding;
import com.xw.project.gracefulmovies.ui.activity.base.BaseActivity;

import java.util.Locale;

/**
 * 关于
 * <p>
 * Created by woxingxiao on 2018-08-24.
 */
public class AboutActivity extends BaseActivity<ActivityAboutBinding> {

    @Override
    protected int contentLayoutRes() {
        return R.layout.activity_about;
    }

    @Override
    protected void afterSetContentView() {
        transparentStatusBar();

        initializeToolbar();
        try {
            PackageManager manager = getPackageManager();
            PackageInfo info = manager.getPackageInfo(getPackageName(), 0);
            String s = String.format(Locale.CHINA, "版本：%s（Build %d）", info.versionName, info.versionCode);
            mBinding.versionNameTv.setText(s);
        } catch (Exception e) {
            e.printStackTrace();
            mBinding.versionNameTv.setText("");
        }
        mBinding.includedLayout.licenseTv.setText(Html.fromHtml(getString(R.string.license)));
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.mail_iv:
                String s1 = "邮箱";
                String s2 = getString(R.string.gmail_address);

                ClipboardManager cmb = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clipData = ClipData.newPlainText("content", s2);
                if (cmb != null)
                    cmb.setPrimaryClip(clipData);

                toast(getString(R.string.hint_clipboard, s1, s2));
                break;
            case R.id.github_iv:
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.setData(Uri.parse(getString(R.string.github_page)));
                startActivity(Intent.createChooser(intent, "请选择浏览器"));
                break;
            case R.id.license_tv:
                navigate(OpenLicenseActivity.class);
                break;
        }
    }
}
