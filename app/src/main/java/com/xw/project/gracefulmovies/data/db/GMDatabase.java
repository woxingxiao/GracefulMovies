package com.xw.project.gracefulmovies.data.db;

import android.app.Application;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;
import android.support.annotation.WorkerThread;

import com.xw.project.gracefulmovies.data.db.dao.BoxOfficeDao;
import com.xw.project.gracefulmovies.data.db.dao.CityDao;
import com.xw.project.gracefulmovies.data.db.dao.MovieDao;
import com.xw.project.gracefulmovies.data.db.entity.BoxOfficeEntity;
import com.xw.project.gracefulmovies.data.db.entity.CityEntity;
import com.xw.project.gracefulmovies.data.db.entity.MovieEntity;

/**
 * <p>
 * Created by woxingxiao on 2018-08-08.
 */
@Database(entities = {
        CityEntity.class,
        MovieEntity.class,
        BoxOfficeEntity.class},
        version = 1,
        exportSchema = false)
@TypeConverters({Converters.class})
public abstract class GMDatabase extends RoomDatabase {

    private static final String DB_NAME = "graceful_movie.db";

    public abstract CityDao cityDao();

    public abstract MovieDao movieDao();

    public abstract BoxOfficeDao boxOfficeDao();

    @WorkerThread
    public static GMDatabase createAsync(Application application) {
        return Room.databaseBuilder(application, GMDatabase.class, DB_NAME).build();
    }
}
