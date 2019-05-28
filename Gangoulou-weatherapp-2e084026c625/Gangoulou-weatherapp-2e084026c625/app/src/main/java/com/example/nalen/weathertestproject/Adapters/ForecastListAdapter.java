package com.example.nalen.weathertestproject.Adapters;

import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.nalen.weathertestproject.R;
import com.example.nalen.weathertestproject.WeatherItemInfo;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;


public class ForecastListAdapter extends RecyclerView.Adapter<ForecastListAdapter.ForecastListViewHolder> {

    private ArrayList<WeatherItemInfo> forecastItemList = new ArrayList<>();

    public ForecastListAdapter(ArrayList<WeatherItemInfo> forecastItemList, FragmentManager c) {
        this.forecastItemList = forecastItemList;
    }

    public void setItems(ArrayList<WeatherItemInfo> forecastItemList) {
        this.forecastItemList.addAll(forecastItemList);
    }

    @NonNull
    @Override
    public ForecastListViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {

        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.forecast_item_layout, viewGroup, false);
        return new ForecastListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ForecastListViewHolder locationsListViewHolder, int position) {
        int min = findMinTemp();
        int max = findMaxTemp();
        locationsListViewHolder.bind(forecastItemList.get(position), position, min, max);
    }

    @Override
    public int getItemCount() {
        return forecastItemList.size();
    }

    int findMaxTemp() {
        int max = 0;
        for (int i = 0; i < forecastItemList.size(); i++) {
            if (Integer.parseInt(forecastItemList.get(i).getTemperature()) > max) {
                max = Integer.parseInt(forecastItemList.get(i).getTemperature());

            }
        }
        return max;
    }

    int findMinTemp() {
        int min = 0;
        for (int i = 0; i < forecastItemList.size(); i++) {
            if (Integer.parseInt(forecastItemList.get(i).getTemperature()) < min) {
                min = Integer.parseInt(forecastItemList.get(i).getTemperature());
            }
        }
        return min;
    }

    public static class ForecastListViewHolder extends RecyclerView.ViewHolder {

        TextView temperatureTextView;
        TextView humidityTextView;
        TextView weather;
        TextView windInfoTextView;
        TextView dateTime;
        TextView dayName;
        RelativeLayout temperatureLineLayout;
        ImageView forecastItemImageView;

        private ForecastListViewHolder(View itemView) {
            super(itemView);

            temperatureTextView = itemView.findViewById(R.id.forecast_item_temperature);
            humidityTextView = itemView.findViewById(R.id.forecast_item_humidity);
            weather = itemView.findViewById(R.id.forecast_item_weather);
            windInfoTextView = itemView.findViewById(R.id.forecast_item_wind_info);
            dateTime = itemView.findViewById(R.id.forecast_item_date);
            forecastItemImageView = itemView.findViewById(R.id.forecast_item_icon);
            dayName = itemView.findViewById(R.id.day_name_string);
            temperatureLineLayout = itemView.findViewById(R.id.temperature_line_layout);
        }


        private void bind(WeatherItemInfo weatherItemInfo, int position, int min, int max) {

            temperatureTextView.setText(weatherItemInfo.getTemperature() + "℃");
            humidityTextView.setText(weatherItemInfo.getHumidity());
            weather.setText(weatherItemInfo.getWeather());
            windInfoTextView.setText(weatherItemInfo.getWindInfo());
            int temp = Integer.parseInt(weatherItemInfo.getTemperature());
            if (Math.abs(max) > Math.abs(min))
                temperatureLineLayout.setPadding(0, 0, 0, 44 + (44 / (Math.abs(min) + Math.abs(max))) * temp);
            else
                temperatureLineLayout.setPadding(0, 0, 0, 44 + (44 / (Math.abs(min) + Math.abs(max))) * temp);

            String dayOfWeek = "";
            Date date;
            try {
                date = new SimpleDateFormat("yyyy-M-d").parse(weatherItemInfo.getDateTime().substring(0, 10));
                dayOfWeek = new SimpleDateFormat("EEEE", Locale.ENGLISH).format(date);
            } catch (ParseException e) {
                e.printStackTrace();
            }

            if (position == 0) {
                dayName.setText("Today");
            } else if (weatherItemInfo.getDateTime().substring(11, 16).equals("00:00")) {
                dayName.setText(dayOfWeek + " " + weatherItemInfo.getDateTime().substring(8, 11));
            } else {
                dayName.setText("");
            }
            dateTime.setText(weatherItemInfo.getDateTime().substring(11, 16));
            forecastItemImageView.setImageResource(weatherItemInfo.getWeatherIconTag());
        }
    }
}