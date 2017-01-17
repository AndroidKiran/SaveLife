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
import java.util.ListIterator;

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
    public Observable<DatabaseResult<Needs>> observeNeedsWithUsers(User owner) {
        return Observable.combineLatest(observeNeeds(owner), observeUserIdsFor(), mergeNeedsWithUser())
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
    public Observable<DatabaseResult<Needs>> observeNeedsFor(User user) {
        return needDatabase.observeNeedsFor(user)
                .map(asReverseDatabaseResultWithUserUpdated(user))
                .onErrorReturn(DatabaseResult.<Needs>errorAsDatabaseResult());
    }

    @Override
    public Observable<DatabaseResult<Integer>> observerResponseCount(Need need) {
        return needDatabase.observerResponseCount(need)
                .map(new Func1<Integer, DatabaseResult<Integer>>() {
                    @Override
                    public DatabaseResult<Integer> call(Integer integer) {
                        return new DatabaseResult<Integer>(integer);
                    }
                })
                .onErrorReturn(DatabaseResult.<Integer>errorAsDatabaseResult());
    }

    @Override
    public Observable<DatabaseResult<Integer>> updateResponseCount(Need need, int count) {
        return needDatabase.updateResponseCount(need, count)
                .map(new Func1<Integer, DatabaseResult<Integer>>() {
                    @Override
                    public DatabaseResult<Integer> call(Integer integer) {
                        return new DatabaseResult<Integer>(integer);
                    }
                })
                .onErrorReturn(DatabaseResult.<Integer>errorAsDatabaseResult());
    }

    @Override
    public Observable<DatabaseResult<Needs>> observeLatestNeedsFor(User user) {
        return needDatabase.observeLatestNeedsFor(user)
                .map(asNeedsDatabaseResult())
                .onErrorReturn(DatabaseResult.<Needs>errorAsDatabaseResult());
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
    public Observable<DatabaseResult<Users>> observeUserIdsFor() {
        return needDatabase.observerUserIdsFor()
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

    private Func1<Needs, DatabaseResult<Needs>> asReverseDatabaseResultWithUserUpdated(final User user) {
        return new Func1<Needs, DatabaseResult<Needs>>() {
            @Override
            public DatabaseResult<Needs> call(Needs needs) {
                ListIterator<Need> needListIterator = needs.getNeeds().listIterator();
                while (needListIterator.hasNext()){
                    Need need = needListIterator.next();
                    need.setUser(user);
                }
               return new DatabaseResult<Needs>(reverse(needs));
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
                ListIterator<Need> needListIterator = needsDatabaseResult.getData().getNeeds().listIterator();
                while (needListIterator.hasNext()){
                    Need need = needListIterator.next();
                    ListIterator<User> userListIterator = usersDatabaseResult.getData().getUsers().listIterator();
                    while (userListIterator.hasNext()){
                        User user = userListIterator.next();
                        if (need.getUserID().equals(user.getId())){
                            need.setUser(user);
                        }
                    }
                }
                return needsDatabaseResult.getData();
            }
        };
    }

}
