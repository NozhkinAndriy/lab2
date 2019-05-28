package com.example.nalen.weathertestproject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class WeatherHttpProcessor {

    //private static String BASE_URL = "http://api.openweathermap.org/data/2.5/weather?q=";
    //private static String BASE_URL = "http://api.openweathermap.org/data/2.5/forecast?q=London,us";
    private static String BASE_URL = "http://api.openweathermap.org/data/2.5/forecast?";
    private static String API_KEY = "&APPID=3f4badff28a0763d31533073aa63e4b8";
    HttpURLConnection conn;
    private String UNIT_SYSTEM = "metric";

    public String getWeatherData(String location) throws IOException {

        HttpURLConnection urlConnection;
        BufferedReader reader;
        URL url = new URL(BASE_URL + location + API_KEY + "&units=" + UNIT_SYSTEM);

        urlConnection = (HttpURLConnection) url.openConnection();
        urlConnection.setRequestMethod("GET");
        urlConnection.connect();

        InputStream inputStream = urlConnection.getInputStream();
        StringBuilder stringBuilder = new StringBuilder();

        if (inputStream == null) {
            return null;
        }

        reader = new BufferedReader(new InputStreamReader(inputStream));

        String line;

        while ((line = reader.readLine()) != null) {
            stringBuilder.append(line + "\n");
        }

        if (stringBuilder.length() == 0) {
            return null;
        }
        return stringBuilder.toString();
    }
}