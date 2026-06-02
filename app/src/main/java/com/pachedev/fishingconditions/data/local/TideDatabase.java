package com.pachedev.fishingconditions.data.local;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

/**
 * Room database for local tide cache.
 */
@Database(entities = {TideEntity.class}, version = 1)
public abstract class TideDatabase extends RoomDatabase {

    private static final String DATABASE_NAME = "fishing_conditions_db";

    private static volatile TideDatabase instance;

    public abstract TideDao tideDao();

    public static TideDatabase getInstance(Context context) {
        if (instance == null) {
            synchronized (TideDatabase.class) {
                if (instance == null) {
                    instance = Room.databaseBuilder(
                            context.getApplicationContext(),
                            TideDatabase.class,
                            DATABASE_NAME
                    ).build();
                }
            }
        }

        return instance;
    }
}