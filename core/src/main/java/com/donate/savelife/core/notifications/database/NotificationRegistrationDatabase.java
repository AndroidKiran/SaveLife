package com.donate.savelife.core.notifications.database;

import com.donate.savelife.core.notifications.model.Registrations;
import com.donate.savelife.core.user.data.model.User;

import rx.Observable;

/**
 * Created by ravi on 09/01/17.
 */

public interface NotificationRegistrationDatabase {

    Observable <Registrations> observeRegistrations();

    Observable<String> writeRegistration(String uid, String registartionId);

    Observable<String> observeRegistrationIdForUser(User user);
}
