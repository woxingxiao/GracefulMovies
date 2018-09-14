package com.xw.project.gracefulmovies.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Transformations;

import com.xw.project.gracefulmovies.data.DataResource;
import com.xw.project.gracefulmovies.data.db.entity.BoxOfficeEntity;
import com.xw.project.gracefulmovies.repository.BoxOfficeRepository;
import com.xw.project.gracefulmovies.viewmodel.base.BaseViewModel;

import java.util.List;

/**
 * <p>
 * Created by woxingxiao on 2018-08-17.
 */
public class BoxOfficeViewModel extends BaseViewModel {

    private final LiveData<DataResource<List<BoxOfficeEntity>>> mBoxOffices;
    private BoxOfficeRepository mRepository = new BoxOfficeRepository();

    public BoxOfficeViewModel() {
        mBoxOffices = Transformations.switchMap(getLoadLive(), input -> mRepository.getBoxOffices());
    }

    public LiveData<DataResource<List<BoxOfficeEntity>>> getBoxOffices() {
        return mBoxOffices;
    }
}
