package com.heaton.weekview.model.localDataSource;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.heaton.weekview.model.ClassInterval;
import com.heaton.weekview.model.DateTypeConverters;

@TypeConverters({DateTypeConverters.class})
@Database(entities = {ClassInterval.class}, version = 1, exportSchema = false)
public abstract class ClassDataBase extends RoomDatabase {
    private static final String DB_NAME = "ClassDatabase.db";
    private static volatile ClassDataBase instance;

    public static ClassDataBase getInstance(Context context) {
        if (instance == null) {
            synchronized (LocalClassDataSource.class) {
                if (instance == null) {
                    instance = create(context);
                }
            }
        }
        return instance;
    }

    private static ClassDataBase create(final Context context) {
        ClassDataBase dataBase = Room.databaseBuilder(
                context,
                ClassDataBase.class,
                DB_NAME)
                .fallbackToDestructiveMigration()
                .build();
        return dataBase;
    }

    public abstract ClassIntervalDao getClassIntervalDao();

}
