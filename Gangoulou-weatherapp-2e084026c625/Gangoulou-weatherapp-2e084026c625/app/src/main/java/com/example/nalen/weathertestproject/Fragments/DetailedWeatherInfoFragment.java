package com.example.nalen.weathertestproject.Fragments;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nalen.weathertestproject.Adapters.ForecastListAdapter;
import com.example.nalen.weathertestproject.DatabaseProcessor;
import com.example.nalen.weathertestproject.R;
import com.example.nalen.weathertestproject.WeatherHttpProcessor;
import com.example.nalen.weathertestproject.WeatherItemInfo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class DetailedWeatherInfoFragment extends Fragment {
    WeatherHttpProcessor weatherHttpProcessor;

    TextView cityName;
    TextView temperature;
    TextView humidity;
    TextView windInfo;
    TextView weatherInfoLabel;
    TextView dateTime;

    ImageView currentWeatherImageView;

    Button returnToMainButton;
    Button addToBookmarksButton;

    String response;
    String locationString;
    String temperatureString;
    String humidityString;
    String windInfoString;
    String weatherInfoString;
    String dateTimeString;
    String cityNameString;

    String lat;
    String lon;
    boolean isBookmarked = false;

    int temperatureInt;

    RecyclerView forecastItemListRecyclerView;
    ForecastListAdapter mAdapter;

    ArrayList<WeatherItemInfo> weatherItemsList = new ArrayList<>();

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        getActivity().setTitle("Weather");
        cityName = view.findViewById(R.id.selected_city);
        temperature = view.findViewById(R.id.selected_city_temperature);
        humidity = view.findViewById(R.id.selected_city_humidity);
        windInfo = view.findViewById(R.id.selected_city_wind_info);
        weatherInfoLabel = view.findViewById(R.id.selected_city_weather);
        currentWeatherImageView = view.findViewById(R.id.current_weather_icon);

        weatherHttpProcessor = new WeatherHttpProcessor();

        JSONWeatherTask updateData = new JSONWeatherTask();

        try {
            response = updateData.execute(locationString).get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        try {
            JSONObject jsonObject = new JSONObject(response);
            JSONArray weatherInfo = jsonObject.getJSONArray("list");
            Log.d("ARRAY LENGHT", String.valueOf(weatherInfo.length()));
            for (int i = 0; i < weatherInfo.length(); i++) {

                JSONObject main = weatherInfo.getJSONObject(i).getJSONObject("main");
                JSONObject wind = weatherInfo.getJSONObject(i).getJSONObject("wind");
                JSONObject weather = weatherInfo.getJSONObject(i).getJSONArray("weather").getJSONObject(0);

                dateTimeString = weatherInfo.getJSONObject(i).getString("dt_txt");
                temperatureString = main.getString("temp");
                temperatureString = "" + (int) Double.parseDouble(temperatureString);
                humidityString = main.getString("humidity");
                windInfoString = wind.getString("speed");

                windInfoString += "m/s";
                humidityString += "%";
                weatherInfoString = weather.getString("main");
                weatherItemsList.add(new WeatherItemInfo(cityNameString, temperatureString, humidityString, "50%", windInfoString, dateTimeString, weatherInfoString));
            }


            temperatureString = weatherInfo.getJSONObject(0).getJSONObject("main").getString("temp");
            humidityString = weatherInfo.getJSONObject(0).getJSONObject("main").getString("humidity");
            windInfoString = weatherInfo.getJSONObject(0).getJSONObject("wind").getString("speed");
            weatherInfoString = weatherInfo.getJSONObject(0).getJSONArray("weather").getJSONObject(0).getString("main");
            currentWeatherImageView.setImageResource(weatherItemsList.get(0).getWeatherIconTag());

            windInfoString += "m/s";
            humidityString += "%";
            temperatureString = "" + (int) Double.parseDouble(temperatureString);

            temperature.setText(temperatureString + "℃");
            humidity.setText(humidityString);
            windInfo.setText(windInfoString);
            cityName.setText(cityNameString);
            weatherInfoLabel.setText(weatherInfoString);

        } catch (JSONException e) {
            e.printStackTrace();
            temperature.setText("ERROR");
        }
        mAdapter = new ForecastListAdapter(weatherItemsList, getFragmentManager());
        forecastItemListRecyclerView.setAdapter(mAdapter);
    }


    @Override
    @Nullable
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.detailed_weather_info_layout, container, false);
        LinearLayoutManager layoutManager
                = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);

        assert getArguments() != null;
        Bundle bundle = getArguments();

        locationString = bundle.getString("address", "lviv");
        cityNameString = bundle.getString("cityname", "Error");
        lat = bundle.getString("lat", "0");
        lon = bundle.getString("lon", "0");
        isBookmarked = bundle.getBoolean("isbookmarked");

        forecastItemListRecyclerView = rootView.findViewById(R.id.forecast_items_list);
        forecastItemListRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        forecastItemListRecyclerView.setLayoutManager(layoutManager);

        returnToMainButton = rootView.findViewById(R.id.forecast_return_main_button);
        returnToMainButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BookmarkedLocationsListFragment bookmarkedLocationsListFragment = new BookmarkedLocationsListFragment();
                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.emptyFrame, bookmarkedLocationsListFragment).commit();
            }
        });
        addToBookmarksButton = rootView.findViewById(R.id.add_to_bookmarks_button);
        if (!isBookmarked) {
            addToBookmarksButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DatabaseProcessor databaseProcessor = new DatabaseProcessor(getContext());
                    databaseProcessor.addData(cityNameString, "lat=" + lat + "&lon=" + lon);
                    addToBookmarksButton.setVisibility(View.INVISIBLE);
                    Toast.makeText(getContext(), "Location successfully added to your bookmarks", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            addToBookmarksButton.setVisibility(View.INVISIBLE);
        }
        return rootView;

    }

    private class JSONWeatherTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            String data = null;
            try {

                data = new WeatherHttpProcessor().getWeatherData(params[0]);

            } catch (IOException e) {
                e.printStackTrace();
            }
            return data;
        }

        @Override
        protected void onPostExecute(String s) {
        }
    }
}