package com.donate.savelife.core.requirement.service;

import com.donate.savelife.core.database.DatabaseResult;
import com.donate.savelife.core.requirement.model.Need;
import com.donate.savelife.core.requirement.model.Needs;
import com.donate.savelife.core.user.data.model.User;

import rx.Observable;

public interface NeedService {


    Observable<DatabaseResult<Need>> observeNeeds(User owner);

    Observable<DatabaseResult<Need>> writeNeed(Need need);

    Observable<DatabaseResult<Need>> observeNeed(String needID);

    Observable<DatabaseResult<Need>> observeMyNeeds(User user);

    Observable<DatabaseResult<Integer>> observerResponseCount(Need need);

    Observable<DatabaseResult<Integer>> updateResponseCount(Need need, int count);

    Observable<DatabaseResult<Needs>> observeLatestNeedsFor(User user);

}
