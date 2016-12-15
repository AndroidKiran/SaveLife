package com.donate.savelife.core.user.database;

import com.donate.savelife.core.user.data.model.Heros;

import rx.Observable;

/**
 * Created by ravi on 07/12/16.
 */

public interface HeroDatabase {

    Observable<Heros> observeHeros(String needID);

    Observable<String> observeHeroFrom(String needID, String userID);

    Observable<String> saveHero(String needId, String userID);
}
