package com.donate.savelife.core.country.displayer;


import com.donate.savelife.core.country.model.Cities;
import com.donate.savelife.core.country.model.City;

/**
 * Created by ravi on 30/09/16.
 */

public interface CityDisplayer {

    void attach(CityInteractionListener cityInteractionListener);

    void detach(CityInteractionListener cityInteractionListener);

    void display(Cities cities);

    void displayLoading();

    void displayContent();

    void displayError();

    void displayEmpty();

    interface CityInteractionListener {

        void onCitySelected(City city);

        void onContentLoaded();

        void onError();

        void onEmpty();

    }

}
