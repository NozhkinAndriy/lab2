package com.example.nalen.weathertestproject.Fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.example.nalen.weathertestproject.Adapters.LocationsListAdapter;
import com.example.nalen.weathertestproject.BookmarkedCityItemInfo;
import com.example.nalen.weathertestproject.DatabaseProcessor;
import com.example.nalen.weathertestproject.R;

import java.util.ArrayList;
import java.util.List;


public class BookmarkedLocationsListFragment extends Fragment implements LocationsListAdapter.ItemClickListener {

    List<BookmarkedCityItemInfo> citiesList = new ArrayList<>();

    private RecyclerView locationsRecyclerView;
    private LocationsListAdapter forecastListAdapter;
    private RecyclerView.LayoutManager locationsLayoutManager;
    LocationsListAdapter locationsListAdapter;
    ProgressBar progressBar;

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.home:
                return true;
        }
        return false;
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        getActivity().setTitle("Recent locations");

        View rootView = inflater.inflate(R.layout.fragment_locations, container, false);

        progressBar = rootView.findViewById(R.id.forecastLoadingProgressBar);
        progressBar.setVisibility(View.INVISIBLE);

        RecyclerView locationsListRecyclerView = rootView.findViewById(R.id.locations_recycler_view);
        locationsListRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        DatabaseProcessor databaseProcessor = new DatabaseProcessor(getContext());
        citiesList.addAll(databaseProcessor.getData());

        locationsListAdapter = new LocationsListAdapter(citiesList, getFragmentManager());
        locationsListRecyclerView.setAdapter(locationsListAdapter);
        locationsListAdapter.setClickListener(this);

        setHasOptionsMenu(true);

        return rootView;
    }

    @Override
    public void onClick(View view, int position) {
        FragmentManager fManager;

        if (view == view.findViewById(R.id.locationImageView)) {
            fManager = getFragmentManager();

            DatabaseProcessor databaseProcessor = new DatabaseProcessor(getContext());
            int id = Integer.parseInt(citiesList.get(position).getDatabaseID());
            databaseProcessor.removeItem(id);

            BookmarkedLocationsListFragment locationsListAdapter = new BookmarkedLocationsListFragment();
            FragmentTransaction fragmentTransaction = fManager.beginTransaction();
            fragmentTransaction.replace(R.id.emptyFrame, locationsListAdapter).commit();
        }

        if (view == view.findViewById(R.id.locationItemTextView)) {
            progressBar.setVisibility(View.VISIBLE);

            DetailedWeatherInfoFragment detailedWeatherInfoFragment = new DetailedWeatherInfoFragment();
            Bundle args = new Bundle();
            args.putString("address", citiesList.get(position).getLocation());
            args.putString("cityname", citiesList.get(position).getCityName());
            args.putBoolean("isbookmarked", true);
            args.putString("bookmarkID", citiesList.get(position).getDatabaseID());
            detailedWeatherInfoFragment.setArguments(args);

            fManager = getFragmentManager();
            FragmentTransaction fragmentTransaction = fManager.beginTransaction();
            fragmentTransaction.replace(R.id.emptyFrame, detailedWeatherInfoFragment, "DetailedWeatherFragment").commit();
        }
    }
}
