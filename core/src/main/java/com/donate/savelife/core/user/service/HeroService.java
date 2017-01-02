package com.donate.savelife.core.user.service;

import com.donate.savelife.core.database.DatabaseResult;
import com.donate.savelife.core.user.data.model.Heros;
import com.donate.savelife.core.user.data.model.User;

import rx.Observable;

/**
 * Created by ravi on 07/12/16.
 */

public interface HeroService {

    Observable<DatabaseResult<Heros>> observerHeros(String needID);

    Observable<DatabaseResult<Boolean>> observeHero(String needID, String userID);

    Observable<DatabaseResult<User>> saveHero(String needId, String userID);

}
