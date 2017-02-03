package com.donate.savelife.core.country.presenter;

import com.donate.savelife.core.analytics.Analytics;
import com.donate.savelife.core.analytics.ErrorLogger;
import com.donate.savelife.core.country.OnFragmentInteractionListener;
import com.donate.savelife.core.country.displayer.CountriesDisplayer;
import com.donate.savelife.core.country.model.Countries;
import com.donate.savelife.core.country.model.Country;
import com.donate.savelife.core.country.service.CountryService;
import com.donate.savelife.core.database.DatabaseResult;

import rx.functions.Action1;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by ravi on 01/10/16.
 */

public class CountryPresenter {

    private final CountryService countryService;
    private final CountriesDisplayer countryDisplayer;
    private final Analytics analytics;
    private final ErrorLogger errorLogger;
    private final OnFragmentInteractionListener fragmentInteractionListener;
    private CompositeSubscription subscription = new CompositeSubscription();


    public CountryPresenter(CountryService countryService,
                            CountriesDisplayer countryDisplayer,
                            OnFragmentInteractionListener fragmentInteractionListener,
                            Analytics analytics,
                            ErrorLogger errorLogger) {
        this.countryService = countryService;
        this.countryDisplayer = countryDisplayer;
        this.analytics = analytics;
        this.errorLogger = errorLogger;
        this.fragmentInteractionListener = fragmentInteractionListener;
    }

    public void startPresenting(){
        countryDisplayer.attach(countryInteractionListener);
        subscription.add(
                countryService.getCountries()
                        .subscribe(new Action1<DatabaseResult<Countries>>() {
                            @Override
                            public void call(DatabaseResult<Countries> countriesDatabaseResult) {
                                if (countriesDatabaseResult.isSuccess()){
                                    countryDisplayer.display(countriesDatabaseResult.getData());
                                } else {
                                    errorLogger.reportError(countriesDatabaseResult.getFailure(), "Failed to fetch the countries");
                                    countryDisplayer.displayError();
                                }
                            }
                        })
        );


    }

    public void stopPresenting(){
        countryDisplayer.detach(null);
        subscription.clear();
        subscription = new CompositeSubscription();
    }

    final CountriesDisplayer.CountryInteractionListener countryInteractionListener = new CountriesDisplayer.CountryInteractionListener() {
        @Override
        public void onCountrySelected(Country country) {
            fragmentInteractionListener.onFragmentInteraction(country);
        }

        @Override
        public void onContentLoaded() {
            countryDisplayer.displayContent();
        }

        @Override
        public void onError() {
            countryDisplayer.displayError();
        }

        @Override
        public void onEmpty() {
            countryDisplayer.displayEmpty();
        }
    };
}
