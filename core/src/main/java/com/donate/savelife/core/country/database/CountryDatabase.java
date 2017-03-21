package com.donate.savelife.core.country.database;


import com.donate.savelife.core.country.model.Cities;
import com.donate.savelife.core.country.model.Countries;

import rx.Observable;

/**
 * Created by ravi on 30/09/16.
 */

public interface CountryDatabase {

    Observable<Countries> observeCountries();

    Observable<Cities> observeCities(String countryCode);

}
