package com.donate.savelife.user.database;

import android.text.TextUtils;

import com.donate.savelife.core.UniqueList;
import com.donate.savelife.core.user.data.model.Heros;
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

    public FirebaseHeroDatabase(FirebaseDatabase firebaseDatabase, FirebaseObservableListeners firebaseObservableListeners) {
        heroDB = firebaseDatabase.getReference("heros");
        this.firebaseObservableListeners = firebaseObservableListeners;
    }

    @Override
    public Observable<Heros> observeHeros(String needID) {
        return firebaseObservableListeners.listenToSingleValueEvents(heroDB.child(needID), toHeros());
    }

    @Override
    public Observable<String> observeHeroFrom(String needID, String userID) {
        return firebaseObservableListeners.listenToValueEvents(heroDB.child(needID).child(userID), asHero());
    }

    @Override
    public Observable<String> saveHero(String needId, String userID) {
        return firebaseObservableListeners.setValue(userID, heroDB.child(needId).child(userID), userID);
    }

    private Func1<DataSnapshot, Heros> toHeros() {
        return new Func1<DataSnapshot, Heros>() {
            @Override
            public Heros call(DataSnapshot dataSnapshot) {
                Iterable<DataSnapshot> children = dataSnapshot.getChildren();
                List<String> heros = new UniqueList<String>();
                for (DataSnapshot child : children) {
                    String heroID = child.getValue(String.class);
                    heros.add(heroID);
                }
                return new Heros(heros);
            }
        };
    }

    private Func1<DataSnapshot, String> toHero() {
        return new Func1<DataSnapshot, String>() {
            @Override
            public String call(DataSnapshot dataSnapshot) {
                String heroID = "";
                Iterable<DataSnapshot> children = dataSnapshot.getChildren();
                for (DataSnapshot child : children) {
                    heroID = child.getValue(String.class);
                }
                return heroID;
            }
        };
    }

//    private <T> Func1<DataSnapshot, T> as(final Class<T> tClass) {
//        return new Func1<DataSnapshot, T>() {
//            @Override
//            public T call(DataSnapshot dataSnapshot) {
//                return dataSnapshot.getValue(tClass);
//            }
//        };
//    }

    private Func1<DataSnapshot, String> asHero(){
        return new Func1<DataSnapshot, String>() {
            @Override
            public String call(DataSnapshot dataSnapshot) {
                String userID = "";
                String uid = dataSnapshot.getValue(String.class);
                if (!TextUtils.isEmpty(uid)){
                    userID = uid;
                }
                return userID;
            }
        };
    }


}
