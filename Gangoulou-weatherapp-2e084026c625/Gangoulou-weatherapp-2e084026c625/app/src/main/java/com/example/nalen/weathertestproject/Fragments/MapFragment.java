package com.example.nalen.weathertestproject.Fragments;

import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.nalen.weathertestproject.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;
import java.util.Locale;


public class MapFragment extends Fragment {

    private MapView mMapView;
    private GoogleMap googleMap;
    private Button returnButton;
    private Button selectLocationButton;
    private Double lat, lng;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_map, container, false);
        returnButton = rootView.findViewById(R.id.return_button);
        selectLocationButton = rootView.findViewById(R.id.get_weather_info_button);

        returnButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                BookmarkedLocationsListFragment bookmarkedLocationsListFragment = new BookmarkedLocationsListFragment();
                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.emptyFrame, bookmarkedLocationsListFragment).commit();

            }
        });

        selectLocationButton.setOnClickListener(new View.OnClickListener() {
            @Override

            public void onClick(View v) {
                Geocoder gcd = new Geocoder(getContext(), Locale.getDefault());
                Bundle args = new Bundle();
                try {

                    String locality = gcd.getFromLocation(lat, lng, 3).get(0).getLocality();

                    DetailedWeatherInfoFragment detailedWeatherInfoFragment = new DetailedWeatherInfoFragment();
                    detailedWeatherInfoFragment.setArguments(args);
                    args.putString("address", "lat=" + lat.toString() + "&lon=" + lng.toString());
                    args.putString("cityname", locality);
                    args.putString("lat", lat.toString());
                    args.putString("lon", lng.toString());
                    args.putBoolean("isBookmarked", false);
                    FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                    fragmentTransaction.replace(R.id.emptyFrame, detailedWeatherInfoFragment).commit();

                    // DatabaseProcessor databaseProcessor = new DatabaseProcessor(getContext());
                    // databaseProcessor.addData(locality, "lat=" + lat.toString() + "&lon=" + lng.toString());

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });


        mMapView = rootView.findViewById(R.id.mapView);
        mMapView.onCreate(savedInstanceState);

        mMapView.onResume();

        try {
            MapsInitializer.initialize(getActivity().getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }

        mMapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap mMap) {
                googleMap = mMap;
                Geocoder gcd = new Geocoder(getContext(), Locale.getDefault());
                List<Address> adress;
                try {
                    assert getArguments() != null;
                    adress = gcd.getFromLocationName(getArguments().getString("adress", "Lviv"), 1);
                    if (adress.size() > 0) {

                        LatLng latLng = new LatLng(adress.get(0).getLatitude(), adress.get(0).getLongitude());
                        googleMap.addMarker(new MarkerOptions().position(latLng).title(adress.get(0).getAddressLine(0)));
                        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(adress.get(0).getLatitude(), adress.get(0).getLongitude()), 12.0f));
                        lat = latLng.latitude;
                        lng = latLng.longitude;

                    } else {

                        Toast.makeText(getContext(), "Location not found, please try again.", Toast.LENGTH_SHORT).show();

                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                    @Override
                    public void onMapClick(LatLng latLng) {
                        googleMap.clear();
                        googleMap.addMarker(new MarkerOptions().position(latLng));
                        lat = latLng.latitude;
                        lng = latLng.longitude;
                    }
                });
            }
        });

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mMapView.onLowMemory();
    }
}