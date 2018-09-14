package com.xw.project.gracefulmovies.data.db.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Transaction;

import com.xw.project.gracefulmovies.data.db.entity.MovieEntity;

import java.util.List;

/**
 * <p>
 * Created by woxingxiao on 2018-08-08.
 */
@Dao
public abstract class MovieDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract void insertAll(List<MovieEntity> movies);

    @Query("SELECT * FROM movies WHERE isNow = 1 ORDER BY rank ASC")
    public abstract LiveData<List<MovieEntity>> loadMovieNowList();

    @Query("SELECT * FROM movies WHERE isNow = 0 ORDER BY rank ASC")
    public abstract LiveData<List<MovieEntity>> loadMovieFutureList();

    @Query("DELETE FROM movies WHERE isNow = 1")
    public abstract void deleteNow();

    @Query("DELETE FROM movies WHERE isNow = 0")
    public abstract void deleteFuture();

    @Transaction
    public void updateMovieNowList(List<MovieEntity> movies) {
        deleteNow();
        insertAll(movies);
    }

    @Transaction
    public void updateMovieFutureList(List<MovieEntity> movies) {
        deleteFuture();
        insertAll(movies);
    }
}
