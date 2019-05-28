package com.example.nalen.weathertestproject;

public class BookmarkedCityItemInfo {

    private String cityName;
    private String location;
    private String databaseID;

    public BookmarkedCityItemInfo(String databaseID, String cityName, String location) {

        this.cityName = cityName;
        this.location = location;
        this.databaseID = databaseID;
    }

    public String getCityName() {

        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getDatabaseID() {
        return databaseID;
    }

    public void setDatabaseID(String databaseID) {
        this.databaseID = databaseID;
    }
}
