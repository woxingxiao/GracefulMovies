package com.xw.project.gracefulmovies.data.db.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Transaction;

import com.xw.project.gracefulmovies.data.db.entity.BoxOfficeEntity;

import java.util.List;

/**
 * <p>
 * Created by woxingxiao on 2018-08-17.
 */
@Dao
public abstract class BoxOfficeDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract void insertAll(List<BoxOfficeEntity> boxOffices);

    @Query("SELECT * FROM box_offices ORDER BY id ASC")
    public abstract LiveData<List<BoxOfficeEntity>> loadBoxOfficeList();

    @Query("DELETE FROM box_offices")
    abstract void delete();

    @Transaction
    public void update(List<BoxOfficeEntity> boxOffices) {
        delete();
        insertAll(boxOffices);
    }
}
