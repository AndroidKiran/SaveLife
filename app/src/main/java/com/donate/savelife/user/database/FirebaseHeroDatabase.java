package com.donate.savelife.user.database;

import com.donate.savelife.core.UniqueList;
import com.donate.savelife.core.user.data.model.Heroes;
import com.donate.savelife.core.user.database.HeroDatabase;
import com.donate.savelife.rx.FirebaseObservableListeners;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

import rx.Observable;
import rx.functions.Func1;

/**
 * Created by ravi on 07/12/16.
 */

public class FirebaseHeroDatabase implements HeroDatabase {

    private final DatabaseReference heroDB;
    private final FirebaseObservableListeners firebaseObservableListeners;
    private final FirebaseDatabase firebaseDatabase;

    public FirebaseHeroDatabase(FirebaseDatabase firebaseDatabase, FirebaseObservableListeners firebaseObservableListeners) {
        this.firebaseDatabase = firebaseDatabase;
        heroDB = firebaseDatabase.getReference("heroes");
        this.firebaseObservableListeners = firebaseObservableListeners;
    }

    @Override
    public Observable<Heroes> observeHeros(String needID) {
        return firebaseObservableListeners.listenToSingleValueEvents(heroDB.child(needID), toHeroes());
    }

    @Override
    public Observable<Boolean> observeHeroFrom(String needID, String userID) {
        return firebaseObservableListeners.listenToValueEvents(heroDB, isHeroExists(needID, userID));
    }

    @Override
    public Observable<String> saveHero(String needId, String userID) {
        return firebaseObservableListeners.setValue(userID, heroDB.child(needId).child(userID), userID);
    }

    @Override
    public Observable<Boolean> observeNeedExists(String needID) {
        return firebaseObservableListeners.listenToSingleValueEvents(heroDB, isNeedExists(needID));
    }

    private Func1<DataSnapshot, Heroes> toHeroes() {
        return new Func1<DataSnapshot, Heroes>() {
            @Override
            public Heroes call(DataSnapshot dataSnapshot) {
                Iterable<DataSnapshot> children = dataSnapshot.getChildren();
                List<String> heroes = new UniqueList<String>();
                for (DataSnapshot child : children) {
                    heroes.add(child.getKey());
                }
                return new Heroes(heroes);
            }
        };
    }

    private Func1<DataSnapshot, Boolean> isHeroExists(final String needID, final String userID) {
        return new Func1<DataSnapshot, Boolean>() {
            @Override
            public Boolean call(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    DataSnapshot needSnapshot = dataSnapshot.child(needID);
                    if (needSnapshot.exists()) {
                        DataSnapshot userSnapshot = needSnapshot.child(userID);
                        if (userSnapshot.exists()) {
                            return true;
                        }
                    }
                }
                return false;
            }
        };
    }

    private Func1<DataSnapshot, Boolean> isNeedExists(final String needID) {
        return new Func1<DataSnapshot, Boolean>() {
            @Override
            public Boolean call(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    DataSnapshot needSnapshot = dataSnapshot.child(needID);
                    if (needSnapshot.exists()) {
                        return true;
                    }
                }
                return false;
            }
        };
    }
}
