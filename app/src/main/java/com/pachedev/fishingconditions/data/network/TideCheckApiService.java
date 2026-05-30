package com.pachedev.fishingconditions.data.network;

import com.pachedev.fishingconditions.model.tidecheck.TideCheckNearestStationResponse;
import com.pachedev.fishingconditions.model.tidecheck.TideCheckResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Retrofit service interface for TideCheck API.
 */
public interface TideCheckApiService {

    @GET("api/stations/nearest")
    Call<List<TideCheckNearestStationResponse>> getNearestStations(
            @Query("lat") double latitude,
            @Query("lng") double longitude
    );

    @GET("api/station/{stationId}/tides")
    Call<TideCheckResponse> getTides(
            @Path("stationId") String stationId,
            @Query("datum") String datum,
            @Query("days") int days,
            @Query("start") String startDate
    );
}