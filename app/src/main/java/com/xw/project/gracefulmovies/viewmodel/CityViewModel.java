package com.xw.project.gracefulmovies.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import com.xw.project.gracefulmovies.GMApplication;
import com.xw.project.gracefulmovies.data.db.entity.CityEntity;

/**
 * <p>
 * Created by woxingxiao on 2018-08-12.
 */
public class CityViewModel extends ViewModel {

    private final LiveData<CityEntity> mCity;

    public CityViewModel() {
        mCity = GMApplication.getInstance().getCityRepository().getCity();
    }

    public LiveData<CityEntity> getCity() {
        return mCity;
    }
}
