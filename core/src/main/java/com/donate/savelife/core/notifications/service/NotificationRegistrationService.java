package com.donate.savelife.core.notifications.service;

import com.donate.savelife.core.database.DatabaseResult;
import com.donate.savelife.core.notifications.model.Registrations;

import rx.Observable;

/**
 * Created by ravi on 09/01/17.
 */

public interface NotificationRegistrationService {

    Observable<DatabaseResult<Registrations>> observeRegistrations();

    Observable<DatabaseResult<String>> writeRegistration(String uid, String registrationId);

}
