package com.donate.savelife.requirements.database;

import com.donate.savelife.core.UniqueList;
import com.donate.savelife.core.requirement.database.NeedDatabase;
import com.donate.savelife.core.requirement.model.Need;
import com.donate.savelife.core.requirement.model.Needs;
import com.donate.savelife.core.user.data.model.User;
import com.donate.savelife.rx.FirebaseObservableListeners;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

import rx.Observable;
import rx.functions.Func1;

public class FirebaseNeedDatabase implements NeedDatabase {
    private static final int DEFAULT_LIMIT = 1000;

    private final DatabaseReference needDB;
    private final FirebaseObservableListeners firebaseObservableListeners;

    public FirebaseNeedDatabase(FirebaseDatabase firebaseDatabase, FirebaseObservableListeners firebaseObservableListeners) {
        needDB = firebaseDatabase.getReference("needs");
        needDB.keepSynced(true);
        this.firebaseObservableListeners = firebaseObservableListeners;
    }

    @Override
    public Observable<Needs> observeNeeds(User user) {
        return firebaseObservableListeners.listenToValueEvents(needDB.limitToLast(DEFAULT_LIMIT), toNeeds());
    }

    @Override
    public Observable<List<String>> observerUserIdsFor(User user) {
        return firebaseObservableListeners.listenToValueEvents(needDB.limitToLast(DEFAULT_LIMIT), getKeys());
    }

    @Override
    public Observable<Needs> observeMoreNeeds(User user, Need need) {
        return firebaseObservableListeners.listenToValueEvents(needDB.orderByKey().endAt(need.getId()).limitToLast(DEFAULT_LIMIT), toNeeds());
    }

    @Override
    public Observable<List<String>> observerMoreUserIdsFor(User user, Need need) {
        return firebaseObservableListeners.listenToValueEvents(needDB.orderByKey().endAt(need.getId()).limitToLast(DEFAULT_LIMIT), getKeys());
    }

    @Override
    public Observable<Need> writeNewNeed(Need need) {
        return firebaseObservableListeners.setValue(need, needDB.push(), need);
    }

    @Override
    public Observable<Need> observeNeed(String needID) {
        return firebaseObservableListeners.listenToValueEvents(needDB.child(needID), as(Need.class));
    }

    @Override
    public Observable<Needs> observeLatestNeedFor(User user) {
        return null;
    }

    private Func1<DataSnapshot, Needs> toNeeds(){
        return new Func1<DataSnapshot, Needs>() {
            @Override
            public Needs call(DataSnapshot dataSnapshot) {
                Iterable<DataSnapshot> children = dataSnapshot.getChildren();
                UniqueList<Need> needs = new UniqueList<Need>();
                for (DataSnapshot child : children){
                    Need need = child.getValue(Need.class);
                    need.setId(child.getKey());
                    needs.add(need);
                }
                return new Needs(needs);
            }
        };
    }

    private <T> Func1<DataSnapshot, T> as(final Class<T> tClass) {
        return new Func1<DataSnapshot, T>() {
            @Override
            public T call(DataSnapshot dataSnapshot) {
                return dataSnapshot.getValue(tClass);
            }
        };
    }

    private static Func1<DataSnapshot, List<String>> getKeys() {
        return new Func1<DataSnapshot, List<String>>() {
            @Override
            public List<String> call(DataSnapshot dataSnapshot) {
                UniqueList<String> keys = new UniqueList<String>();
                if (dataSnapshot.hasChildren()) {
                    Iterable<DataSnapshot> children = dataSnapshot.getChildren();
                    for (DataSnapshot child : children) {
                        Need need = child.getValue(Need.class);
                        keys.add(need.getUserID());
                    }
                }
                return keys;
            }
        };
    }

}
