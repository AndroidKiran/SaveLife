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
import com.donate.savelife.core.utils.UtilBundles;

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
    private User user;
    private User needPostedByUser;

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
        chatDisplayer.attach(actionListener);
        chatDisplayer.disableInteraction();

        subscriptions.add(
                Observable.combineLatest(chatService.observeChats(need), loginService.getAuthentication(), asPair())
                        .subscribe(new Action1<Pair>() {
                            @Override
                            public void call(Pair pair) {
                                if (pair.auth.isSuccess()) {
                                    user = pair.auth.getUser();
                                    displayChat(pair);
                                } else {
                                    errorLogger.reportError(pair.auth.getFailure(), "Not logged in when opening chat");
                                    navigator.toLogin();
                                }
                            }
                        })
        );
    }

    private void displayChat(ChatPresenter.Pair pair) {
        if (pair.chatResult.isSuccess()) {
            chatDisplayer.display(pair.chatResult.getData(), user);
        } else {
            errorLogger.reportError(pair.chatResult.getFailure(), "Failed to fetch chat");
            chatDisplayer.displayError();
        }
    }

    private void displayMoreChat(ChatPresenter.Pair pair) {
        if (pair.chatResult.isSuccess()) {
            chatDisplayer.displayMore(pair.chatResult.getData(), user);
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
                            needPostedByUser = userDatabaseResult.getData();
                            chatDisplayer.setTitleLayout(need, needPostedByUser);
                        } else {
                            errorLogger.reportError(userDatabaseResult.getFailure(), "Unable to fetch user");
                        }
                    }
                })
        );
    }


    public void stopPresenting() {
        chatDisplayer.detach(null);
        subscriptions.clear(); //TODO sort out checks
        subscriptions = new CompositeSubscription();
    }

    private boolean userIsAuthenticated() {
        return user != null;
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
            analytics.trackMessageLength(message.length(), user.getId(), need.getId());
            subscriptions.add(
                    chatService.sendMessage(need, new Message(user.getId(), message))
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
        }

        @Override
        public void onLoadMore(Message message) {
            subscriptions.add(
                    Observable.combineLatest(chatService.observeMoreChats(need, message), loginService.getAuthentication(), asPair())
                            .subscribe(new Action1<Pair>() {
                                @Override
                                public void call(Pair pair) {
                                    if (pair.auth.isSuccess()) {
                                        user = pair.auth.getUser();
                                        displayMoreChat(pair);
                                    } else {
                                        errorLogger.reportError(pair.auth.getFailure(), "Not logged in when opening chat");
                                        navigator.toLogin();
                                    }
                                }
                            })
            );
        }

        @Override
        public void onToolbarClick() {
            if (needPostedByUser != null && need != null)
                chatDisplayer.showNeedDialog(need, needPostedByUser);
        }

        @Override
        public void onCallClick(String mobileNum) {
            navigator.toDialNumber(mobileNum);
        }

        @Override
        public void onAddressClick(String address) {
            Uri gmmIntentUri = Uri.parse("geo:0,0?q=" + Uri.encode(address));
            navigator.toMap(gmmIntentUri);
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
            message.setNeedId(need.getId());
            navigator.toProfile(message.getNeedId(), message.getUserId());
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
        outState.putParcelable(UtilBundles.SAVED_LIST, chatDisplayer.getChat());
        outState.putParcelable(UtilBundles.SAVED_USER, chatDisplayer.getUser());
        outState.putParcelable(UtilBundles.SAVED_LAST_ITEM, chatDisplayer.getLastMessage());
    }

    private Func1<Chat, DatabaseResult<Chat>> toChats() {
        return new Func1<Chat, DatabaseResult<Chat>>() {
            @Override
            public DatabaseResult<Chat> call(Chat chat) {
                return new DatabaseResult<Chat>(chat);
            }
        };
    }

    public void onRestoreInstanceState(Bundle outState) {
        Message message = (Message) outState.getParcelable(UtilBundles.SAVED_LAST_ITEM);
        Chat chat = (Chat) outState.getParcelable(UtilBundles.SAVED_LIST);

        if (null != message){
            chatDisplayer.setLastMessage((Message) outState.getParcelable(UtilBundles.SAVED_LAST_ITEM));
        }

        if (null != chat){
            chatDisplayer.attach(actionListener);
            chatDisplayer.disableInteraction();
            subscriptions.add(
                    Observable.combineLatest(Observable.just(chat)
                            .map(toChats()), loginService.getAuthentication(), asPair())
                            .subscribe(new Action1<Pair>() {
                                @Override
                                public void call(Pair pair) {
                                    if (pair.auth.isSuccess()) {
                                        user = pair.auth.getUser();
                                        displayChat(pair);
                                    } else {
                                        errorLogger.reportError(pair.auth.getFailure(), "Not logged in when opening chat");
                                        navigator.toLogin();
                                    }
                                }
                            })
            );
        } else {
            initPresenter();
        }


    }
}
