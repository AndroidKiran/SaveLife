package com.donate.savelife.core.requirement.database;


import com.donate.savelife.core.requirement.model.Need;
import com.donate.savelife.core.requirement.model.Needs;
import com.donate.savelife.core.user.data.model.User;

import java.util.List;

import rx.Observable;

public interface NeedDatabase {

    Observable<Needs> observeNeeds(User owner);

    Observable<List<String>> observerUserIdsFor();

//    Observable<Needs> observeMoreNeeds(User user, Need need);

//    Observable<List<String>> observerMoreUsersFor(User user, Need need);

    Observable<Need> writeNewNeed(Need need);

    Observable<Need> observeNeed(String needID);

    Observable<Needs> observeNeedsFor(User user);

    Observable<Integer> observerResponseCount(Need need);

    Observable<Integer> updateResponseCount(Need need, int count);

    Observable<Needs> observeLatestNeedsFor(User user);

}
