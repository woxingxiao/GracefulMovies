package com.xw.project.gracefulmovies.view.activity;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.text.Html;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.xw.project.gracefulmovies.R;

import org.polaric.colorful.Colorful;

import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 关于
 * <p/>
 * Created by woxingxiao on 2017-02-10.
 */
public class AboutActivity extends BaseActivity {

    @BindView(R.id.version_name_text)
    TextView mVersionNameText;
    @BindView(R.id.about_github_img)
    ImageView mGitHubImg;
    @BindView(R.id.about_license_text)
    TextView mLicenseText;

    public static void navigation(Activity activity) {
        activity.startActivity(new Intent(activity, AboutActivity.class));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        transparentStatusBar();

        setContentView(R.layout.activity_about);
        ButterKnife.bind(this);

        initializeToolbar();

        try {
            PackageManager manager = getPackageManager();
            PackageInfo info = manager.getPackageInfo(getPackageName(), 0);
            String s = String.format(Locale.CHINA, "版本：%s（Build %d）", info.versionName, info.versionCode);
            mVersionNameText.setText(s);
        } catch (Exception e) {
            e.printStackTrace();
            mVersionNameText.setText("");
        }
        if (Colorful.getThemeDelegate().isNight()) {
            Drawable drawable = mGitHubImg.getDrawable();
            if (drawable != null) {
                DrawableCompat.setTint(drawable, Color.WHITE);
                mGitHubImg.setImageDrawable(drawable);
            }
        }
        mLicenseText.setText(Html.fromHtml(getString(R.string.license)));
    }

    @OnClick({R.id.about_weibo_img, R.id.about_wechat_img, R.id.about_qq_img, R.id.about_gmail_img,
            R.id.about_github_img, R.id.about_alipay_img, R.id.about_wechat_pay_img,
            R.id.about_license_text})
    public void onClick(View view) {
        if (view.getId() == R.id.about_github_img) {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setData(Uri.parse(getString(R.string.github_page)));
            startActivity(Intent.createChooser(intent, "请选择浏览器"));
            return;
        }
        if (view.getId() == R.id.about_license_text) {
            OpenLicenseActivity.navigation(this);
            return;
        }

        String s1 = "";
        String s2 = "";
        switch (view.getId()) {
            case R.id.about_weibo_img:
                s1 = "微博昵称";
                s2 = getString(R.string.weibo_name);
                break;
            case R.id.about_wechat_img:
                s1 = "微信号";
                s2 = getString(R.string.wechat_number);
                break;
            case R.id.about_qq_img:
                s1 = "QQ号";
                s2 = getString(R.string.qq_number);
                break;
            case R.id.about_gmail_img:
                s1 = "邮箱";
                s2 = getString(R.string.gmail_address);
                break;
            case R.id.about_alipay_img:
                s1 = "支付宝账户";
                s2 = getString(R.string.alipay_name);
                break;
            case R.id.about_wechat_pay_img:
                s1 = "微信号";
                s2 = getString(R.string.wechat_number);
                break;
        }

        ClipboardManager cmb = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clipData = ClipData.newPlainText("content", s2);
        cmb.setPrimaryClip(clipData);

        showToast(getString(R.string.hint_clipboard, s1, s2));
    }
}
