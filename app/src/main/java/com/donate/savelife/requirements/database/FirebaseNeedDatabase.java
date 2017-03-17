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

import rx.Observable;
import rx.functions.Func1;

public class FirebaseNeedDatabase implements NeedDatabase {
    private static final int DEFAULT_LIMIT = 4000;

    private final DatabaseReference needDB;
    private final FirebaseObservableListeners firebaseObservableListeners;

    public FirebaseNeedDatabase(FirebaseDatabase firebaseDatabase, FirebaseObservableListeners firebaseObservableListeners) {
        needDB = firebaseDatabase.getReference("needs");
        this.firebaseObservableListeners = firebaseObservableListeners;
    }

    @Override
    public Observable<Need> observeNeeds(User owner) {
        return firebaseObservableListeners.listenToChildEvents(needDB.orderByChild(User.CITY).equalTo(owner.getCity()), asNeed());
    }

    @Override
    public Observable<Need> writeNewNeed(Need need) {
        return firebaseObservableListeners.setValue(need, needDB.push(), need);
    }

    @Override
    public Observable<Need> observeNeed(String needID) {
        return firebaseObservableListeners.listenToValueEvents(needDB.child(needID), asNeed());
    }

    @Override
    public Observable<Need> observeMyNeeds(User user) {
        return firebaseObservableListeners.listenToChildEvents(needDB.orderByChild(Need.USER_ID).equalTo(user.getId()), asNeed());
    }

    @Override
    public Observable<Integer> observerResponseCount(Need need) {
        return firebaseObservableListeners.listenToValueEvents(needDB.child(need.getId()).child(Need.RESPONSE_COUNT), as(Integer.class));
    }

    @Override
    public Observable<Integer> updateResponseCount(Need need, int count) {
        return firebaseObservableListeners.setValue(count, needDB.child(need.getId()).child(Need.RESPONSE_COUNT), count);
    }

    @Override
    public Observable<Needs> observeLatestNeedsFor(User user) {
        return firebaseObservableListeners.listenToValueEvents(needDB.orderByChild(Need.USER_ID).endAt(user.getId()).limitToLast(1), toNeeds());
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

    private Func1<DataSnapshot, Need> asNeed(){
        return new Func1<DataSnapshot, Need>() {
            @Override
            public Need call(DataSnapshot dataSnapshot) {
                Need need = null;
                if (dataSnapshot.exists()){
                    need = dataSnapshot.getValue(Need.class);
                    need.setId(dataSnapshot.getKey());
                }
                return need;
            }
        };
    }

}
