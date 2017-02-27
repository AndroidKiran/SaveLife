package com.donate.savelife.core.requirement.service;

import com.donate.savelife.core.database.DatabaseResult;
import com.donate.savelife.core.requirement.model.Need;
import com.donate.savelife.core.requirement.model.Needs;
import com.donate.savelife.core.user.data.model.User;
import com.donate.savelife.core.user.data.model.Users;

import rx.Observable;

public interface NeedService {

    Observable<DatabaseResult<Needs>> observeNeedsWithUsers(User owner);

    Observable<DatabaseResult<Needs>> observeNeeds(User owner);

//    Observable<DatabaseResult<Needs>> observeMoreNeeds(User user, Need need);

//    Observable<DatabaseResult<Needs>> observeMoreNeedsWithUsers(User user, Need need);

    Observable<DatabaseResult<Need>> writeNeed(Need need);

    Observable<DatabaseResult<Users>> observeUserIdsFor();

//    Observable<DatabaseResult<Users>> observeMoreUsersFor(User user, Need need);

    Observable<DatabaseResult<Need>> observeNeed(String needID);

    Observable<DatabaseResult<Needs>> observeNeedsFor(User user);

    Observable<DatabaseResult<Integer>> observerResponseCount(Need need);

    Observable<DatabaseResult<Integer>> updateResponseCount(Need need, int count);

    Observable<DatabaseResult<Needs>> observeLatestNeedsFor(User user);


}
