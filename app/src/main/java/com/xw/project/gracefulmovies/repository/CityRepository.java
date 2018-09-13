package com.xw.project.gracefulmovies.repository;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;

import com.xw.project.gracefulmovies.GMApplication;
import com.xw.project.gracefulmovies.data.db.entity.CityEntity;
import com.xw.project.gracefulmovies.rx.RxSchedulers;
import com.xw.project.gracefulmovies.rx.SimpleConsumer;

import io.reactivex.Observable;

/**
 * <p>
 * Created by woxignxiao on 2018-08-12.
 */
public class CityRepository {

    private final MutableLiveData<CityEntity> mUpperCity = new MutableLiveData<>();
    private final MutableLiveData<CityEntity> mCity = new MutableLiveData<>();

    public void init() {
        Observable.just("")
                .compose(RxSchedulers.applyIO())
                .subscribe(new SimpleConsumer<String>() {
                    @Override
                    public void accept(String it) {
                        LiveData<CityEntity> upperCity = GMApplication.getInstance().getDatabase().cityDao().loadUpperCity();
                        if (upperCity.getValue() == null) {
                            CityEntity entity = new CityEntity();
                            entity.setId(880);
                            entity.setName("成都");

                            updateCity(entity);
                        }
                        LiveData<CityEntity> city = GMApplication.getInstance().getDatabase().cityDao().loadCity();
                        if (city.getValue() == null) {
                            CityEntity entity = new CityEntity();
                            entity.setId(880);
                            entity.setName("成都");

                            updateCity(entity);
                        }
                    }
                });
    }

    public LiveData<CityEntity> getUpperCity() {
        return mUpperCity;
    }

    public void updateUpperCity(CityEntity city) {
        mUpperCity.postValue(city);

        Observable.just("")
                .compose(RxSchedulers.applyIO())
                .subscribe(new SimpleConsumer<String>() {
                    @Override
                    public void accept(String it) {
                        GMApplication.getInstance().getDatabase().cityDao().updateCity(city);
                    }
                });
    }

    public LiveData<CityEntity> getCity() {
        return mCity;
    }

    public void updateCity(CityEntity city) {
        mCity.postValue(city);

        Observable.just("")
                .compose(RxSchedulers.applyIO())
                .subscribe(new SimpleConsumer<String>() {
                    @Override
                    public void accept(String it) {
                        GMApplication.getInstance().getDatabase().cityDao().updateCity(city);
                    }
                });
    }

    public CityEntity getUpperCityEntity() {
        if (mUpperCity.getValue() != null) {
            return mUpperCity.getValue();
        }
        return null;
    }

    public CityEntity getCityEntity() {
        if (mCity.getValue() != null) {
            return mCity.getValue();
        }
        return null;
    }
}
