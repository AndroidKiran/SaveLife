package com.donate.savelife.country.database;


import com.donate.savelife.core.country.database.CountryDatabase;
import com.donate.savelife.core.country.model.Cities;
import com.donate.savelife.core.country.model.City;
import com.donate.savelife.core.country.model.Countries;
import com.donate.savelife.core.country.model.Country;
import com.donate.savelife.rx.FirebaseObservableListeners;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.functions.Func1;

/**
 * Created by ravi on 30/09/16.
 */

public class FirebaseCountryDatabase implements CountryDatabase {

    private final DatabaseReference countryDB;
    private final FirebaseObservableListeners firebaseObservableListeners;

    public FirebaseCountryDatabase(FirebaseDatabase firebaseDatabase, FirebaseObservableListeners firebaseObservableListeners) {
        countryDB = firebaseDatabase.getReference("countries");
        this.firebaseObservableListeners = firebaseObservableListeners;
    }

    @Override
    public Observable<Countries> observeCountries() {
        return firebaseObservableListeners.listenToValueEvents(countryDB, toCountries());
    }

    @Override
    public Observable<Cities> observeCities(String countryCode) {
        return firebaseObservableListeners.listenToValueEvents(countryDB.child(countryCode), toCities());
    }

    private Func1<DataSnapshot, Countries> toCountries() {
        return new Func1<DataSnapshot, Countries>() {
            @Override
            public Countries call(DataSnapshot dataSnapshot) {
                List<Country> countries = new ArrayList<Country>();
                if (dataSnapshot.hasChildren()) {
                    Iterable<DataSnapshot> children = dataSnapshot.getChildren();
                    for (DataSnapshot child : children) {
                        Country country = (Country) child.getValue(Country.class);
                        country.setIsoCode(child.getKey());
                        countries.add(country);
                    }
                }
                return new Countries(countries);
            }
        };
    }

    private Func1<DataSnapshot, Cities> toCities(){
        return new Func1<DataSnapshot, Cities>() {
            @Override
            public Cities call(DataSnapshot dataSnapshot) {
                List<City> cities = new ArrayList<City>();
                if (dataSnapshot.hasChildren()){
                    Iterable<DataSnapshot> children = dataSnapshot.getChildren();
                    for (DataSnapshot child : children){
                        City city = child.getValue(City.class);
                        city.setId(child.getKey());
                        cities.add(city);
                    }
                }
                return new Cities(cities);
            }
        };
    }

}
