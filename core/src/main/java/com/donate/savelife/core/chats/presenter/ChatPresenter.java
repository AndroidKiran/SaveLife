
package com.donate.savelife.core.chats.presenter;


import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;

import com.donate.savelife.core.analytics.Analytics;
import com.donate.savelife.core.analytics.ErrorLogger;
import com.donate.savelife.core.chats.displayer.ChatDisplayer;
import com.donate.savelife.core.chats.model.Chat;
import com.donate.savelife.core.chats.model.Map;
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
import com.donate.savelife.core.utils.CoreUtils;
import com.donate.savelife.core.utils.GsonService;
import com.donate.savelife.core.utils.SharedPreferenceService;

import java.util.ArrayList;

import rx.functions.Action1;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;


public class ChatPresenter {

    private final LoginService loginService;
    private final ChatService chatService;
    private final ChatDisplayer chatDisplayer;
    private final Analytics analytics;
    private Need need;
    private final Navigator navigator;
    private final ErrorLogger errorLogger;
    private final NeedService needService;
    private final GsonService gsonService;
    private final SharedPreferenceService sharedPreferenceService;
    private final NotificationRegistrationService notificationRegistrationService;
    private final String needId;

    private CompositeSubscription subscriptions = new CompositeSubscription();
    private User appOwner;
    private User postOwner;
    private int responseCount;

    public ChatPresenter(
            LoginService loginService,
            ChatService chatService,
            ChatDisplayer chatDisplayer,
            String needId,
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
        this.needId = needId;
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
                needService.observeNeed(needId)
                        .subscribe(new Action1<DatabaseResult<Need>>() {
                            @Override
                            public void call(DatabaseResult<Need> needDatabaseResult) {
                                if (needDatabaseResult.isSuccess()) {
                                    need = needDatabaseResult.getData();

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
                                } else {
                                    errorLogger.reportError(needDatabaseResult.getFailure(), "Failed the get need by id");
                                }
                            }
                        })
        );

    }


    public void stopPresenting() {
        if (isMyPost()) {
            updateTheResponseCount(0);
        }
        chatDisplayer.detach(null);
        subscriptions.clear();
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
            Message message1 = new Message(appOwner.getId(), message);
            pushMessageToDb(message1);
            pushAnalyticsForOnMessageSendClicked(message);
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
            pushAnalyticsForOnToolbarClicked();
        }

        @Override
        public void onCallClick(String mobileNum) {
            navigator.toDialNumber(mobileNum);
            pushAnalyticsForOnCallClicked(mobileNum);
        }

        @Override
        public void onAddressClick(String address) {
            Uri gmmIntentUri = Uri.parse("geo:0,0?q=" + Uri.encode(address));
            navigator.toMap(gmmIntentUri);
            pushAnalyticsForOnLocationClicked(address);
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
            Map map = message.getMap();
            if (map != null){
                Uri gmmIntentUri = Uri.parse("geo:"+map.getLatitude()+","+map.getLongitude()+"?q=");
                navigator.toMap(gmmIntentUri);
//                pushAnalyticsForOnLocationClicked(address);
            }

        }

        @Override
        public void onProfileClicked(Message message) {
            if (isMyPost()) {
                message.setNeedId(need.getId());
                navigator.toProfile(message);
                pushAnalyticsForOnChatItemClicked(message);
            }
        }

        @Override
        public void onMapAttachClicked() {
            navigator.toMapPicker();
        }
    };


    private void updateTheResponseCount(int count) {
        count = isMyPost() ? 0 : count;
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
        fcmRemoteMsg.setContent_available(true);

        FCMRemoteMsg.Notification notification = new FCMRemoteMsg.Notification();
        String body = message.getBody();
        if (!TextUtils.isEmpty(body)){
            notification.setBody(appOwner.getName() + " says " + body);
        } else {
            notification.setBody(appOwner.getName() + " sent location");
        }

        fcmRemoteMsg.setNotification(notification);

        FCMRemoteMsg.Data data = new FCMRemoteMsg.Data();
        data.setNeed_id(need.getId());
        data.setClick_action(AppConstant.CLICK_ACTION_CHAT);
        fcmRemoteMsg.setData(data);

        notificationQueueBundle.putParcelable(AppConstant.NOFICATION_QUEUE_EXTRA, fcmRemoteMsg);
        notificationQueueBundle.putParcelable(AppConstant.NEED_EXTRA, need);

        subscriptions.add(

                notificationRegistrationService.observeRegistrationsForNeed(need, appOwner.getId())
                        .subscribeOn(Schedulers.io())
                        .subscribe(new Action1<DatabaseResult<ArrayList<String>>>() {
                                       @Override
                                       public void call(DatabaseResult<ArrayList<String>> arrayListDatabaseResult) {
                                           if (arrayListDatabaseResult.isSuccess()) {
                                               if (arrayListDatabaseResult.getData().size() > 0) {
                                                   FCMRemoteMsg fcmRemoteMsg = (FCMRemoteMsg) notificationQueueBundle.getParcelable(AppConstant.NOFICATION_QUEUE_EXTRA);
                                                   ArrayList<String> list = arrayListDatabaseResult.getData();
                                                   fcmRemoteMsg.setRegistration_ids(list);
                                                   notificationQueueBundle.putParcelable(AppConstant.NOFICATION_QUEUE_EXTRA, fcmRemoteMsg);
                                                   navigator.startAppCentralService(notificationQueueBundle, AppConstant.ACTION_ADD_NOTIFICATION_TO_QUEUE);
                                               }
                                           }
                                       }
                                   }
                        )

        );


    }


    private void pushAnalyticsForOnMessageSendClicked(String message) {
        Bundle messageSentBundle = new Bundle();
        messageSentBundle.putString(Analytics.PARAM_OWNER_ID, appOwner.getId());
        messageSentBundle.putInt(Analytics.PARAM_MESSAGE_LENGTH, message.length());
        messageSentBundle.putString(Analytics.PARAM_EVENT_NAME, Analytics.PARAM_WRITE_MESSAGE);
        analytics.trackEventOnClick(messageSentBundle);
    }

    private void pushAnalyticsForOnToolbarClicked() {
        Bundle toolbarBarBundle = new Bundle();
        toolbarBarBundle.putString(Analytics.PARAM_OWNER_ID, appOwner.getId());
        toolbarBarBundle.putString(Analytics.PARAM_EVENT_NAME, Analytics.PARAM_BRIEF_NEED);
        analytics.trackEventOnClick(toolbarBarBundle);
    }

    private void pushAnalyticsForOnCallClicked(String mobileNum){
        Bundle callBundle = new Bundle();
        callBundle.putString(Analytics.PARAM_OWNER_ID, appOwner.getId());
        callBundle.putString(Analytics.PARAM_MOBILE_NUM, mobileNum);
        callBundle.putString(Analytics.PARAM_EVENT_NAME, Analytics.PARAM_CALL_SEEKER);
        analytics.trackEventOnClick(callBundle);
    }

    private void pushAnalyticsForOnLocationClicked(String address){
        Bundle addressBundle = new Bundle();
        addressBundle.putString(Analytics.PARAM_OWNER_ID, appOwner.getId());
        addressBundle.putString(Analytics.PARAM_ADDRESS, address);
        addressBundle.putString(Analytics.PARAM_EVENT_NAME, Analytics.PARAM_OPEN_SEEKER_LOCATION);
        analytics.trackEventOnClick(addressBundle);
    }

    private void pushAnalyticsForOnChatItemClicked(Message message){
        Bundle listItemBundle = new Bundle();
        listItemBundle.putString(Analytics.PARAM_MESSAGE_ID, message.getId());
        listItemBundle.putString(Analytics.PARAM_EVENT_NAME, Analytics.PARAM_VIEW_PROFILE_FROM_CHAT);
        analytics.trackEventOnClick(listItemBundle);
    }

    private void pushMessageToDb(Message message) {
        subscriptions.add(
                chatService.sendMessage(need, message)
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

    }

    public void OnActivityResult(Map map) {
        Message message = new Message(appOwner.getId(), map, CoreUtils.ContentType.MAP);
        pushMessageToDb(message);
    }
}
