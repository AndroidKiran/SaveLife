package com.donate.savelife.notifications.database;

import com.donate.savelife.core.notifications.database.NotificationRegistrationDatabase;
import com.donate.savelife.core.notifications.model.Registrations;
import com.donate.savelife.rx.FirebaseObservableListeners;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

import rx.Observable;
import rx.functions.Func1;

/**
 * Created by ravi on 09/01/17.
 */

public class FirebaseNotificationRegistrationDatabase implements NotificationRegistrationDatabase {

    private final DatabaseReference notificationRegistrationDatabase;
    private final FirebaseObservableListeners firebaseObservableListeners;

    public FirebaseNotificationRegistrationDatabase(FirebaseDatabase firebaseDatabase, FirebaseObservableListeners firebaseObservableListeners) {
        notificationRegistrationDatabase = firebaseDatabase.getReference("NotificationRegistrations");
        this.firebaseObservableListeners = firebaseObservableListeners;
    }


    @Override
    public Observable<Registrations> observeRegistrations() {
        return firebaseObservableListeners.listenToValueEvents(notificationRegistrationDatabase, toRegistrations());
    }


    @Override
    public Observable<String> writeRegistration(String uid, String registrationId) {
        return firebaseObservableListeners.setValue(registrationId, notificationRegistrationDatabase.child(uid), registrationId);
    }


    private Func1<DataSnapshot, Registrations> toRegistrations() {
        return new Func1<DataSnapshot, Registrations>() {
            @Override
            public Registrations call(DataSnapshot dataSnapshot) {
                Iterable<DataSnapshot> children = dataSnapshot.getChildren();
                ArrayList<String> registrationList = new ArrayList<String>();
                for (DataSnapshot child : children) {
                    registrationList.add(child.getValue(String.class));
                }
                return new Registrations(registrationList);
            }
        };

    }
}
