package com.donate.savelife.country.city.view;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.donate.savelife.core.country.model.City;


/**
 * Created by ravi on 01/10/16.
 */

public class CityViewHolder extends RecyclerView.ViewHolder {

    public CityItemView cityItemView;

    public CityViewHolder(CityItemView itemView) {
        super(itemView);
        this.cityItemView = itemView;
    }

    public void bind(final City city, final CitySelectionListener citySelectionListener){
        cityItemView.display(city);
        cityItemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                citySelectionListener.onCitySelected(city);
            }
        });
    }

    public interface CitySelectionListener {
        void onCitySelected(City city);
    }
}
