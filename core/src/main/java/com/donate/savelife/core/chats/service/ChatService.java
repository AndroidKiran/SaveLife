package com.donate.savelife.core.chats.service;


import com.donate.savelife.core.chats.model.Chat;
import com.donate.savelife.core.chats.model.Message;
import com.donate.savelife.core.database.DatabaseResult;
import com.donate.savelife.core.requirement.model.Need;
import com.donate.savelife.core.user.data.model.User;
import com.donate.savelife.core.user.data.model.Users;

import rx.Observable;

public interface ChatService {

    Observable<DatabaseResult<Chat>> observeChats(Need need);

    Observable<DatabaseResult<Chat>> observeMoreChats(Need need, Message message);

    Observable<DatabaseResult<Chat>> observeChat(Need need);

    Observable<DatabaseResult<Chat>> observeMoreChat(Need need, Message message);

    Observable <DatabaseResult<Message>> sendMessage(Need need, Message message);

    Observable<DatabaseResult<Users>> observeUserIdsFor(Need need);

    Observable<DatabaseResult<Users>> observeMoreUserIdsFor(Need need, Message message);

    Observable<DatabaseResult<User>> observeUserFor(Need need);

}
