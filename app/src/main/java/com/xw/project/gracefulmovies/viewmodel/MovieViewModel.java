package com.xw.project.gracefulmovies.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Transformations;

import com.xw.project.gracefulmovies.data.DataResource;
import com.xw.project.gracefulmovies.data.db.entity.CityEntity;
import com.xw.project.gracefulmovies.data.db.entity.MovieEntity;
import com.xw.project.gracefulmovies.repository.MovieRepository;
import com.xw.project.gracefulmovies.viewmodel.base.BaseViewModel;

import java.util.List;

/**
 * <p>
 * Created by woxingxioa on 2018-08-09.
 */
public class MovieViewModel extends BaseViewModel {

    private final MutableLiveData<CityEntity> mCity = new MutableLiveData<>();

    public void setCity(CityEntity city) {
        if (mCity.getValue() == null || mCity.getValue().getId() != city.getId()) {
            mCity.setValue(city);
        }
    }

    public LiveData<DataResource<List<MovieEntity>>> getMovieList(boolean now) {
        return Transformations.switchMap(mCity, input ->
                Transformations.switchMap(getLoadLive(), aBoolean -> {
                            if (now) {
                                return MovieRepository.getInstance().getMovieNowList(input.getId());
                            } else {
                                return MovieRepository.getInstance().getMovieFutureList(input.getId());
                            }
                        }
                )
        );
    }
}
