package com.example.nalen.weathertestproject;

import android.content.SearchRecentSuggestionsProvider;

public class LocationSearchSuggestionProvider extends SearchRecentSuggestionsProvider {
    public final static String AUTHORITY = "com.example.nalen.weathertestproject.LocationSearchSuggestionProvider";
    public final static int MODE = DATABASE_MODE_QUERIES;

    public LocationSearchSuggestionProvider() {
        setupSuggestions(AUTHORITY, MODE);
    }
}
