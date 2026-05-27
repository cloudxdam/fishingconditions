package com.pachedev.fishingconditions.data.network;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import com.pachedev.fishingconditions.BuildConfig;

/**
 * Provides a Retrofit instance for Marea API.
 *
 * Uses a Singleton pattern so the same instance is reused.
 */
public class TideRetrofitInstance {

    private static final String BASE_URL = "https://api.marea.ooo/";

    private static final String API_KEY = BuildConfig.MAREA_API_KEY;
    private static Retrofit retrofit;

    private TideRetrofitInstance() {
        // Prevent instantiation
    }

    public static Retrofit getInstance() {
        if (retrofit == null) {
            OkHttpClient client = new OkHttpClient.Builder()
                    .addInterceptor(chain -> chain.proceed(
                            chain.request()
                                    .newBuilder()
                                    .addHeader("x-marea-api-token", API_KEY)
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
