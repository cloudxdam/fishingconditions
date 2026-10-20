package com.pachedev.fishingconditions.data.local;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

/**
 * Room database for local tide cache.
 */
@Database(entities = {TideEntity.class}, version = 2, exportSchema = false)
public abstract class TideDatabase extends RoomDatabase {

    private static final String DATABASE_NAME = "fishing_conditions_db";

    private static final Migration MIGRATION_1_2 = new Migration(1, 2) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            database.execSQL("ALTER TABLE tide_cache ADD COLUMN cachedAt INTEGER NOT NULL DEFAULT 0");
        }
    };

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
                    ).addMigrations(MIGRATION_1_2)
                     .build();
                }
            }
        }

        return instance;
    }
}
