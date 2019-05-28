package com.example.nalen.weathertestproject;

import android.app.SearchManager;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.provider.SearchRecentSuggestions;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.widget.CursorAdapter;
import android.widget.SearchView;
import android.widget.Toast;

import com.example.nalen.weathertestproject.Adapters.LocationSearchSuggestionAdapter;
import com.example.nalen.weathertestproject.Fragments.BookmarkedLocationsListFragment;
import com.example.nalen.weathertestproject.Fragments.MapFragment;


public class MainActivity extends AppCompatActivity {
    private FragmentTransaction fragmentTransaction;
    private LocationSearchSuggestionAdapter mAdapter;
    private int fragmentID;

    public static boolean isNetworkConnected(Context c) {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) c.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BookmarkedLocationsListFragment locationsFragment = new BookmarkedLocationsListFragment();
        fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.emptyFrame, locationsFragment).commit();

        final String[] from = new String[]{"cityName"};
        final int[] to = new int[]{android.R.id.text1};

        mAdapter = new LocationSearchSuggestionAdapter(getApplicationContext(),
                android.R.layout.simple_list_item_1, null,
                from,
                to,
                CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.options_menu, menu);

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        final SearchView searchView = (SearchView) menu.findItem(R.id.search_field_appbar).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setSuggestionsAdapter(mAdapter);

        searchView.setOnSuggestionListener(new SearchView.OnSuggestionListener() {
            @Override
            public boolean onSuggestionClick(int position) {

                MapFragment mapFragment = new MapFragment();
                Bundle args = new Bundle();
                args.putString("adress", searchView.getQuery().toString());
                mapFragment.setArguments(args);
                fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.emptyFrame, mapFragment, "MapFragment").commit();

                return true;
            }

            @Override
            public boolean onSuggestionSelect(int position) {
                return true;
            }
        });

        SearchView.OnQueryTextListener queryTextListener = new SearchView.OnQueryTextListener() {
            public boolean onQueryTextChange(String newText) {
                if (!isNetworkConnected(getApplicationContext())) {
                    return true;
                }
                if (!newText.equals("")) {
                    mAdapter.populateAdapter(newText);
                    return true;
                }
                return true;
            }

            public boolean onQueryTextSubmit(String query) {
                if (!isNetworkConnected(getApplicationContext())) {
                    Toast.makeText(MainActivity.this, "Network connection error", Toast.LENGTH_SHORT).show();
                    return true;
                }
                if (query != null) {

                    SearchRecentSuggestions suggestions = new SearchRecentSuggestions(getApplicationContext(),
                            LocationSearchSuggestionProvider.AUTHORITY, LocationSearchSuggestionProvider.MODE);
                    suggestions.saveRecentQuery(query, null);

                    MapFragment mapFragment = new MapFragment();
                    Bundle args = new Bundle();
                    args.putString("adress", query);
                    mapFragment.setArguments(args);
                    fragmentTransaction = getSupportFragmentManager().beginTransaction();
                    fragmentTransaction.replace(R.id.emptyFrame, mapFragment).commit();
                }
                return true;
            }
        };

        searchView.setOnQueryTextListener(queryTextListener);
        return true;
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}