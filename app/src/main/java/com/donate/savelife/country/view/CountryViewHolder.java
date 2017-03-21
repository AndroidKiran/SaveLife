package com.donate.savelife.country.view;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.donate.savelife.core.country.model.Country;


/**
 * Created by ravi on 01/10/16.
 */

public class CountryViewHolder extends RecyclerView.ViewHolder {

    public CountryItemView countryItemView;

    public CountryViewHolder(CountryItemView itemView) {
        super(itemView);
        this.countryItemView = itemView;
    }

    public void bind(final Country country, final CountrySelectionListener countrySelectionListener){
        countryItemView.display(country);
        countryItemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                countrySelectionListener.onCountrySelected(country);
            }
        });
    }

    public interface CountrySelectionListener {
        void onCountrySelected(Country country);
    }
}
