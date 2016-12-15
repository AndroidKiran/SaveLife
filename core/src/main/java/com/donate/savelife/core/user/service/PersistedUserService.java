package com.donate.savelife.core.user.service;


import com.donate.savelife.core.database.DatabaseResult;
import com.donate.savelife.core.user.data.model.User;
import com.donate.savelife.core.user.data.model.Users;
import com.donate.savelife.core.user.database.UserDatabase;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import rx.Observable;
import rx.functions.Func1;

public class PersistedUserService implements UserService {

    private final UserDatabase userDatabase;

    public PersistedUserService(UserDatabase userDatabase) {
        this.userDatabase = userDatabase;
    }

    @Override
    public Observable<DatabaseResult<Users>> getTopHeros() {
        return userDatabase.observeTopHeros()
                .map(asReverseDatabaseResult())
                .onErrorReturn(DatabaseResult.<Users>errorAsDatabaseResult());
    }

    @Override
    public Observable<DatabaseResult<User>> observeUser(String userId) {
        return userDatabase.observeUser(userId)
                .map(asUserDatabaseResult())
                .onErrorReturn(DatabaseResult.<User>errorAsDatabaseResult());
    }

    @Override
    public Observable<DatabaseResult<User>> updateUser(User user) {
        return userDatabase.updateUser(user)
                .map(asUserDatabaseResult())
                .onErrorReturn(DatabaseResult.<User>errorAsDatabaseResult());
    }

    @Override
    public Observable<DatabaseResult<User>> updateTheLifeCount(User user) {
        return userDatabase.updateTheLifeCount(user)
                .map(asUserDatabaseResult());
    }

    private Func1<User, DatabaseResult<User>> asUserDatabaseResult() {
        return new Func1<User, DatabaseResult<User>>() {
            @Override
            public DatabaseResult<User> call(User user) {
                return new DatabaseResult<User>(user);
            }
        };
    }

    private Func1<Users, DatabaseResult<Users>> asUsersDatabaseResult(){
        return new Func1<Users, DatabaseResult<Users>>() {
            @Override
            public DatabaseResult<Users> call(Users users) {
                return new DatabaseResult<Users>(users);
            }
        };
    }

    private Func1<Users, DatabaseResult<Users>> asReverseDatabaseResult() {
        return new Func1<Users, DatabaseResult<Users>>() {
            @Override
            public DatabaseResult<Users> call(Users users) {
                return new DatabaseResult<Users>(reverse(users));
            }
        };
    }

    public Users reverse(Users users) {
        List<User> reverseList = new ArrayList<User>(users.getUsers());
        Collections.reverse(reverseList);
        return new Users(reverseList);
    }

}
