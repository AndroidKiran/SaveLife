package com.donate.savelife.core.user.service;

import com.donate.savelife.core.database.DatabaseResult;
import com.donate.savelife.core.user.data.model.User;
import com.donate.savelife.core.user.data.model.Users;

import rx.Observable;

public interface UserService {

    Observable<DatabaseResult<Users>> getTopHeros();

    Observable<DatabaseResult<User>> observeUser(String userId);

    Observable<DatabaseResult<User>> updateUser(User user);

    Observable<DatabaseResult<User>> updateTheLifeCount(User user);

}
