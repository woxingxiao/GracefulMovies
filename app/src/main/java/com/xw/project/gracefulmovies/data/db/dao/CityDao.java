package com.xw.project.gracefulmovies.data.db.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Transaction;

import com.xw.project.gracefulmovies.data.db.entity.CityEntity;

/**
 * <p>
 * Created by woxingxiao on 2018-08-08.
 */
@Dao
public abstract class CityDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract void insert(CityEntity city);

    @Query("SELECT * FROM cities WHERE isUpper = 0 LIMIT 1")
    public abstract LiveData<CityEntity> loadCity();

    @Query("DELETE FROM cities")
    public abstract void delete();

    @Transaction
    public void updateCity(CityEntity city) {
        delete();
        insert(city);
    }
}
