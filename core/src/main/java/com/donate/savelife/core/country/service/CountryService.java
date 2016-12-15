package com.donate.savelife.core.country.service;


import com.donate.savelife.core.country.model.Countries;
import com.donate.savelife.core.database.DatabaseResult;

import rx.Observable;

/**
 * Created by ravi on 30/09/16.
 */

public interface CountryService {
    Observable<DatabaseResult<Countries>> getCountries();
}
