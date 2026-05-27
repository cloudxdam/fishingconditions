package com.pachedev.fishingconditions.data.network;

import com.pachedev.fishingconditions.model.tides.TideResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Retrofit service interface for Marea API.
 */
public interface TideApiService {

    @GET("v2/tides")
    Call<TideResponse> getTides(
            @Query("latiture") double latitude,
            @Query("longitude") double longitude
    );
}
