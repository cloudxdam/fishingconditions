package com.pachedev.fishingconditions.data.network;

import com.pachedev.fishingconditions.model.marine.MarineResponse;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Retrofit service interface for Open-Meteo Marine API.
 */
public interface MarineApiService {

    @GET("marine")
    Call<MarineResponse> getMarineData(
            @Query("latitude") double latitude,
            @Query("longitude") double longitude,
            @Query("hourly") String hourly,
            @Query("timezone") String timezone,
            @Query("start_date") String startDate,
            @Query("end_date") String endDate
    );
}
