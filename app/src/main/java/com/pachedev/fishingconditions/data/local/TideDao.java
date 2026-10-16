package com.pachedev.fishingconditions.data.local;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

/**
 * Data access object for cached tide data.
 */
@Dao
public interface TideDao {

    @Query("SELECT * FROM tide_cache WHERE latitude = :latitude AND longitude = :longitude AND date = :date LIMIT 1")
    TideEntity findBySpotAndDate(double latitude, double longitude, String date);

    @Query("DELETE FROM tide_cache WHERE cachedAt < :cutoff")
    void deleteOlderThan(long cutoff);

    @Insert
    void insert(TideEntity tideEntity);
}