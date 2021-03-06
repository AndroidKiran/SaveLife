package com.donate.savelife.core.chats.service;

import com.donate.savelife.core.UniqueList;
import com.donate.savelife.core.chats.database.ChatDatabase;
import com.donate.savelife.core.chats.model.Chat;
import com.donate.savelife.core.chats.model.Message;
import com.donate.savelife.core.database.DatabaseResult;
import com.donate.savelife.core.requirement.model.Need;
import com.donate.savelife.core.user.data.model.Heroes;
import com.donate.savelife.core.user.data.model.User;
import com.donate.savelife.core.user.data.model.Users;
import com.donate.savelife.core.user.database.HeroDatabase;
import com.donate.savelife.core.user.database.UserDatabase;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.ListIterator;
import java.util.concurrent.Callable;

import rx.Observable;
import rx.functions.Func1;
import rx.functions.Func2;

public class PersistedChatService implements ChatService {

    private final ChatDatabase chatDatabase;
    private final UserDatabase userDatabase;
    private final HeroDatabase heroDatabase;

    public PersistedChatService(ChatDatabase chatDatabase, UserDatabase userDatabase, HeroDatabase heroDatabase) {
        this.chatDatabase = chatDatabase;
        this.userDatabase = userDatabase;
        this.heroDatabase = heroDatabase;
    }

    @Override
    public Observable<DatabaseResult<Chat>> observeChats(Need need) {
        return Observable.combineLatest(observeChat(need), observeUsersFor(need), mergeChat())
                .map(asReverseDatabaseResult())
                .onErrorReturn(DatabaseResult.<Chat>errorAsDatabaseResult());
    }

    @Override
    public Observable<DatabaseResult<Chat>> observeMoreChats(Need need, Message message) {
        return Observable.combineLatest(observeMoreChat(need, message), observeMoreUsersFor(need, message), mergeChat())
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
                .map(asMessageDatabaseResults())
                .onErrorReturn(DatabaseResult.<Message>errorAsDatabaseResult());
    }

    @Override
    public Observable<DatabaseResult<Users>> observeUsersFor(Need need) {
        return chatDatabase.observerUsersFor(need)
                .flatMap(getUsersFromIds());
    }

    @Override
    public Observable<DatabaseResult<Users>> observeMoreUsersFor(Need need, Message message) {
        return chatDatabase.observerMoreUsersFor(need, message)
                .flatMap(getUsersFromIds());
    }

    @Override
    public Observable<DatabaseResult<User>> observeUserFor(Need need) {
        return userDatabase.readUserFrom(UserDatabase.SINGLE_VALUE_EVENT_TYPE, need.getUserID())
                .map(aUserDatabaseResult());
    }

    @Override
    public Observable<DatabaseResult<Users>> observerChatUsersFor(Need need) {
        return chatDatabase.observerChatUsersFor(need)
                .flatMap(getUsersFromIds());
    }

    @Override
    public Observable<DatabaseResult<Users>> observeHeroes(Need need) {
        return Observable.combineLatest(observerChatUsersFor(need), observeHeroesFor(need), mergeHeroes())
                .onErrorReturn(DatabaseResult.<Users>errorAsDatabaseResult());
    }

    @Override
    public Observable<DatabaseResult<Heroes>> observeHeroesFor(final Need need) {
        return heroDatabase.observeNeedExists(need.getId())
                .flatMap(new Func1<Boolean, Observable<Heroes>>() {
                    @Override
                    public Observable<Heroes> call(Boolean aBoolean) {
                        if (aBoolean){
                            return heroDatabase.observeHeros(need.getId());
                        }
                        return Observable.fromCallable(new Callable<Heroes>() {
                            @Override
                            public Heroes call() throws Exception {
                                List<String> heroList = new ArrayList<String>();
                                return new Heroes(heroList);
                            }
                        });
                    }
                })
                .map(new Func1<Heroes, DatabaseResult<Heroes>>() {
                    @Override
                    public DatabaseResult<Heroes> call(Heroes heroes) {
                        return new DatabaseResult<Heroes>(heroes);
                    }
                })
                .onErrorReturn(DatabaseResult.<Heroes>errorAsDatabaseResult());
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

    private Func2<DatabaseResult<Chat>, DatabaseResult<Users>, Chat> mergeChat() {
        return new Func2<DatabaseResult<Chat>, DatabaseResult<Users>, Chat>() {
            @Override
            public Chat call(DatabaseResult<Chat> chatDatabaseResult, DatabaseResult<Users> usersDatabaseResult) {
                ListIterator<Message> chatListIterator = chatDatabaseResult.getData().getMessages().listIterator();
                while (chatListIterator.hasNext()) {
                    Message message = chatListIterator.next();
                    ListIterator<User> userListIterator = usersDatabaseResult.getData().getUsers().listIterator();
                    while (userListIterator.hasNext()) {
                        User user = userListIterator.next();
                        if (message.getUserID().equals(user.getId())) {
                            message.setAuthor(user);
                        }
                    }
                }
                return chatDatabaseResult.getData();
            }
        };
    }

    private Func2<DatabaseResult<Users>, DatabaseResult<Heroes>, DatabaseResult<Users>> mergeHeroes(){
        return new Func2<DatabaseResult<Users>, DatabaseResult<Heroes>, DatabaseResult<Users>>() {
            @Override
            public DatabaseResult<Users> call(DatabaseResult<Users> usersDatabaseResult, DatabaseResult<Heroes> heroesDatabaseResult) {
                ListIterator<User> userListIterator = usersDatabaseResult.getData().getUsers().listIterator();
                while (userListIterator.hasNext()){
                    User user = userListIterator.next();
                    ListIterator<String> stringListIterator = heroesDatabaseResult.getData().getHeroes().listIterator();
                    while (stringListIterator.hasNext()){
                        String userId = stringListIterator.next();
                        if (userId.equals(user.getId())){
                            userListIterator.remove();
                        }
                    }
                }
                return usersDatabaseResult;
            }
        };
    }


    private Func1<User, DatabaseResult<User>> aUserDatabaseResult() {
        return new Func1<User, DatabaseResult<User>>() {
            @Override
            public DatabaseResult<User> call(User user) {
                return new DatabaseResult<User>(user);
            }
        };
    }

    private Func1<Users, DatabaseResult<Users>> asUsersDatabaseResult(){
        return new Func1<Users, DatabaseResult<Users>>() {
            @Override
            public DatabaseResult<Users> call(Users users) {
                return new DatabaseResult<Users>(users);
            }
        };
    }

    private Func1<Message, DatabaseResult<Message>> asMessageDatabaseResults() {
        return new Func1<Message, DatabaseResult<Message>>() {
            @Override
            public DatabaseResult<Message> call(Message message) {
                return new DatabaseResult<Message>(message);
            }
        };
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

    public Chat reverse(Chat chat) {
        UniqueList<Message> reverseList = new UniqueList<Message>();
        reverseList.addAll(chat.getMessages());
        Collections.reverse(reverseList);
        return new Chat(reverseList);
    }
}
