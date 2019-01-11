package com.xw.project.gracefulmovies.ui.activity;

import android.Manifest;
import android.arch.lifecycle.LiveData;
import android.content.DialogInterface;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.LinearInterpolator;

import com.xw.project.gracefulmovies.GMApplication;
import com.xw.project.gracefulmovies.R;
import com.xw.project.gracefulmovies.data.db.dao.CityDao;
import com.xw.project.gracefulmovies.data.db.entity.CityEntity;
import com.xw.project.gracefulmovies.databinding.ActivitySplashBinding;
import com.xw.project.gracefulmovies.rx.RxSchedulers;
import com.xw.project.gracefulmovies.rx.SimpleConsumer;
import com.xw.project.gracefulmovies.ui.activity.base.BaseActivity;
import com.yanzhenjie.permission.AndPermission;

import io.reactivex.Observable;

/**
 * 启动页
 * <p>
 * Created by woxingxiao on 2018-09-03.
 */
public class SplashActivity extends AppCompatActivity {

    private ActivitySplashBinding mBinding;
    private Handler mHandler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_splash);

        mBinding.splashBgIv.animate()
                .scaleXBy(0.1f)
                .scaleYBy(0.1f)
                .setDuration(4000)
                .setInterpolator(new LinearInterpolator())
                .start();
        mBinding.getRoot().addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View view, int i, int i1, int i2, int i3, int i4, int i5, int i6, int i7) {
                mBinding.getRoot().removeOnLayoutChangeListener(this);

                mHandler.postDelayed(SplashActivity.this::requestPermission, 2000);
            }
        });

        CityDao dao = GMApplication.getInstance().getDatabase().cityDao();
        LiveData<CityEntity> liveData = dao.loadCity();
        liveData.observe(this, cityEntity -> {
            liveData.removeObservers(this);

            if (cityEntity == null) {
                cityEntity = GMApplication.getInstance().getCityRepository().genDefaultCity();
                Observable.just(cityEntity)
                        .compose(RxSchedulers.applyIO())
                        .subscribe(new SimpleConsumer<CityEntity>() {
                            @Override
                            public void accept(CityEntity it) {
                                dao.insert(it);
                            }
                        });
            }
        });
    }

    private void requestPermission() {
        AndPermission.with(this)
                .runtime()
                .permission(Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.READ_PHONE_STATE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .onGranted(permissions -> jumpToMain())
                .onDenied(permissions -> {
                    if (AndPermission.hasAlwaysDeniedPermission(this, permissions)) {
                        AlertDialog dialog = new AlertDialog.Builder(this).create();
                        dialog.setCancelable(false);
                        dialog.setButton(DialogInterface.BUTTON_POSITIVE, "去设置", (dialog13, which) -> {
                            AndPermission.with(this)
                                    .runtime()
                                    .setting()
                                    .onComeback(this::requestPermission)
                                    .start();
                        });
                        if (!AndPermission.hasPermissions(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
                            dialog.setTitle("定位权限");
                            dialog.setMessage("为了准确获取所在地的数据，请授予定位权限！\n设置：权限管理->定位");
                            dialog.show();

                            return;
                        }
                        if (!AndPermission.hasPermissions(this, Manifest.permission.READ_PHONE_STATE)) {
                            dialog.setTitle("手机状态权限");
                            dialog.setMessage("为保证应用正常工作，请授予手机状态权限！\n设置：权限管理->手机");
                            dialog.show();

                            return;
                        }
                        if (!AndPermission.hasPermissions(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                            dialog.setTitle("外部存储权限");
                            dialog.setMessage("为保证应用正常工作，请授予外部存储权限！\n设置：权限管理->存储");
                            dialog.show();
                        }
                    } else {
                        requestPermission();
                    }
                })
                .rationale((context, permissions, executor) -> {
                    AlertDialog dialog = new AlertDialog.Builder(this).create();
                    dialog.setButton(DialogInterface.BUTTON_POSITIVE, "授权", (dialog1, which) ->
                            executor.execute()
                    );
                    dialog.setCancelable(false);
                    if (!AndPermission.hasPermissions(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
                        dialog.setTitle("定位权限");
                        dialog.setMessage("为了准确获取所在地的数据，请授予定位权限！");
                        dialog.show();

                        return;
                    }
                    if (!AndPermission.hasPermissions(this, Manifest.permission.READ_PHONE_STATE)) {
                        dialog.setTitle("手机状态权限");
                        dialog.setMessage("为保证应用正常工作，请授予手机状态权限！");
                        dialog.show();

                        return;
                    }
                    if (!AndPermission.hasPermissions(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                        dialog.setTitle("外部存储权限");
                        dialog.setMessage("为保证应用正常工作，请授予外部存储权限！");
                        dialog.show();
                    }
                })
                .start();
    }

    private void jumpToMain() {
        mHandler.removeCallbacksAndMessages(null);
        mBinding.splashBgIv.animate().cancel();

        BaseActivity.navigate(SplashActivity.this, MainActivity.class);
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        finish();
    }
}
