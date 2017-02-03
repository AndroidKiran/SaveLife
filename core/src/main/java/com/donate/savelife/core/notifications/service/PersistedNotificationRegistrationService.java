package com.donate.savelife.core.notifications.service;

import com.donate.savelife.core.chats.database.ChatDatabase;
import com.donate.savelife.core.database.DatabaseResult;
import com.donate.savelife.core.notifications.database.NotificationRegistrationDatabase;
import com.donate.savelife.core.notifications.model.FcmRegistration;
import com.donate.savelife.core.notifications.model.Registrations;
import com.donate.savelife.core.requirement.model.Need;
import com.donate.savelife.core.user.data.model.User;
import com.donate.savelife.core.user.data.model.Users;
import com.donate.savelife.core.user.database.UserDatabase;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

import rx.Observable;
import rx.functions.Func1;
import rx.functions.Func2;

/**
 * Created by ravi on 09/01/17.
 */

public class PersistedNotificationRegistrationService implements NotificationRegistrationService {

    private final NotificationRegistrationDatabase notificationRegistrationDatabase;
    private final ChatDatabase chatDatabase;
    private final UserDatabase userDatabase;

    public PersistedNotificationRegistrationService(NotificationRegistrationDatabase notificationRegistrationDatabase,
                                                    ChatDatabase chatDatabase,
                                                    UserDatabase userDatabase) {
        this.notificationRegistrationDatabase = notificationRegistrationDatabase;
        this.chatDatabase = chatDatabase;
        this.userDatabase = userDatabase;
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

    @Override
    public Observable<DatabaseResult<ArrayList<String>>> observeRegistrationsForNeed(Need need) {
        return observeRegistrations().zipWith(observeUserIdsFor(need), getRegistrationIds(need))
                .map(new Func1<ArrayList<String>, DatabaseResult<ArrayList<String>>>() {
                    @Override
                    public DatabaseResult<ArrayList<String>> call(ArrayList<String> strings) {
                        return new DatabaseResult<ArrayList<String>>(strings);
                    }
                })
                .onErrorReturn(DatabaseResult.<ArrayList<String>>errorAsDatabaseResult());
    }

    @Override
    public Observable<DatabaseResult<Users>> observeUserIdsFor(Need need) {
        return chatDatabase.observerChatUserIdsFor(need)
                .flatMap(getUsersFromIds());
    }

    @Override
    public Observable<DatabaseResult<String>> observeRegIdFor(User user) {
        return notificationRegistrationDatabase.observeRegistrationIdForUser(user)
                .map(new Func1<String, DatabaseResult<String>>() {
                    @Override
                    public DatabaseResult<String> call(String s) {
                        return new DatabaseResult<String>(s);
                    }
                })
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

    private Func1<String, DatabaseResult<String>> asIdDatabaseResults() {
        return new Func1<String, DatabaseResult<String>>() {
            @Override
            public DatabaseResult<String> call(String s) {
                return new DatabaseResult<String>(s);
            }
        };
    }

    private Func1<List<String>, Observable<DatabaseResult<Users>>> getUsersFromIds() {
        return new Func1<List<String>, Observable<DatabaseResult<Users>>>() {
            @Override
            public Observable<DatabaseResult<Users>> call(List<String> userIds) {
                return Observable.from(userIds)
                        .flatMap(getUserFromId())
                        .toList()
                        .map(new Func1<List<User>, DatabaseResult<Users>>() {
                            @Override
                            public DatabaseResult<Users> call(List<User> users) {
                                return new DatabaseResult<>(new Users(users));
                            }
                        });
            }
        };
    }

    private Func1<String, Observable<User>> getUserFromId() {
        return new Func1<String, Observable<User>>() {
            @Override
            public Observable<User> call(final String userId) {
                return userDatabase.readUserFrom(UserDatabase.SINGLE_VALUE_EVENT_TYPE, userId);
            }
        };
    }

    private Func2<DatabaseResult<Registrations>, DatabaseResult<Users>, ArrayList<String>> getRegistrationIds(final Need need) {
        return new Func2<DatabaseResult<Registrations>, DatabaseResult<Users>, ArrayList<String>>() {
            @Override
            public ArrayList<String> call(DatabaseResult<Registrations> registrationsDatabaseResult, DatabaseResult<Users> usersDatabaseResult) {
                ArrayList<String> regIdList = new ArrayList<String>();
                ListIterator<FcmRegistration> fcmRegistrationListIterator = registrationsDatabaseResult.getData().getRegistrationList().listIterator();
                while (fcmRegistrationListIterator.hasNext()) {
                    FcmRegistration fcmRegistration = fcmRegistrationListIterator.next();
                    ListIterator<User> userListIterator = usersDatabaseResult.getData().getUsers().listIterator();
                    while (userListIterator.hasNext()) {
                        User user = userListIterator.next();
                        if (user.getId().equals(fcmRegistration.getUserId())) {
                            regIdList.add(fcmRegistration.getRegId());
                        }
                    }
                }
                return regIdList;
            }
        };
    }
}
