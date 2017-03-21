package com.donate.savelife.country.city.view;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.donate.savelife.R;
import com.donate.savelife.core.country.displayer.CityDisplayer;
import com.donate.savelife.core.country.model.Cities;
import com.donate.savelife.core.country.model.City;

import java.util.ArrayList;

/**
 * Created by ravi on 01/10/16.
 */

public class CityAdapter extends RecyclerView.Adapter<CityViewHolder> {

    private final LayoutInflater inflater;
    private final Context context;
    private Cities cities;
    private CityDisplayer.CityInteractionListener cityInteractionListener;

    public CityAdapter(LayoutInflater inflater, Context context) {
        this.inflater = inflater;
        this.context = context;
        this.cities = new Cities(new ArrayList<City>());
    }

    public void setData(Cities cities) {
        if (cities.size() > 0) {
            this.cities = cities;
            notifyDataSetChanged();
            cityInteractionListener.onContentLoaded();
        } else {
            cityInteractionListener.onEmpty();
        }

    }

    @Override
    public CityViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        CityItemView cityItemView = (CityItemView) inflater.inflate(R.layout.city_item, parent, false);
        return new CityViewHolder(cityItemView);
    }

    @Override
    public void onBindViewHolder(CityViewHolder holder, int position) {
        holder.bind(this.cities.getCity(position), citySelectionListener);
    }

    @Override
    public int getItemCount() {
        return this.cities.size();
    }


    public void attach(CityDisplayer.CityInteractionListener cityInteractionListener) {
        this.cityInteractionListener = cityInteractionListener;
    }

    public void detach(CityDisplayer.CityInteractionListener cityInteractionListener) {
        this.cityInteractionListener = cityInteractionListener;
    }



    CityViewHolder.CitySelectionListener citySelectionListener = new CityViewHolder.CitySelectionListener() {
        @Override
        public void onCitySelected(City city) {
            cityInteractionListener.onCitySelected(city);
        }
    };
}
