package com.donate.savelife.core.chats.presenter;


import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;

import com.donate.savelife.core.analytics.Analytics;
import com.donate.savelife.core.analytics.ErrorLogger;
import com.donate.savelife.core.chats.displayer.ChatDisplayer;
import com.donate.savelife.core.chats.model.Chat;
import com.donate.savelife.core.chats.model.Message;
import com.donate.savelife.core.chats.service.ChatService;
import com.donate.savelife.core.database.DatabaseResult;
import com.donate.savelife.core.login.service.LoginService;
import com.donate.savelife.core.navigation.Navigator;
import com.donate.savelife.core.notifications.database.FCMRemoteMsg;
import com.donate.savelife.core.notifications.service.NotificationRegistrationService;
import com.donate.savelife.core.requirement.model.Need;
import com.donate.savelife.core.requirement.service.NeedService;
import com.donate.savelife.core.user.data.model.User;
import com.donate.savelife.core.utils.AppConstant;
import com.donate.savelife.core.utils.GsonService;
import com.donate.savelife.core.utils.SharedPreferenceService;

import rx.functions.Action1;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;


public class ChatPresenter {

    private final LoginService loginService;
    private final ChatService chatService;
    private final ChatDisplayer chatDisplayer;
    private final Analytics analytics;
    private final Need need;
    private final Navigator navigator;
    private final ErrorLogger errorLogger;
    private final NeedService needService;
    private final GsonService gsonService;
    private final SharedPreferenceService sharedPreferenceService;
    private final NotificationRegistrationService notificationRegistrationService;

    private CompositeSubscription subscriptions = new CompositeSubscription();
    private User appOwner;
    private User postOwner;
    private int responseCount;

    public ChatPresenter(
            LoginService loginService,
            ChatService chatService,
            ChatDisplayer chatDisplayer,
            Need need,
            Analytics analytics,
            Navigator navigator,
            ErrorLogger errorLogger,
            NeedService needService,
            GsonService gsonService,
            SharedPreferenceService sharedPreferenceService,
            NotificationRegistrationService notificationRegistrationService
    ) {
        this.loginService = loginService;
        this.chatService = chatService;
        this.chatDisplayer = chatDisplayer;
        this.analytics = analytics;
        this.need = need;
        this.navigator = navigator;
        this.errorLogger = errorLogger;
        this.needService = needService;
        this.gsonService = gsonService;
        this.sharedPreferenceService = sharedPreferenceService;
        appOwner = gsonService.toUser(sharedPreferenceService.getLoginUserPreference());
        this.notificationRegistrationService = notificationRegistrationService;
    }


    public void startPresenting() {
        chatDisplayer.attach(actionListener);
        chatDisplayer.disableInteraction();
        subscriptions.add(
                chatService.observeUserFor(need)
                        .subscribe(new Action1<DatabaseResult<User>>() {
                            @Override
                            public void call(DatabaseResult<User> userDatabaseResult) {
                                if (userDatabaseResult.isSuccess()) {
                                    postOwner = userDatabaseResult.getData();
                                    chatDisplayer.setTitleLayout(need, postOwner);
                                } else {
                                    errorLogger.reportError(userDatabaseResult.getFailure(), "Unable to fetch user");
                                }
                            }
                        })
        );

        subscriptions.add(
                needService.observerResponseCount(need)
                        .subscribe(new Action1<DatabaseResult<Integer>>() {
                            @Override
                            public void call(DatabaseResult<Integer> integerDatabaseResult) {
                                if (integerDatabaseResult.isSuccess()) {
                                    responseCount = (int) integerDatabaseResult.getData();

                                    if (isMyPost() && responseCount > 0) {
                                        updateTheResponseCount(0);
                                    }

                                } else {
                                    errorLogger.reportError(integerDatabaseResult.getFailure(), "Unable to fetch response count");
                                }
                            }
                        })
        );


        subscriptions.add(
                chatService.observeChats(need)
                        .subscribe(new Action1<DatabaseResult<Chat>>() {
                            @Override
                            public void call(DatabaseResult<Chat> chatDatabaseResult) {
                                if (chatDatabaseResult.isSuccess()) {
                                    chatDisplayer.display(chatDatabaseResult.getData(), appOwner, need);
                                } else {
                                    errorLogger.reportError(chatDatabaseResult.getFailure(), "Failed to fetch chat");
                                    chatDisplayer.displayError();
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
        public void onSubmitMessage(final String message) {
            subscriptions.add(
                    chatService.sendMessage(need, new Message(appOwner.getId(), message))
                            .subscribe(new Action1<DatabaseResult<Message>>() {
                                @Override
                                public void call(DatabaseResult<Message> messageDatabaseResult) {
                                    if (messageDatabaseResult.isSuccess()) {
                                        chatDisplayer.scrollChat();
                                        updateTheResponseCount(responseCount + 1);
                                        pushToNotificationQueue(messageDatabaseResult.getData(), need);
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
                    chatService.observeMoreChats(need, message)
                            .subscribe(new Action1<DatabaseResult<Chat>>() {
                                @Override
                                public void call(DatabaseResult<Chat> chatDatabaseResult) {
                                    if (chatDatabaseResult.isSuccess()) {
                                        chatDisplayer.displayMore(chatDatabaseResult.getData(), appOwner, need);
                                    } else {
                                        errorLogger.reportError(chatDatabaseResult.getFailure(), "Unable to fetch more chats");

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
            if (isMyPost()) {
                message.setNeedId(need.getId());
                navigator.toProfile(message);

                Bundle listItemBundle = new Bundle();
                listItemBundle.putString(Analytics.PARAM_MESSAGE_ID, message.getId());
                listItemBundle.putString(Analytics.PARAM_LIST_NAME, AppConstant.CHAT_LIST);
                analytics.trackListItemClick(listItemBundle);

            }
        }
    };


    private void updateTheResponseCount(int count) {
        needService.updateResponseCount(need, count)
                .subscribe(new Action1<DatabaseResult<Integer>>() {
                    @Override
                    public void call(DatabaseResult<Integer> integerDatabaseResult) {
                        if (!integerDatabaseResult.isSuccess()) {
                            errorLogger.reportError(integerDatabaseResult.getFailure(), "Unable to updated the response counr");
                        }
                    }
                });
    }

    private boolean isMyPost() {
        return appOwner.getId().equals(need.getUserID());
    }

    private void pushToNotificationQueue(Message message, Need need) {
        final Bundle notificationQueueBundle = new Bundle();
        FCMRemoteMsg fcmRemoteMsg = new FCMRemoteMsg();
        fcmRemoteMsg.setCollapse_key(need.getId());
        fcmRemoteMsg.setPriority("high");
        FCMRemoteMsg.Notification notification = new FCMRemoteMsg.Notification();
        notification.setTitle(appOwner.getName());
        notification.setBody(message.getBody());
        fcmRemoteMsg.setNotification(notification);
        notificationQueueBundle.putParcelable(AppConstant.NOFICATION_QUEUE_EXTRA, fcmRemoteMsg);
        notificationQueueBundle.putParcelable(AppConstant.NEED_EXTRA, need);
        subscriptions.add(

                notificationRegistrationService.observeRegistrationsForNeed(need)
                        .subscribeOn(Schedulers.io())
                        .subscribe(new Action1<DatabaseResult<String>>() {
                            @Override
                            public void call(DatabaseResult<String> stringDatabaseResult) {
                                if (stringDatabaseResult.isSuccess()) {
                                    if (!TextUtils.isEmpty(stringDatabaseResult.getData())) {
                                        FCMRemoteMsg fcmRemoteMsg = (FCMRemoteMsg) notificationQueueBundle.getParcelable(AppConstant.NOFICATION_QUEUE_EXTRA);
                                        fcmRemoteMsg.setRegistration_ids(stringDatabaseResult.getData());
                                        notificationQueueBundle.putParcelable(AppConstant.NOFICATION_QUEUE_EXTRA, fcmRemoteMsg);
                                        navigator.startAppCentralService(notificationQueueBundle, AppConstant.ACTION_ADD_NOTIFICATION_TO_QUEUE);
                                    }
                                }
                            }
                        })
        );


    }
}
