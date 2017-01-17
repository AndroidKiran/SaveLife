package com.donate.savelife.core.notifications.database;

import rx.Observable;

/**
 * Created by ravi on 10/01/17.
 */

public interface NotificationQueueDatabase {

    Observable<FCMRemoteMsg> pushIntoQueue(FCMRemoteMsg fcmRemoteMsg);
}
