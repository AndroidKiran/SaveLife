package com.donate.savelife.core.requirement.database;


import com.donate.savelife.core.requirement.model.Need;
import com.donate.savelife.core.requirement.model.Needs;
import com.donate.savelife.core.user.data.model.User;

import rx.Observable;

public interface NeedDatabase {

    Observable<Need> observeNeeds(User owner);

    Observable<Need> writeNewNeed(Need need);

    Observable<Need> observeNeed(String needID);

    Observable<Need> observeMyNeeds(User user);

    Observable<Integer> observerResponseCount(Need need);

    Observable<Integer> updateResponseCount(Need need, int count);

    Observable<Needs> observeLatestNeedsFor(User user);

}
