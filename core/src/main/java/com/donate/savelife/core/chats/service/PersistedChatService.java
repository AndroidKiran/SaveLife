package com.donate.savelife.core.chats.service;

import com.donate.savelife.core.UniqueList;
import com.donate.savelife.core.chats.database.ChatDatabase;
import com.donate.savelife.core.chats.model.Chat;
import com.donate.savelife.core.chats.model.Message;
import com.donate.savelife.core.database.DatabaseResult;
import com.donate.savelife.core.requirement.model.Need;
import com.donate.savelife.core.user.data.model.User;
import com.donate.savelife.core.user.data.model.Users;
import com.donate.savelife.core.user.database.UserDatabase;

import java.util.Collections;
import java.util.List;

import rx.Observable;
import rx.functions.Func1;
import rx.functions.Func2;

public class PersistedChatService implements ChatService {

    private final ChatDatabase chatDatabase;
    private final UserDatabase userDatabase;

    public PersistedChatService(ChatDatabase chatDatabase, UserDatabase userDatabase) {
        this.chatDatabase = chatDatabase;
        this.userDatabase = userDatabase;
    }

    @Override
    public Observable<DatabaseResult<Chat>> observeChats(Need need) {
        return Observable.combineLatest(observeChat(need),observeUserIdsFor(need), mergeChat())
                .map(asReverseDatabaseResult())
                .onErrorReturn(DatabaseResult.<Chat>errorAsDatabaseResult());
    }

    @Override
    public Observable<DatabaseResult<Chat>> observeMoreChats(Need need, Message message) {
        return Observable.combineLatest(observeMoreChat(need, message), observeMoreUserIdsFor(need, message), mergeChat())
                .map(asReverseDatabaseResult())
                .onErrorReturn(DatabaseResult.<Chat>errorAsDatabaseResult());
    }

    @Override
    public Observable<DatabaseResult<Chat>> observeChat(final Need need) {
        return chatDatabase.observeChat(need)
                .map(asDatabaseResult())
                .onErrorReturn(DatabaseResult.<Chat>errorAsDatabaseResult());
    }

    @Override
    public Observable<DatabaseResult<Chat>> observeMoreChat(Need need, Message message) {
        return chatDatabase.observeMoreChat(need, message)
                .map(asDatabaseResult())
                .onErrorReturn(DatabaseResult.<Chat>errorAsDatabaseResult());
    }

    @Override
    public Observable<DatabaseResult<Message>> sendMessage(Need need, Message message) {
        return chatDatabase.sendMessage(need, message)
                .map(new Func1<Message, DatabaseResult<Message>>() {
                    @Override
                    public DatabaseResult<Message> call(Message message) {
                        return new DatabaseResult<Message>(message);
                    }
                })
                .onErrorReturn(DatabaseResult.<Message>errorAsDatabaseResult());
    }

    @Override
    public Observable<DatabaseResult<Users>> observeUserIdsFor(Need need) {
        return chatDatabase.observerUserIdsFor(need)
                .flatMap(getUsersFromIds());
    }

    @Override
    public Observable<DatabaseResult<Users>> observeMoreUserIdsFor(Need need, Message message) {
        return chatDatabase.observerMoreUserIdsFor(need, message)
                .flatMap(getUsersFromIds());
    }

    @Override
    public Observable<DatabaseResult<User>> observeUserFor(Need need) {
        return userDatabase.readUserFrom(UserDatabase.SINGLE_VALUE_EVENT_TYPE, need.getUserID())
                .map(aUsersDatabaseResult());
    }

    private Func1<Chat, DatabaseResult<Chat>> asDatabaseResult() {
        return new Func1<Chat, DatabaseResult<Chat>>() {
            @Override
            public DatabaseResult<Chat> call(Chat chat) {
                return new DatabaseResult<Chat>(chat);
            }
        };
    }

    private Func1<Chat, DatabaseResult<Chat>> asReverseDatabaseResult() {
        return new Func1<Chat, DatabaseResult<Chat>>() {
            @Override
            public DatabaseResult<Chat> call(Chat chat) {
                return new DatabaseResult<Chat>(reverse(chat));
            }
        };
    }


    private Func1<List<String>, Observable<DatabaseResult<Users>>> getUsersFromIds() {
        return new Func1<List<String>, Observable<DatabaseResult<Users>>>() {
            @Override
            public Observable<DatabaseResult<Users>> call(List<String> userIds) {
                return Observable.from(userIds)
                        .flatMap(getUserFromId())
                        .toList()
                        .map(new Func1<List<User>, DatabaseResult<Users>>() {
                            @Override
                            public DatabaseResult<Users> call(List<User> users) {
                                return new DatabaseResult<>(new Users(users));
                            }
                        });
            }
        };
    }

    private Func1<String, Observable<User>> getUserFromId() {
        return new Func1<String, Observable<User>>() {
            @Override
            public Observable<User> call(final String userId) {
                return userDatabase.readUserFrom(UserDatabase.SINGLE_VALUE_EVENT_TYPE, userId);
            }
        };
    }

    private Func2<DatabaseResult<Chat>, DatabaseResult<Users>, Chat> mergeChat(){
       return new Func2<DatabaseResult<Chat>, DatabaseResult<Users>, Chat>() {
           @Override
           public Chat call(DatabaseResult<Chat> chatDatabaseResult, DatabaseResult<Users> usersDatabaseResult) {
               UniqueList<Message> messages = new UniqueList<Message>();
               for (Message message : chatDatabaseResult.getData().getMessages()){
                   for (User user : usersDatabaseResult.getData().getUsers()){
                       if (message.getUserId().equals(user.getId())){
                           message.setAuthor(user);
                           messages.add(message);
                       }
                   }
               }
               return new Chat(messages);
           }
       };
    }

    public Chat reverse(Chat chat) {
        UniqueList<Message> reverseList = new UniqueList<Message>();
        reverseList.addAll(chat.getMessages());
        Collections.reverse(reverseList);
        return new Chat(reverseList);
    }

    private Func1<User, DatabaseResult<User>> aUsersDatabaseResult() {
       return new Func1<User, DatabaseResult<User>>() {
           @Override
           public DatabaseResult<User> call(User user) {
               return new DatabaseResult<User>(user);
           }
       };
    }


}
