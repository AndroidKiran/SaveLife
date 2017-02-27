package com.donate.savelife.core.chats.database;


import com.donate.savelife.core.chats.model.Chat;
import com.donate.savelife.core.chats.model.Message;
import com.donate.savelife.core.requirement.model.Need;

import java.util.List;

import rx.Observable;

public interface ChatDatabase {

    public static final int DEFAULT_LIMIT = 1000;

    Observable<Chat> observeChat(Need need);

    Observable<Chat> observeMoreChat(Need need, Message message);

    Observable<Message> sendMessage(Need need, Message message);

    Observable<List<String>> observerUsersFor(Need need);

    Observable<List<String>> observerMoreUsersFor(Need need, Message message);

    Observable<List<String>> observerChatUsersFor(Need need);


}
