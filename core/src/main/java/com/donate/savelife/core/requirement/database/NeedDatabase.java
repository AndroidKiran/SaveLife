package com.donate.savelife.core.requirement.database;


import com.donate.savelife.core.requirement.model.Need;
import com.donate.savelife.core.requirement.model.Needs;
import com.donate.savelife.core.user.data.model.User;

import java.util.List;

import rx.Observable;

public interface NeedDatabase {

    Observable<Needs> observeNeeds(User user);

    Observable<List<String>> observerUserIdsFor(User user);

    Observable<Needs> observeMoreNeeds(User user, Need need);

    Observable<List<String>> observerMoreUserIdsFor(User user, Need need);

    Observable<Need> writeNewNeed(Need need);

    Observable<Need> observeNeed(String needID);

    Observable<Needs> observeLatestNeedFor(User user);

}
