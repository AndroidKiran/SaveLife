package com.donate.savelife.chats.database;

import com.donate.savelife.core.UniqueList;
import com.donate.savelife.core.chats.database.ChatDatabase;
import com.donate.savelife.core.chats.model.Chat;
import com.donate.savelife.core.chats.model.Message;
import com.donate.savelife.core.requirement.model.Need;
import com.donate.savelife.rx.FirebaseObservableListeners;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

import rx.Observable;
import rx.functions.Func1;

public class FirebaseChatDatabase implements ChatDatabase {

    private final DatabaseReference messagesDB;
    private final FirebaseObservableListeners firebaseObservableListeners;

    public FirebaseChatDatabase(FirebaseDatabase firebaseDatabase, FirebaseObservableListeners firebaseObservableListeners) {
        messagesDB = firebaseDatabase.getReference("messages");
        messagesDB.keepSynced(true);
        this.firebaseObservableListeners = firebaseObservableListeners;
    }

    @Override
    public Observable<Chat> observeChat(Need need) {
        return firebaseObservableListeners.listenToValueEvents(messagesInChannel(need).limitToLast(DEFAULT_LIMIT), toChat());
    }

    @Override
    public Observable<Chat> observeMoreChat(Need need, Message message) {
        return firebaseObservableListeners.listenToValueEvents(messagesInChannel(need).orderByKey().endAt(message.getId()).limitToLast(DEFAULT_LIMIT), toChat());
    }

    @Override
    public Observable<Message> sendMessage(Need need, Message message) {
        return firebaseObservableListeners.setValue(message, messagesInChannel(need).push(), message);
    }

    @Override
    public Observable<List<String>> observerUserIdsFor(Need need) {
        return firebaseObservableListeners.listenToValueEvents(messagesInChannel(need).limitToLast(DEFAULT_LIMIT), getKeys());
    }

    @Override
    public Observable<List<String>> observerMoreUserIdsFor(Need need, Message message) {
        return firebaseObservableListeners.listenToValueEvents(messagesInChannel(need).orderByKey().endAt(message.getId()).limitToLast(DEFAULT_LIMIT), getKeys());
    }

    @Override
    public Observable<List<String>> observerChatUserIdsFor(Need need) {
        return firebaseObservableListeners.listenToValueEvents(messagesInChannel(need), getKeys());
    }


    private DatabaseReference messagesInChannel(Need need) {
        return messagesDB.child(need.getId());
    }

    private Func1<DataSnapshot, Chat> toChat() {
        return new Func1<DataSnapshot, Chat>() {
            @Override
            public Chat call(DataSnapshot dataSnapshot) {
                Iterable<DataSnapshot> children = dataSnapshot.getChildren();
                UniqueList<Message> messages = new UniqueList<Message>();
                for (DataSnapshot child : children) {
                    Message message = child.getValue(Message.class);
                    message.setId(child.getKey());
                    messages.add(message);
                }
                return new Chat(messages);
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
                        Message message = child.getValue(Message.class);
                        keys.add(message.getUserId());
                    }
                }
                return keys;
            }
        };
    }
}
