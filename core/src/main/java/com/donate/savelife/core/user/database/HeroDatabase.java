package com.donate.savelife.core.user.database;

import com.donate.savelife.core.user.data.model.Heroes;

import rx.Observable;

/**
 * Created by ravi on 07/12/16.
 */

public interface HeroDatabase {

    Observable<Heroes> observeHeros(String needID);

    Observable<Boolean> observeHeroFrom(String needID, String userID);

    Observable<String> saveHero(String needId, String userID);
}
