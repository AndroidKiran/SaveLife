package com.donate.savelife.core.user.database;


import com.donate.savelife.core.user.data.model.User;
import com.donate.savelife.core.user.data.model.Users;

import rx.Observable;

public interface UserDatabase {

    String SINGLE_VALUE_EVENT_TYPE = "single_value_event";
    String VALUE_EVENT_TYPE = "value_event";

    Observable<Users> observeTopHeros();

    Observable<User> readUserFrom(String type, String userId);

    void writeCurrentUser(User user);

    Observable<User> observeUser(String userId);

    Observable<User> updateUser(User user);

    Observable<User> updateTheLifeCount(User user);

}
