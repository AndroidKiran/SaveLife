package com.donate.savelife.core.country.presenter;

import com.donate.savelife.core.analytics.Analytics;
import com.donate.savelife.core.analytics.ErrorLogger;
import com.donate.savelife.core.country.OnCityInteractionListener;
import com.donate.savelife.core.country.displayer.CityDisplayer;
import com.donate.savelife.core.country.model.Cities;
import com.donate.savelife.core.country.model.City;
import com.donate.savelife.core.country.service.CountryService;
import com.donate.savelife.core.database.DatabaseResult;

import rx.functions.Action1;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by ravi on 01/10/16.
 */

public class CityPresenter {

    private final CountryService countryService;
    private final Analytics analytics;
    private final ErrorLogger errorLogger;
    private final String countryCode;
    private final CityDisplayer cityDisplayer;
    private final OnCityInteractionListener fragmentInteractionListener;
    private CompositeSubscription subscription = new CompositeSubscription();


    public CityPresenter(CountryService countryService,
                         CityDisplayer cityDisplayer,
                         OnCityInteractionListener fragmentInteractionListener,
                         Analytics analytics,
                         ErrorLogger errorLogger,
                         String countryCode) {
        this.countryService = countryService;
        this.cityDisplayer = cityDisplayer;
        this.analytics = analytics;
        this.errorLogger = errorLogger;
        this.fragmentInteractionListener = fragmentInteractionListener;
        this.countryCode = countryCode;
    }

    public void startPresenting(){
        cityDisplayer.attach(cityInteractionListener);
        subscription.add(
                countryService.observeCities(countryCode)
                        .subscribe(new Action1<DatabaseResult<Cities>>() {
                            @Override
                            public void call(DatabaseResult<Cities> citiesDatabaseResult) {
                                if (citiesDatabaseResult.isSuccess()){
                                    cityDisplayer.display(citiesDatabaseResult.getData());
                                } else {

                                }
                            }
                        })
        );
    }

    public void stopPresenting(){
        cityDisplayer.detach(null);
        subscription.clear();
        subscription = new CompositeSubscription();
    }

    final CityDisplayer.CityInteractionListener cityInteractionListener = new CityDisplayer.CityInteractionListener() {


        @Override
        public void onCitySelected(City city) {
            fragmentInteractionListener.onCityInteraction(city);
        }

        @Override
        public void onContentLoaded() {
            cityDisplayer.displayContent();
        }

        @Override
        public void onError() {
            cityDisplayer.displayError();
        }

        @Override
        public void onEmpty() {
            cityDisplayer.displayEmpty();
        }
    };
}
