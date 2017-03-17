package com.donate.savelife.core.requirement.service;


import com.donate.savelife.core.database.DatabaseResult;
import com.donate.savelife.core.requirement.database.NeedDatabase;
import com.donate.savelife.core.requirement.model.Need;
import com.donate.savelife.core.requirement.model.Needs;
import com.donate.savelife.core.user.data.model.User;
import com.donate.savelife.core.user.database.UserDatabase;

import java.util.Calendar;

import rx.Observable;
import rx.functions.Func1;

public class PersistedNeedService implements NeedService {

    private final NeedDatabase needDatabase;
    private final UserDatabase userDatabase;

    public PersistedNeedService(NeedDatabase needDatabase, UserDatabase userDatabase) {
        this.needDatabase = needDatabase;
        this.userDatabase = userDatabase;
    }

    @Override
    public Observable<DatabaseResult<Need>> observeNeeds(User user) {
        return needDatabase.observeNeeds(user)
                .flatMap(updateNeedWithUserInfo())
                .map(asNeedDatabaseResultWithFilter(user))
                .onErrorReturn(DatabaseResult.<Need>errorAsDatabaseResult());
    }

    private Func1<Need, Observable<Need>> updateNeedWithUserInfo() {
        return new Func1<Need, Observable<Need>>() {
            @Override
            public Observable<Need> call(final Need need) {
                return userDatabase.readUserFrom(UserDatabase.SINGLE_VALUE_EVENT_TYPE, need.getUserID())
                        .map(new Func1<User, Need>() {
                            @Override
                            public Need call(User user) {
                                need.setUser(user);
                                return need;
                            }
                        });
            }
        };

    }

    private Func1<Need, DatabaseResult<Need>> asNeedDatabaseResultWithFilter(final User user) {
        return new Func1<Need, DatabaseResult<Need>>() {
            @Override
            public DatabaseResult<Need> call(Need need) {
                if (isDurationValid(need) && !user.getId().equals(need.getId())) {
                    return new DatabaseResult<Need>(need);
                } else {
                    return new DatabaseResult<Need>(new Throwable());
                }
            }
        };
    }

    @Override
    public Observable<DatabaseResult<Need>> observeNeed(String needID) {
        return needDatabase.observeNeed(needID)
                .map(asNeedDatabaseResult())
                .onErrorReturn(DatabaseResult.<Need>errorAsDatabaseResult());
    }

    @Override
    public Observable<DatabaseResult<Need>> observeMyNeeds(User user) {
        return needDatabase.observeMyNeeds(user)
                .flatMap(updateNeedWithUserInfo())
                .map(asNeedDatabaseResult())
                .onErrorReturn(DatabaseResult.<Need>errorAsDatabaseResult());
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
                .map(asNeedsDatabaseResult(user))
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

    private Func1<Needs, DatabaseResult<Needs>> asNeedsDatabaseResult(final User user) {
        return new Func1<Needs, DatabaseResult<Needs>>() {
            @Override
            public DatabaseResult<Needs> call(Needs needs) {
                return new DatabaseResult<Needs>(needs);
            }
        };
    }


    private Func1<Need, DatabaseResult<Need>> asNeedDatabaseResult() {
        return new Func1<Need, DatabaseResult<Need>>() {
            @Override
            public DatabaseResult<Need> call(Need need) {
                return new DatabaseResult<Need>(need);
            }
        };
    }

    private boolean isDurationValid(Need need) {
        Calendar cal1 = Calendar.getInstance();
        Calendar cal2 = Calendar.getInstance();
        cal1.setTimeInMillis(need.getTimeStamp());
        cal2.setTimeInMillis(System.currentTimeMillis());

        int days = cal2.get(Calendar.DAY_OF_YEAR) - cal1.get(Calendar.DAY_OF_YEAR);

        if (days < 5) {
            return true;
        }
        return false;
    }
}
