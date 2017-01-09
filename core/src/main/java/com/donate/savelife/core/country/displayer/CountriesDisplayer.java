package com.donate.savelife.core.country.displayer;


import com.donate.savelife.core.country.model.Countries;
import com.donate.savelife.core.country.model.Country;

/**
 * Created by ravi on 30/09/16.
 */

public interface CountriesDisplayer {

    void attach(CountryInteractionListener countryInteractionListener);

    void detach(CountryInteractionListener countryInteractionListener);

    void display(Countries countries);

    void displayLoading();

    void displayContent();

    void displayError();

    void displayEmpty();

    interface CountryInteractionListener {

        void onCountrySelected(Country country);

        void onContentLoaded();

        void onError();

        void onEmpty();

    }

}
