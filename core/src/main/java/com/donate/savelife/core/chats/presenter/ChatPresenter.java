package com.donate.savelife.core.chats.presenter;


import android.net.Uri;
import android.os.Bundle;

import com.donate.savelife.core.analytics.Analytics;
import com.donate.savelife.core.analytics.ErrorLogger;
import com.donate.savelife.core.chats.displayer.ChatDisplayer;
import com.donate.savelife.core.chats.model.Chat;
import com.donate.savelife.core.chats.model.Message;
import com.donate.savelife.core.chats.service.ChatService;
import com.donate.savelife.core.database.DatabaseResult;
import com.donate.savelife.core.login.data.model.Authentication;
import com.donate.savelife.core.login.service.LoginService;
import com.donate.savelife.core.navigation.Navigator;
import com.donate.savelife.core.requirement.model.Need;
import com.donate.savelife.core.user.data.model.User;
import com.donate.savelife.core.utils.AppConstant;

import rx.Observable;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.functions.Func2;
import rx.subscriptions.CompositeSubscription;

import static com.donate.savelife.core.chats.presenter.ChatPresenter.Pair.asPair;


public class ChatPresenter {

    private final LoginService loginService;
    private final ChatService chatService;
    private final ChatDisplayer chatDisplayer;
    private final Analytics analytics;
    private final Need need;
    private final Navigator navigator;
    private final ErrorLogger errorLogger;

    private CompositeSubscription subscriptions = new CompositeSubscription();
    private User appOwner;
    private User postOwner;

    public ChatPresenter(
            LoginService loginService,
            ChatService chatService,
            ChatDisplayer chatDisplayer,
            Need need,
            Analytics analytics,
            Navigator navigator,
            ErrorLogger errorLogger
    ) {
        this.loginService = loginService;
        this.chatService = chatService;
        this.chatDisplayer = chatDisplayer;
        this.analytics = analytics;
        this.need = need;
        this.navigator = navigator;
        this.errorLogger = errorLogger;
    }

    public void initPresenter() {
        subscriptions.add(
                Observable.combineLatest(chatService.observeChats(need),loginService.getAuthentication(), asPair())
                        .subscribe(new Action1<Pair>() {
                            @Override
                            public void call(Pair pair) {
                                if (pair.auth.isSuccess()) {
                                    appOwner = pair.auth.getUser();
                                    displayChat(pair);
                                } else {
                                    errorLogger.reportError(pair.auth.getFailure(), "Not logged in when opening chat");
                                    navigator.toIntro();
                                }
                            }
                        })
        );
    }

    private void displayChat(ChatPresenter.Pair pair) {
        if (pair.chatResult.isSuccess()) {
            chatDisplayer.display(pair.chatResult.getData(), appOwner);
        } else {
            errorLogger.reportError(pair.chatResult.getFailure(), "Failed to fetch chat");
            chatDisplayer.displayError();
        }
    }

    private void displayMoreChat(ChatPresenter.Pair pair) {
        if (pair.chatResult.isSuccess()) {
            chatDisplayer.displayMore(pair.chatResult.getData(), appOwner);
        } else {
            errorLogger.reportError(pair.chatResult.getFailure(), "Failed to fetch more chat");
        }
    }

    public void startPresenting(){
        chatDisplayer.attach(actionListener);
        chatDisplayer.disableInteraction();
        subscriptions.add(
                chatService.observeUserFor(need)
                .subscribe(new Action1<DatabaseResult<User>>() {
                    @Override
                    public void call(DatabaseResult<User> userDatabaseResult) {
                        if (userDatabaseResult.isSuccess()){
                            postOwner = userDatabaseResult.getData();
                            chatDisplayer.setTitleLayout(need, postOwner);
                        } else {
                            errorLogger.reportError(userDatabaseResult.getFailure(), "Unable to fetch user");
                        }
                    }
                })
        );

        initPresenter();
    }


    public void stopPresenting() {
        chatDisplayer.detach(null);
        subscriptions.clear(); //TODO sort out checks
        subscriptions = new CompositeSubscription();
    }

    private boolean userIsAuthenticated() {
        return appOwner != null;
    }

    private final ChatDisplayer.ChatActionListener actionListener = new ChatDisplayer.ChatActionListener() {
        @Override
        public void onUpPressed() {
            navigator.toParent();
        }

        @Override
        public void onMessageLengthChanged(int messageLength) {
            if (userIsAuthenticated() && messageLength > 0) {
                chatDisplayer.enableInteraction();
            } else {
                chatDisplayer.disableInteraction();
            }
        }

        @Override
        public void onSubmitMessage(String message) {
            subscriptions.add(
                    chatService.sendMessage(need, new Message(appOwner.getId(), message))
                            .subscribe(new Action1<DatabaseResult<Message>>() {
                                @Override
                                public void call(DatabaseResult<Message> messageDatabaseResult) {
                                    if (messageDatabaseResult.isSuccess()) {
                                        chatDisplayer.scrollChat();
                                    } else {
                                        errorLogger.reportError(messageDatabaseResult.getFailure(), "Send msg failed");
                                    }
                                }
                            })

            );

            Bundle messageSentBundle = new Bundle();
            messageSentBundle.putString(Analytics.PARAM_OWNER_ID, appOwner.getId());
            messageSentBundle.putInt(Analytics.PARAM_MESSAGE_LENGTH, message.length());
            messageSentBundle.putString(Analytics.PARAM_BUTTON_NAME, AppConstant.SEND_MESSAGE_BUTTON);
            analytics.trackButtonClick(messageSentBundle);
        }

        @Override
        public void onLoadMore(Message message) {
            subscriptions.add(
                    Observable.combineLatest(chatService.observeMoreChats(need, message),loginService.getAuthentication(), asPair())
                            .subscribe(new Action1<Pair>() {
                                @Override
                                public void call(Pair pair) {
                                    if (pair.auth.isSuccess()) {
                                        appOwner = pair.auth.getUser();
                                        displayMoreChat(pair);
                                    } else {
                                        errorLogger.reportError(pair.auth.getFailure(), "Not logged in when opening chat");
                                        navigator.toIntro();
                                    }
                                }
                            })
            );
        }

        @Override
        public void onToolbarClick() {
            if (postOwner != null && need != null)
                chatDisplayer.showNeedDialog(need, postOwner);

            Bundle toolbarBarBundle = new Bundle();
            toolbarBarBundle.putString(Analytics.PARAM_OWNER_ID, appOwner.getId());
            toolbarBarBundle.putString(Analytics.PARAM_BUTTON_NAME, AppConstant.TOOLBAR);
            analytics.trackButtonClick(toolbarBarBundle);
        }

        @Override
        public void onCallClick(String mobileNum) {
            navigator.toDialNumber(mobileNum);

            Bundle callBundle = new Bundle();
            callBundle.putString(Analytics.PARAM_OWNER_ID, appOwner.getId());
            callBundle.putString(Analytics.PARAM_MOBILE_NUM, mobileNum);
            callBundle.putString(Analytics.PARAM_BUTTON_NAME, AppConstant.CALL_BUTTON);
            analytics.trackButtonClick(callBundle);
        }

        @Override
        public void onAddressClick(String address) {
            Uri gmmIntentUri = Uri.parse("geo:0,0?q=" + Uri.encode(address));
            navigator.toMap(gmmIntentUri);

            Bundle addressBundle = new Bundle();
            addressBundle.putString(Analytics.PARAM_OWNER_ID, appOwner.getId());
            addressBundle.putString(Analytics.PARAM_ADDRESS, address);
            addressBundle.putString(Analytics.PARAM_BUTTON_NAME, AppConstant.ADDRESS_BUTTON);
            analytics.trackButtonClick(addressBundle);
        }

        @Override
        public void onContentLoaded() {
            chatDisplayer.displayContent();
        }

        @Override
        public void onError() {
            chatDisplayer.displayError();
        }

        @Override
        public void onEmpty() {
            chatDisplayer.displayEmpty();
        }

        @Override
        public void onChatClicked(Message message) {
            if (appOwner.getId().equals(need.getUserID())){
                message.setNeedId(need.getId());
                navigator.toProfile(message);

                Bundle listItemBundle = new Bundle();
                listItemBundle.putString(Analytics.PARAM_MESSAGE_ID, message.getId());
                listItemBundle.putString(Analytics.PARAM_LIST_NAME, AppConstant.CHAT_LIST);
                analytics.trackListItemClick(listItemBundle);

            }
        }
    };

    static class Pair {

        public final DatabaseResult<Chat> chatResult;
        public final Authentication auth;

        private Pair(DatabaseResult<Chat> chatResult, Authentication auth) {
            this.chatResult = chatResult;
            this.auth = auth;
        }

        static Func2<DatabaseResult<Chat>, Authentication, Pair> asPair() {
            return new Func2<DatabaseResult<Chat>, Authentication, Pair>() {
                @Override
                public ChatPresenter.Pair call(DatabaseResult<Chat> chatDatabaseResult, Authentication authentication) {
                    return new ChatPresenter.Pair(chatDatabaseResult, authentication);
                }
            };
        }

    }


    public void onSaveInstanceState(Bundle outState) {
        outState.putParcelable(AppConstant.SAVED_LIST, chatDisplayer.getChat());
        outState.putParcelable(AppConstant.SAVED_USER, chatDisplayer.getUser());
        outState.putParcelable(AppConstant.SAVED_LAST_ITEM, chatDisplayer.getLastMessage());
    }

    private Func1<Chat, DatabaseResult<Chat>> toChats() {
        return new Func1<Chat, DatabaseResult<Chat>>() {
            @Override
            public DatabaseResult<Chat> call(Chat chat) {
                return new DatabaseResult<Chat>(chat);
            }
        };
    }
}
