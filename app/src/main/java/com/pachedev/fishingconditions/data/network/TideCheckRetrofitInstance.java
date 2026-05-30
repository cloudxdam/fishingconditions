package com.pachedev.fishingconditions.data.network;

import com.pachedev.fishingconditions.BuildConfig;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Provides a Retrofit instance for TideCheck API.
 */
public class TideCheckRetrofitInstance {

    private static final String BASE_URL = "https://tidecheck.com/";
    private static Retrofit retrofit;

    private TideCheckRetrofitInstance() {
        // Prevent instantiation
    }

    public static Retrofit getRetrofitInstance() {
        if (retrofit == null) {
            OkHttpClient client = new OkHttpClient.Builder()
                    .addInterceptor(chain -> chain.proceed(
                            chain.request()
                                    .newBuilder()
                                    .addHeader("X-API-Key", BuildConfig.TIDECHECK_API_KEY)
                                    .build()
                    )).build();

            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }




}
