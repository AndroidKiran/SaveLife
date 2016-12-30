package com.donate.savelife.core.requirement.service;


import com.donate.savelife.core.UniqueList;
import com.donate.savelife.core.database.DatabaseResult;
import com.donate.savelife.core.requirement.database.NeedDatabase;
import com.donate.savelife.core.requirement.model.Need;
import com.donate.savelife.core.requirement.model.Needs;
import com.donate.savelife.core.user.data.model.User;
import com.donate.savelife.core.user.data.model.Users;
import com.donate.savelife.core.user.database.UserDatabase;

import java.util.Collections;
import java.util.List;

import rx.Observable;
import rx.functions.Func1;
import rx.functions.Func2;

public class PersistedNeedService implements NeedService {

    private final NeedDatabase needDatabase;
    private final UserDatabase userDatabase;

    public PersistedNeedService(NeedDatabase needDatabase, UserDatabase userDatabase) {
        this.needDatabase = needDatabase;
        this.userDatabase = userDatabase;
    }

    @Override
    public Observable<DatabaseResult<Needs>> observeNeedsWithUsers(User user) {
        return Observable.combineLatest(observeNeeds(user), observeUserIdsFor(user), mergeNeedsWithUser())
                .map(asReverseDatabaseResult())
                .onErrorReturn(DatabaseResult.<Needs>errorAsDatabaseResult());
    }


    @Override
    public Observable<DatabaseResult<Needs>> observeNeeds(User user) {
        return needDatabase.observeNeeds(user)
                .map(asNeedsDatabaseResult())
                .onErrorReturn(DatabaseResult.<Needs>errorAsDatabaseResult());
    }

    @Override
    public Observable<DatabaseResult<Needs>> observeMoreNeeds(User user, Need need) {
        return needDatabase.observeMoreNeeds(user, need)
                .map(asReverseDatabaseResult())
                .onErrorReturn(DatabaseResult.<Needs>errorAsDatabaseResult());
    }

    @Override
    public Observable<DatabaseResult<Needs>> observeMoreNeedsWithUsers(User user, Need need) {
        return Observable.combineLatest(observeMoreNeeds(user, need), observeMoreUserIdsFor(user, need), mergeNeedsWithUser())
                .map(asReverseDatabaseResult())
                .onErrorReturn(DatabaseResult.<Needs>errorAsDatabaseResult());
    }


    @Override
    public Observable<DatabaseResult<Need>> observeNeed(String needID) {
        return needDatabase.observeNeed(needID)
                .map(asNeedDatabaseResult())
                .onErrorReturn(DatabaseResult.<Need>errorAsDatabaseResult());
    }


    @Override
    public Observable<DatabaseResult<Need>> writeNeed(Need need) {
        return needDatabase.writeNewNeed(need)
                .map(new Func1<Need, DatabaseResult<Need>>() {
                    @Override
                    public DatabaseResult<Need> call(Need need) {
                        return new DatabaseResult<Need>(need);
                    }
                })
                .onErrorReturn(DatabaseResult.<Need>errorAsDatabaseResult());
    }

    @Override
    public Observable<DatabaseResult<Users>> observeUserIdsFor(User user) {
        return needDatabase.observerUserIdsFor(user)
                .flatMap(getUsersFromIds());
    }

    @Override
    public Observable<DatabaseResult<Users>> observeMoreUserIdsFor(User user, Need need) {
        return needDatabase.observerMoreUserIdsFor(user, need)
                .flatMap(getUsersFromIds());
    }

    private Func1<Needs, DatabaseResult<Needs>> asReverseDatabaseResult() {
        return new Func1<Needs, DatabaseResult<Needs>>() {
            @Override
            public DatabaseResult<Needs> call(Needs needs) {
                return new DatabaseResult<Needs>(reverse(needs));
            }
        };
    }

    private Func1<Needs, DatabaseResult<Needs>> asNeedsDatabaseResult() {
        return new Func1<Needs, DatabaseResult<Needs>>() {
            @Override
            public DatabaseResult<Needs> call(Needs needs) {
                return new DatabaseResult<Needs>(needs);
            }
        };
    }

    public Needs reverse(Needs needs) {
        UniqueList<Need> reverseList = new UniqueList<Need>();
        reverseList.addAll(needs.getNeeds());
        Collections.reverse(reverseList);
        return new Needs(reverseList);
    }


    private Func1<Need, DatabaseResult<Need>> asNeedDatabaseResult() {
        return new Func1<Need, DatabaseResult<Need>>() {
            @Override
            public DatabaseResult<Need> call(Need need) {
                return new DatabaseResult<Need>(need);
            }
        };
    }


    private Func1<List<String>, Observable<DatabaseResult<Users>>> getUsersFromIds() {
        return new Func1<List<String>, Observable<DatabaseResult<Users>>>() {
            @Override
            public Observable<DatabaseResult<Users>> call(List<String> userIds) {
                return Observable.from(userIds)
                        .flatMap(getUserFromId())
                        .toList()
                        .map(new Func1<List<User>, DatabaseResult<Users>>() {
                            @Override
                            public DatabaseResult<Users> call(List<User> users) {
                                return new DatabaseResult<>(new Users(users));
                            }
                        });
            }
        };
    }

    private Func1<String, Observable<User>> getUserFromId() {
        return new Func1<String, Observable<User>>() {
            @Override
            public Observable<User> call(final String userId) {
                return userDatabase.readUserFrom(UserDatabase.SINGLE_VALUE_EVENT_TYPE, userId);
            }
        };
    }

    private Func2<DatabaseResult<Needs>, DatabaseResult<Users>, Needs> mergeNeedsWithUser(){
        return new Func2<DatabaseResult<Needs>, DatabaseResult<Users>, Needs>() {
            @Override
            public Needs call(DatabaseResult<Needs> needsDatabaseResult, DatabaseResult<Users> usersDatabaseResult) {
                UniqueList<Need> needs = new UniqueList<Need>();
                for (Need need : needsDatabaseResult.getData().getNeeds()){
                    for (User user : usersDatabaseResult.getData().getUsers()){
                        if (need.getUserID().equals(user.getId())){
                            need.setUser(user);
                            needs.add(need);
                        }
                    }
                }
                return new Needs(needs);
            }
        };
    }


}
