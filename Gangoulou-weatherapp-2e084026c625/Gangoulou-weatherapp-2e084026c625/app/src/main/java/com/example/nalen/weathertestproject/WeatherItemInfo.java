package com.example.nalen.weathertestproject;

public class WeatherItemInfo {
    private String location;
    private String temperature;
    private String humidity;
    private String rainChances;
    private String windInfo;
    private String dateTime;
    private String weather;
    private int weatherIconTag;


    public WeatherItemInfo(String location, String temperature, String humidity, String reainChances, String windInfo, String dateTime, String weather) {
        this.location = location;
        this.temperature = temperature;
        this.humidity = humidity;
        this.rainChances = reainChances;
        this.windInfo = windInfo;
        this.dateTime = dateTime;
        this.weather = weather;
    }

    public String getWeather() {
        return weather;
    }

    public void setWeather(String weather) {
        this.weather = weather;
    }

    public String getLocation() {
        return location;
    }

    public String getTemperature() {
        return temperature;
    }

    public void setTemperature(String temperature) {
        this.temperature = temperature;
    }

    public String getHumidity() {
        return humidity;
    }

    public void setHumidity(String humidity) {
        this.humidity = humidity;
    }

    public String getRainChances() {
        return rainChances;
    }

    public void setRainChances(String rainChances) {
        this.rainChances = rainChances;
    }

    public String getWindInfo() {
        return windInfo;
    }

    public void setWindInfo(String windInfo) {
        this.windInfo = windInfo;
    }

    public void getLocation(String cityName) {
        this.location = cityName;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public int getWeatherIconTag() {
        switch (weather) {
            case "Thunderstorm":
                return R.mipmap.ic_thunderstorm;
            case "Drizzle":
                return R.mipmap.ic_drizzly;
            case "Rain":
                return R.mipmap.ic_rainy;
            case "Snow":
                return R.mipmap.ic_snow;
            case "Atmosphere":
                return R.mipmap.ic_atmosphere;
            case "Clear":
                return R.mipmap.ic_sunny;
            case "Clouds":
                return R.mipmap.ic_cloudy;
        }
        return weatherIconTag;
    }

}
