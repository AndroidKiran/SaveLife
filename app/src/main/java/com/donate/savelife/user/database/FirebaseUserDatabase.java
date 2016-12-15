package com.donate.savelife.user.database;

import com.donate.savelife.core.user.data.model.User;
import com.donate.savelife.core.user.data.model.Users;
import com.donate.savelife.core.user.database.UserDatabase;
import com.donate.savelife.rx.FirebaseObservableListeners;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.functions.Func1;

public class FirebaseUserDatabase implements UserDatabase {

    private final DatabaseReference usersDB;
    private final FirebaseObservableListeners firebaseObservableListeners;

    public FirebaseUserDatabase(FirebaseDatabase firebaseDatabase, FirebaseObservableListeners firebaseObservableListeners) {
        usersDB = firebaseDatabase.getReference("users");
        this.firebaseObservableListeners = firebaseObservableListeners;
    }

    @Override
    public Observable<Users> observeTopHeros() {
        return firebaseObservableListeners.listenToValueEvents(usersDB.orderByChild(User.LIFE_COUNT).limitToLast(100), toUsers());
    }

    @Override
    public Observable<User> readUserFrom(String userId) {
        return firebaseObservableListeners.listenToSingleValueEvents(usersDB.child(userId), asUser());
    }

    @Override
    public Observable<User> observeUser(String userId) {
        return firebaseObservableListeners.listenToValueEvents(usersDB.child(userId), asUser());
    }

    @Override
    public Observable<User> updateUser(User user) {
        return firebaseObservableListeners.setValue(user, usersDB.child(user.getId()), user);
    }

    @Override
    public Observable<User> updateTheLifeCount(User user) {
        return firebaseObservableListeners.setValue(user.getLifeCount() + 1, usersDB.child(user.getId()).child("lifeCount"), user);
    }

    @Override
    public void writeCurrentUser(User user) {
        usersDB.child(user.getId()).setValue(user); //TODO handle errors
    }

    private Func1<DataSnapshot, Users> toUsers() {
        return new Func1<DataSnapshot, Users>() {
            @Override
            public Users call(DataSnapshot dataSnapshot) {
                Iterable<DataSnapshot> children = dataSnapshot.getChildren();
                List<User> users = new ArrayList<>();
                for (DataSnapshot child : children) {
                    User message = child.getValue(User.class);
                    users.add(message);
                }
                return new Users(users);
            }
        };
    }

    private Func1<DataSnapshot, User> asUser(){
        return new Func1<DataSnapshot, User>() {
            @Override
            public User call(DataSnapshot dataSnapshot) {
                User user = new User();
                User userFromDataSnapshot = dataSnapshot.getValue(User.class);
                if (userFromDataSnapshot != null){
                    user = userFromDataSnapshot;
                }
                return user;
            }
        };
    }
}
