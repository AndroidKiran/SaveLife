package com.donate.savelife.launcher.database;

import com.donate.savelife.core.launcher.database.AppStatusDatabase;
import com.donate.savelife.core.launcher.model.AppStatus;
import com.donate.savelife.rx.FirebaseObservableListeners;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import rx.Observable;
import rx.functions.Func1;

/**
 * Created by ravi on 03/02/17.
 */

public class FirebaseAppStatusDatabase implements AppStatusDatabase {

    private final FirebaseObservableListeners firebaseObservableListeners;
    private final DatabaseReference appStatusDB;

    public FirebaseAppStatusDatabase(FirebaseDatabase firebaseDatabase, FirebaseObservableListeners firebaseObservableListeners){
        this.appStatusDB = firebaseDatabase.getReference("app_status");
        appStatusDB.keepSynced(true);
        this.firebaseObservableListeners = firebaseObservableListeners;
    }
    @Override
    public Observable<AppStatus> observerLatestStatus() {
        return firebaseObservableListeners.listenToValueEvents(appStatusDB.orderByKey().limitToLast(1), asAppStatus());
    }

    private Func1<DataSnapshot, AppStatus> asAppStatus(){
        return new Func1<DataSnapshot, AppStatus>() {
            @Override
            public AppStatus call(DataSnapshot dataSnapshot) {
                AppStatus appStatus = null;
                if (dataSnapshot.exists()){
                    appStatus = dataSnapshot.getValue(AppStatus.class);
                    appStatus.setId(dataSnapshot.getKey());
                }
                return appStatus;
            }
        };
    }
}
