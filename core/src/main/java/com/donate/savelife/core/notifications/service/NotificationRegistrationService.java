package com.donate.savelife.core.notifications.service;

import com.donate.savelife.core.database.DatabaseResult;
import com.donate.savelife.core.notifications.model.Registrations;
import com.donate.savelife.core.requirement.model.Need;
import com.donate.savelife.core.user.data.model.User;
import com.donate.savelife.core.user.data.model.Users;

import java.util.ArrayList;

import rx.Observable;

/**
 * Created by ravi on 09/01/17.
 */

public interface NotificationRegistrationService {

    Observable<DatabaseResult<Registrations>> observeRegistrations();

    Observable<DatabaseResult<String>> writeRegistration(String uid, String registrationId);

    Observable<DatabaseResult<ArrayList<String>>> observeRegistrationsForNeed(Need need);

    Observable<DatabaseResult<Users>> observeUserIdsFor(Need need);

    Observable<DatabaseResult<String>> observeRegIdFor(User user);


}
