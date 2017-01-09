package com.donate.savelife.core.notifications.service;

import com.donate.savelife.core.database.DatabaseResult;
import com.donate.savelife.core.notifications.database.NotificationRegistrationDatabase;
import com.donate.savelife.core.notifications.model.Registrations;

import rx.Observable;
import rx.functions.Func1;

/**
 * Created by ravi on 09/01/17.
 */

public class PersistedNotificationRegistrationService implements NotificationRegistrationService {

    private final NotificationRegistrationDatabase notificationRegistrationDatabase;

    public PersistedNotificationRegistrationService(NotificationRegistrationDatabase  notificationRegistrationDatabase){
        this.notificationRegistrationDatabase = notificationRegistrationDatabase;
    }

    @Override
    public Observable<DatabaseResult<Registrations>> observeRegistrations() {
        return notificationRegistrationDatabase.observeRegistrations()
                .map(asRegistrationDatabaseResults())
                .onErrorReturn(DatabaseResult.<Registrations>errorAsDatabaseResult());
    }

    @Override
    public Observable<DatabaseResult<String>> writeRegistration(String id, String registrationId) {
        return notificationRegistrationDatabase.writeRegistration(id, registrationId)
                .map(asIdDatabaseResults())
                .onErrorReturn(DatabaseResult.<String>errorAsDatabaseResult());
    }

    private Func1<Registrations, DatabaseResult<Registrations>> asRegistrationDatabaseResults() {
        return new Func1<Registrations, DatabaseResult<Registrations>>() {
            @Override
            public DatabaseResult<Registrations> call(Registrations Registrations) {
                return new DatabaseResult<Registrations>(Registrations);
            }
        };
    }

    private Func1<String, DatabaseResult<String>> asIdDatabaseResults(){
        return new Func1<String, DatabaseResult<String>>() {
            @Override
            public DatabaseResult<String> call(String s) {
                return new DatabaseResult<String>(s);
            }
        };
    }
}
