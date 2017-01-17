package com.donate.savelife.core.notifications.service;

import com.donate.savelife.core.database.DatabaseResult;
import com.donate.savelife.core.notifications.database.FCMRemoteMsg;

import rx.Observable;

/**
 * Created by ravi on 10/01/17.
 */

public interface NotificationQueueService {
    Observable<DatabaseResult<FCMRemoteMsg>> writeIntoQueue(FCMRemoteMsg fcmRemoteMsg);
}
