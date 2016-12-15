package com.donate.savelife.core.country.service;


import com.donate.savelife.core.country.database.CountryDatabase;
import com.donate.savelife.core.country.model.Countries;
import com.donate.savelife.core.database.DatabaseResult;

import rx.Observable;
import rx.functions.Func1;

/**
 * Created by ravi on 30/09/16.
 */

public class PersistedCountryService implements CountryService {

    private final CountryDatabase countryDatabase;

    public PersistedCountryService(CountryDatabase countryDatabase){
        this.countryDatabase = countryDatabase;
    }

    @Override
    public Observable<DatabaseResult<Countries>> getCountries() {
        return countryDatabase.observeCountries()
                .map(new Func1<Countries, DatabaseResult<Countries>>() {
                    @Override
                    public DatabaseResult<Countries> call(Countries countries) {
                        return new DatabaseResult<Countries>(countries);
                    }
                })
                .onErrorReturn(DatabaseResult.<Countries>errorAsDatabaseResult());
    }
}
