package com.pachedev.fishingconditions;

import android.os.Bundle;
import android.util.Log;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.pachedev.fishingconditions.data.network.OpenMeteoApiService;
import com.pachedev.fishingconditions.data.network.RetrofitInstance;
import com.pachedev.fishingconditions.model.WeatherResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);

        OpenMeteoApiService apiService = RetrofitInstance.getRetrofitInstance().create(OpenMeteoApiService.class);

        Call<WeatherResponse> call = apiService.getWeatherData(
                28.12,
                - 16.73,
                "temperature_2m,wind_speed_10m",
                "sunrise,sunset",
                "Atlantic/Canary"
        );

        call.enqueue(new Callback<WeatherResponse>() {
            @Override
            public void onResponse(Call<WeatherResponse> call, Response<WeatherResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    WeatherResponse data = response.body();

                    double temperature = data.getHourly().getTemperature2m().get(0);
                    String sunrise = data.getDaily().getSunrise().get(0);
                    String sunset = data.getDaily().getSunset().get(0);

                    Log.d("API", "Temperature" + temperature);
                    Log.d("API", "Sunrise" + sunrise);
                    Log.d("API", "Sunset" + sunset);
                }
            }

            @Override
            public void onFailure(Call<WeatherResponse> call, Throwable t) {
                    Log.d("API", "Error" + t.getMessage());
            }
        });

    }
}