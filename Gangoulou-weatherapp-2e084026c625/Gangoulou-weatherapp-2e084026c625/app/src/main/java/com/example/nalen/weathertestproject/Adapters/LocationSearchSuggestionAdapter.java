package com.example.nalen.weathertestproject.Adapters;

import android.content.Context;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.provider.BaseColumns;
import android.widget.SimpleCursorAdapter;

import java.io.IOException;
import java.util.List;

public class LocationSearchSuggestionAdapter extends SimpleCursorAdapter {

    private List<Address> addressList;
    private GetLocale getLocale;
    private Geocoder gcd;


    public LocationSearchSuggestionAdapter(Context context, int layout, Cursor c, String[] from, int[] to, int flagRegisterContentObserver) {
        super(context, layout, c, from, to);
        gcd = new Geocoder(context);
        getLocale = new GetLocale();
    }


    public void populateAdapter(String query) {
        if (query.length() > 0) {
            if (getLocale.getStatus() == AsyncTask.Status.PENDING) {
                getLocale.execute(query);
            } else {
                getLocale.cancel(false);
                getLocale = new GetLocale();
                getLocale.execute(query);
            }
        }
    }

    private class GetLocale extends AsyncTask<String, Void, Void> {
        final MatrixCursor c = new MatrixCursor(new String[]{BaseColumns._ID, "cityName"});

        @Override
        protected Void doInBackground(String... query) {
            try {
                addressList = gcd.getFromLocationName(query[0], 20);
                for (int i = 0; i < addressList.size(); i++) {
                    if (addressList.get(i).getFeatureName().toLowerCase().startsWith(query[0].toLowerCase()))
                        c.addRow(new Object[]{i, addressList.get(i).getFeatureName()});
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            changeCursor(c);
        }
    }
}
