package com.xw.project.gracefulmovies.view.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;
import android.widget.ProgressBar;

import com.xw.project.gracefulmovies.R;

import org.polaric.colorful.Colorful;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * WebViewActivity
 * <p/>
 * Created by woxingxiao on 2017-02-09.
 */
public class WebActivity extends BaseActivity {

    @BindView(R.id.web_progress_bar)
    ProgressBar mProgressBar;
    @BindView(R.id.web_container)
    FrameLayout mContainer;

    private WebView mWebView;

    public static void navigation(Activity activity, String url) {
        Intent intent = new Intent(activity, WebActivity.class);
        intent.putExtra("url", url);
        activity.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);
        ButterKnife.bind(this);

        initializeToolbar();

        mWebView = new WebView(this);
        LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        mWebView.setLayoutParams(lp);
        mContainer.addView(mWebView, 0);

        if (Colorful.getThemeDelegate().isNight()) {
            mContainer.setAlpha(0.7f);
        }

        initWebView(getIntent().getStringExtra("url"));
    }

    private void initWebView(String url) {
        WebSettings webSettings = mWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        webSettings.setUseWideViewPort(true);
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setBuiltInZoomControls(true);
        webSettings.setGeolocationEnabled(true);
        webSettings.setDomStorageEnabled(true);
        webSettings.setDatabaseEnabled(true);
        webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        webSettings.setAllowFileAccess(true);
        webSettings.setAppCacheEnabled(true);
        if (Build.VERSION.SDK_INT >= 21) {
            webSettings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.HONEYCOMB) {
            webSettings.setDisplayZoomControls(false);
        }
        mWebView.setWebChromeClient(new MyWebChromeClient());

        mWebView.loadUrl(url);
    }

    private class MyWebChromeClient extends WebChromeClient {

        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            super.onProgressChanged(view, newProgress);
            if (newProgress == 100) {
                mProgressBar.setVisibility(View.GONE);
            } else {
                if (mProgressBar.getVisibility() != View.VISIBLE)
                    mProgressBar.setVisibility(View.VISIBLE);
                mProgressBar.setProgress(newProgress);
            }
        }
    }

    @Override
    public void onBackPressed() {
        if (mWebView.canGoBack()) {
            mWebView.goBack();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        mContainer.removeAllViews();
        mWebView.removeAllViews();
        mWebView.destroy();
        mWebView = null;
    }
}
