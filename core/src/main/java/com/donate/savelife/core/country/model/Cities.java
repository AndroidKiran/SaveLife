package com.donate.savelife.core.country.model;

import android.content.Context;

import java.text.Collator;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

/**
 * Created by ravi on 17/03/17.
 */

public class Cities {

    private List<City> citiesList;

    public Cities(List<City> cities){
        this.citiesList = cities;
    }

    public int size(){
        return citiesList.size();
    }

    public City getCity(int index){
        return citiesList.get(index);
    }

    public List<City> getCityList() {
        return citiesList;
    }

    public void addCity(City city) {
        citiesList.add(city);
    }

    public void sort(final Context context){
        Collections.sort(citiesList, new Comparator<City>() {
            @Override
            public int compare(City city1, City city2) {
                final Locale locale = context.getResources().getConfiguration().locale;
                final Collator collator = Collator.getInstance(locale);
                collator.setStrength(Collator.PRIMARY);
                return collator.compare(
                        new Locale(locale.getLanguage(), city1.getName()),
                        new Locale(locale.getLanguage(), city2.getName())
                );
            }
        });
    }
}
