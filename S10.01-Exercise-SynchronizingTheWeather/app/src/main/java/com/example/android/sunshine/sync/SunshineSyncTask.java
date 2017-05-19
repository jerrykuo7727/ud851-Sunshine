package com.example.android.sunshine.sync;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.net.Network;
import android.net.Uri;

import com.example.android.sunshine.data.SunshinePreferences;
import com.example.android.sunshine.data.WeatherContract;
import com.example.android.sunshine.data.WeatherDbHelper;
import com.example.android.sunshine.data.WeatherProvider;
import com.example.android.sunshine.utilities.NetworkUtils;
import com.example.android.sunshine.utilities.OpenWeatherJsonUtils;
import com.example.android.sunshine.utilities.SunshineWeatherUtils;

import org.json.JSONException;

import java.io.IOException;
import java.net.URL;

import static com.example.android.sunshine.utilities.NetworkUtils.getUrl;

public class SunshineSyncTask {
    synchronized public static void syncWeather(Context context) {
        try {
            URL weatherRequestUrl = NetworkUtils.getUrl(context);
            String jsonWeatherResponse = NetworkUtils.getResponseFromHttpUrl(weatherRequestUrl);
            ContentValues weatherData[] = OpenWeatherJsonUtils
                    .getWeatherContentValuesFromJson(context, jsonWeatherResponse);

            if (weatherData != null && weatherData.length != 0) {
                ContentResolver contentResolver = context.getContentResolver();
                contentResolver.delete(WeatherContract.WeatherEntry.CONTENT_URI, null, null);
                contentResolver.bulkInsert(WeatherContract.WeatherEntry.CONTENT_URI, weatherData);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}