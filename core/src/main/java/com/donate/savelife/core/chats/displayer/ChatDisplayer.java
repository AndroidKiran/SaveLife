package com.donate.savelife.core.chats.displayer;


import com.donate.savelife.core.chats.model.Chat;
import com.donate.savelife.core.chats.model.Message;
import com.donate.savelife.core.requirement.model.Need;
import com.donate.savelife.core.user.data.model.User;

public interface ChatDisplayer {

    void attach(ChatActionListener actionListener);

    void detach(ChatActionListener actionListener);

    void setTitle(String title);

    void display(Chat chat, User user);

    void displayMore(Chat chat, User user);

    void enableInteraction();

    void disableInteraction();

    void setTitleLayout(Need need, User user);

    void scrollChat();

    void showNeedDialog(Need need, User user);

    void dismissNeedDialog();

    void displayLoading();

    void displayContent();

    void displayError();

    void displayEmpty();

    //Restore methods
    Chat getChat();

    User getUser();

    Message getLastMessage();

    void setLastMessage(Message lastMessage);

    interface ChatActionListener {

        void onUpPressed();

        void onMessageLengthChanged(int messageLength);

        void onSubmitMessage(String message);

        void onLoadMore(Message message);

        void onToolbarClick();

        void onCallClick(String mobileNum);

        void onAddressClick(String address);

        void onContentLoaded();

        void onError();

        void onEmpty();

        void onChatClicked(Message message);

    }

}
