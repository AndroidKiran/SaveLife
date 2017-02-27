package com.donate.savelife.core.user.service;

import com.donate.savelife.core.chats.model.Message;
import com.donate.savelife.core.database.DatabaseResult;
import com.donate.savelife.core.user.data.model.Heroes;
import com.donate.savelife.core.user.data.model.User;
import com.donate.savelife.core.user.database.HeroDatabase;
import com.donate.savelife.core.user.database.UserDatabase;

import rx.Observable;
import rx.functions.Func1;

/**
 * Created by ravi on 07/12/16.
 */

public class PersistedHeroService implements HeroService {

    private final HeroDatabase heroDatabase;
    private final UserDatabase userDatabase;

    public PersistedHeroService(HeroDatabase heroDatabase, UserDatabase userDatabase) {
        this.heroDatabase = heroDatabase;
        this.userDatabase = userDatabase;
    }

    @Override
    public Observable<DatabaseResult<Heroes>> observerHeroes(String needID) {
        return heroDatabase.observeHeros(needID)
                .map(asHerosDatabaseResult())
                .onErrorReturn(DatabaseResult.<Heroes>errorAsDatabaseResult());
    }

    @Override
    public Observable<DatabaseResult<Boolean>> observeHero(Message  message) {
        return heroDatabase.observeHeroFrom(message.getNeedId(), message.getUserID())
                .map(asDatabaseResultExists())
                .onErrorReturn(DatabaseResult.<Boolean>errorAsDatabaseResult());
    }

    @Override
    public Observable<DatabaseResult<User>> honorHero(Message message, int value) {
        return heroDatabase.saveHero(message.getNeedId(), message.getUserID())
                .map(asHeroDatabaseResult())
                .filter(isHeroAddedSuccessfully())
                .flatMap(getUser())
                .filter(isUserFetchSuccessfully())
                .flatMap(updateCount(value))
                .onErrorReturn(DatabaseResult.<User>errorAsDatabaseResult());
    }

    private Func1<Heroes, DatabaseResult<Heroes>> asHerosDatabaseResult() {
        return new Func1<Heroes, DatabaseResult<Heroes>>() {
            @Override
            public DatabaseResult<Heroes> call(Heroes heroes) {
                return new DatabaseResult<Heroes>(heroes);
            }
        };
    }

    private Func1<String, DatabaseResult<String>> asHeroDatabaseResult() {
        return new Func1<String, DatabaseResult<String>>() {
            @Override
            public DatabaseResult<String> call(String userID) {
                return new DatabaseResult<String>(userID);
            }
        };
    }


    private Func1<Boolean, DatabaseResult<Boolean>> asDatabaseResultExists() {
        return new Func1<Boolean, DatabaseResult<Boolean>>() {
            @Override
            public DatabaseResult<Boolean> call(Boolean exists) {
                return new DatabaseResult<Boolean>(exists);
            }
        };
    }


    private Func1<DatabaseResult<String>, Boolean> isHeroAddedSuccessfully(){
        return new Func1<DatabaseResult<String>, Boolean>() {
            @Override
            public Boolean call(DatabaseResult<String> stringDatabaseResult) {
                return stringDatabaseResult.isSuccess() ? true : false;
            }
        };
    }

    private Func1<DatabaseResult<User>, Boolean> isUserFetchSuccessfully(){
        return new Func1<DatabaseResult<User>, Boolean>() {
            @Override
            public Boolean call(DatabaseResult<User> userDatabaseResult) {
                return userDatabaseResult.isSuccess() ? true : false;
            }
        };
    }

    private Func1<DatabaseResult<String>, Observable<DatabaseResult<User>>> getUser(){
        return new Func1<DatabaseResult<String>, Observable<DatabaseResult<User>>>() {
            @Override
            public Observable<DatabaseResult<User>> call(DatabaseResult<String> stringDatabaseResult) {
                return userDatabase.readUserFrom(UserDatabase.SINGLE_VALUE_EVENT_TYPE, stringDatabaseResult.getData())
                        .map(asUserDatabaseResult());
            }
        };
    }



    private Func1<User, DatabaseResult<User>> asUserDatabaseResult() {
        return new Func1<User, DatabaseResult<User>>() {
            @Override
            public DatabaseResult<User> call(User user) {
                return new DatabaseResult<User>(user);
            }
        };
    }

    private  Func1<DatabaseResult<User>, Observable<DatabaseResult<User>>> updateCount(final int value){
        return new Func1<DatabaseResult<User>,Observable<DatabaseResult<User>>>() {
            @Override
            public Observable<DatabaseResult<User>> call(DatabaseResult<User> userDatabaseResult) {
                return userDatabase.updateTheLifeCount(userDatabaseResult.getData(), value)
                        .map(asUserDatabaseResult());
            }
        };
    }

    private Func1<DatabaseResult<Heroes>, Boolean> isHeroHonored(){
        return new Func1<DatabaseResult<Heroes>, Boolean>() {
            @Override
            public Boolean call(DatabaseResult<Heroes> herosDatabaseResult) {
                return herosDatabaseResult.isSuccess() ? true : false;
            }
        };
    }

}
