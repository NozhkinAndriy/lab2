package com.example.nalen.weathertestproject.Adapters;

import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.nalen.weathertestproject.BookmarkedCityItemInfo;
import com.example.nalen.weathertestproject.DatabaseProcessor;
import com.example.nalen.weathertestproject.R;

import java.util.List;


public class LocationsListAdapter extends RecyclerView.Adapter<LocationsListAdapter.LocationsListViewHolder> {

    private ItemClickListener clickListener;
    private List<BookmarkedCityItemInfo> citiesList;
    private FragmentManager fManager;
    private DatabaseProcessor databaseProcessor;

    public LocationsListAdapter(List<BookmarkedCityItemInfo> citiesList, FragmentManager c) {
        this.fManager = c;
        this.citiesList = citiesList;
    }

    public void setItems(List<BookmarkedCityItemInfo> bookmarkedCityItemInfos) {
        this.citiesList.addAll(bookmarkedCityItemInfos);
    }

    @NonNull
    @Override
    public LocationsListViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {

        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.city_item_layout, viewGroup, false);
        return new LocationsListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final LocationsListViewHolder locationsListViewHolder, final int position) {
        locationsListViewHolder.bind(citiesList.get(position).getCityName());
    }

    @Override
    public int getItemCount() {
        return citiesList.size();
    }

    public void setClickListener(ItemClickListener itemClickListener) {
        this.clickListener = itemClickListener;
    }

    public interface ItemClickListener {
        void onClick(View view, int position);
    }

    public class LocationsListViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView bookmarkedLocationTextView;
        private ImageView removeBookmarkedLocationIcon;
        private DatabaseProcessor databaseProcessor;
        private FragmentManager fManager;

        private LocationsListViewHolder(View itemView) {
            super(itemView);
            bookmarkedLocationTextView = itemView.findViewById(R.id.locationItemTextView);
            removeBookmarkedLocationIcon = itemView.findViewById(R.id.locationImageView);
            removeBookmarkedLocationIcon.setOnClickListener(this);
            bookmarkedLocationTextView.setOnClickListener(this);
        }

        private void bind(String city) {
            bookmarkedLocationTextView.setText(city);
        }

        @Override
        public void onClick(View v) {
            if (clickListener != null) clickListener.onClick(v, getAdapterPosition());
        }
    }
}