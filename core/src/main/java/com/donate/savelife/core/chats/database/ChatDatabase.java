package com.donate.savelife.core.chats.database;


import com.donate.savelife.core.chats.model.Chat;
import com.donate.savelife.core.chats.model.Message;
import com.donate.savelife.core.requirement.model.Need;

import java.util.List;

import rx.Observable;

public interface ChatDatabase {

    Observable<Chat> observeChat(Need need);

    Observable<Chat> observeMoreChat(Need need, Message message);

    Observable<Message> sendMessage(Need need, Message message);

    Observable<List<String>> observerUserIdsFor(Need need);

    Observable<List<String>> observerMoreUserIdsFor(Need need, Message message);

}
