package com.donate.savelife.rx;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;

import rx.Observable;
import rx.Subscriber;
import rx.functions.Action0;
import rx.functions.Func1;
import rx.subscriptions.BooleanSubscription;

class ListenToChildEventsOnSubscribe<T> implements Observable.OnSubscribe<T> {

    private final Query query;
    private final Func1<DataSnapshot, T> marshaller;

    ListenToChildEventsOnSubscribe(Query query, Func1<DataSnapshot, T> marshaller) {
        this.query = query;
        this.marshaller = marshaller;
    }

    @Override
    public void call(Subscriber<? super T> subscriber) {
        final ChildEventListener eventListener = query.addChildEventListener(new RxValueListener<>(subscriber, marshaller));
        subscriber.add(BooleanSubscription.create(new Action0() {
            @Override
            public void call() {
                query.removeEventListener(eventListener);
            }
        }));
    }

    private static class RxValueListener<T> implements ChildEventListener {

        private final Subscriber<? super T> subscriber;
        private final Func1<DataSnapshot, T> marshaller;

        RxValueListener(Subscriber<? super T> subscriber, Func1<DataSnapshot, T> marshaller) {
            this.subscriber = subscriber;
            this.marshaller = marshaller;
        }

        @Override
        public void onChildAdded(DataSnapshot dataSnapshot, String s) {
            if (!subscriber.isUnsubscribed()) {
                subscriber.onNext(marshaller.call(dataSnapshot));
            }
        }

        @Override
        public void onChildChanged(DataSnapshot dataSnapshot, String s) {

        }

        @Override
        public void onChildRemoved(DataSnapshot dataSnapshot) {

        }

        @Override
        public void onChildMoved(DataSnapshot dataSnapshot, String s) {

        }

        @Override
        public void onCancelled(DatabaseError databaseError) {
            subscriber.onError(databaseError.toException()); //TODO handle errors in pipeline
        }

    }

}
