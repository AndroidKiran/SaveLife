package com.donate.savelife.core.notifications.service;

import com.donate.savelife.core.database.DatabaseResult;
import com.donate.savelife.core.notifications.database.FCMRemoteMsg;
import com.donate.savelife.core.notifications.database.NotificationQueueDatabase;

import rx.Observable;
import rx.functions.Func1;

/**
 * Created by ravi on 10/01/17.
 */

public class PersistedNotificationQueueService implements NotificationQueueService {

    private final NotificationQueueDatabase notificationQueueDatabase;

    public PersistedNotificationQueueService(NotificationQueueDatabase notificationQueueDatabase){
        this.notificationQueueDatabase = notificationQueueDatabase;
    }

    @Override
    public Observable<DatabaseResult<FCMRemoteMsg>> writeIntoQueue(FCMRemoteMsg notificationQueue) {
        return notificationQueueDatabase.pushIntoQueue(notificationQueue)
                .map(asNotificationQueueDatabaseResult())
                .onErrorReturn(DatabaseResult.<FCMRemoteMsg>errorAsDatabaseResult());
    }


    private Func1<FCMRemoteMsg, DatabaseResult<FCMRemoteMsg>> asNotificationQueueDatabaseResult(){
        return new Func1<FCMRemoteMsg, DatabaseResult<FCMRemoteMsg>>() {
            @Override
            public DatabaseResult<FCMRemoteMsg> call(FCMRemoteMsg notificationQueue) {
                return new DatabaseResult<FCMRemoteMsg>(notificationQueue);
            }
        };
    }
}
