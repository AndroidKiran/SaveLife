package com.donate.savelife.notifications.database;

import com.donate.savelife.core.notifications.database.FCMRemoteMsg;
import com.donate.savelife.core.notifications.database.NotificationQueueDatabase;
import com.donate.savelife.rx.FirebaseObservableListeners;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import rx.Observable;

/**
 * Created by ravi on 10/01/17.
 */

public class FirebaseNotificatonQueueDatabase implements NotificationQueueDatabase {

    private final FirebaseObservableListeners firebaseObservableListeners;
    private final DatabaseReference notificationQueueDB;

    public FirebaseNotificatonQueueDatabase(FirebaseDatabase firebaseDatabase, FirebaseObservableListeners firebaseObservableListeners){
        notificationQueueDB = firebaseDatabase.getReference("notification_queue");
        this.firebaseObservableListeners = firebaseObservableListeners;
    }

    @Override
    public Observable<FCMRemoteMsg> pushIntoQueue(FCMRemoteMsg notificationQueue) {
        return firebaseObservableListeners.setValue(notificationQueue, notificationQueueDB.push(), notificationQueue);
    }
}
