package com.donate.savelife.core.user.presenter;

import android.os.Bundle;
import android.text.TextUtils;

import com.donate.savelife.core.analytics.Analytics;
import com.donate.savelife.core.analytics.ErrorLogger;
import com.donate.savelife.core.chats.model.Message;
import com.donate.savelife.core.database.DatabaseResult;
import com.donate.savelife.core.navigation.Navigator;
import com.donate.savelife.core.notifications.database.FCMRemoteMsg;
import com.donate.savelife.core.notifications.service.NotificationRegistrationService;
import com.donate.savelife.core.user.data.model.User;
import com.donate.savelife.core.user.displayer.ProfileDisplayer;
import com.donate.savelife.core.user.service.HeroService;
import com.donate.savelife.core.user.service.UserService;
import com.donate.savelife.core.utils.AppConstant;
import com.donate.savelife.core.utils.GsonService;
import com.donate.savelife.core.utils.SharedPreferenceService;

import rx.functions.Action1;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by ravi on 19/11/16.
 */

public class ProfilePresenter {

    private final ProfileDisplayer profileDisplayer;
    private final SharedPreferenceService preferenceService;
    private final GsonService gsonService;
    private final UserService userService;
    private final ErrorLogger errorLogger;
    private final Analytics analytics;
    private final Navigator navigator;
    private final HeroService heroService;
    private final User owner;
    private final Message message;
    private final NotificationRegistrationService notificationRegistrationService;

    private CompositeSubscription subscriptions = new CompositeSubscription();

    public ProfilePresenter(
            ProfileDisplayer profileDisplayer,
            SharedPreferenceService preferenceService,
            Navigator navigator,
            GsonService gsonService,
            UserService userService,
            ErrorLogger errorLogger,
            Analytics analytics,
            Message message,
            HeroService heroService,
            NotificationRegistrationService notificationRegistrationService
    ) {
        this.profileDisplayer = profileDisplayer;
        this.preferenceService = preferenceService;
        this.gsonService = gsonService;
        this.userService = userService;
        this.errorLogger = errorLogger;
        this.analytics = analytics;
        this.navigator = navigator;
        this.message = message;
        this.heroService = heroService;
        this.notificationRegistrationService = notificationRegistrationService;
        this.owner = gsonService.toUser(preferenceService.getLoginUserPreference());
    }

    public void startPresenting() {
        profileDisplayer.attach(onProfileInteractionListener);
        profileDisplayer.toggleMenu(TextUtils.isEmpty(message.getNeedId()));

        subscriptions.add(
                userService.observeUser(message.getUserID())
                .subscribe(new Action1<DatabaseResult<User>>() {
                    @Override
                    public void call(DatabaseResult<User> userDatabaseResult) {
                        if (userDatabaseResult.isSuccess()){
                            User user = userDatabaseResult.getData();
                            profileDisplayer.display(user);
                        } else {
                            profileDisplayer.displayError();
                            errorLogger.reportError(userDatabaseResult.getFailure(), "Failed to fetch the user");
                        }
                    }
                })
        );

        subscriptions.add(
                heroService.observeHero(message)
                .subscribe(new Action1<DatabaseResult<Boolean>>() {
                    @Override
                    public void call(DatabaseResult<Boolean> booleanDatabaseResult) {
                        if (booleanDatabaseResult.isSuccess()){
                            profileDisplayer.displayHero(booleanDatabaseResult.getData(), isAppOwner());
                        } else {
                            errorLogger.reportError(booleanDatabaseResult.getFailure(), "Failed to get honor value");
                        }
                    }
                })
        );
    }

    public void stopPresenting() {
        profileDisplayer.detach(null);
        subscriptions.clear();
        subscriptions = new CompositeSubscription();

    }


    ProfileDisplayer.OnProfileInteractionListener onProfileInteractionListener = new ProfileDisplayer.OnProfileInteractionListener() {
        @Override
        public void onEditClick() {
            navigator.toCompleteProfile();
            Bundle toEditProfileBundle = new Bundle();
            toEditProfileBundle.putString(Analytics.PARAM_OWNER_ID, owner.getId());
            toEditProfileBundle.putString(Analytics.PARAM_EVENT_NAME, Analytics.PARAM_OPEN_COMPLETE_PROFILE);
            analytics.trackEventOnClick(toEditProfileBundle);
        }

        @Override
        public void onHonorClick(int value) {
            subscriptions.add(
                    heroService.honorHero(message, value)
                            .subscribe(new Action1<DatabaseResult<User>>() {
                                @Override
                                public void call(DatabaseResult<User> userDatabaseResult) {
                                    if (userDatabaseResult.isSuccess()) {
                                        User hero = userDatabaseResult.getData();
                                        subscriptions.add(
                                                notificationRegistrationService.observeRegIdFor(hero)
                                                .subscribe(new Action1<DatabaseResult<String>>() {
                                                    @Override
                                                    public void call(DatabaseResult<String> stringDatabaseResult) {
                                                        if (stringDatabaseResult.isSuccess()){
                                                            pushToNotificationQueue(stringDatabaseResult.getData());
                                                        } else {
                                                            errorLogger.reportError(stringDatabaseResult.getFailure(), "Failed to get the registration id");
                                                        }
                                                    }
                                                })
                                        );
                                    } else {
                                        errorLogger.reportError(userDatabaseResult.getFailure(), "Failed to honor hero");
                                    }
                                }
                            })

            );

            Bundle onHonorBunlde = new Bundle();
            onHonorBunlde.putString(Analytics.PARAM_OWNER_ID, owner.getId());
            onHonorBunlde.putString(Analytics.PARAM_HERO_ID, message.getUserID());
            onHonorBunlde.putString(Analytics.PARAM_EVENT_NAME, Analytics.PARAM_HONOR_HERO);
            analytics.trackEventOnClick(onHonorBunlde);
        }

        @Override
        public void onNavigateClick() {
            navigator.toParent();
        }
    };

    private boolean isAppOwner(){
        return owner.getId().equals(message.getUserID());
    }

    private void pushToNotificationQueue(String regId) {
        Bundle notificationQueueBundle = new Bundle();

        FCMRemoteMsg fcmRemoteMsg = new FCMRemoteMsg();
        fcmRemoteMsg.setTo(regId);
        fcmRemoteMsg.setPriority("high");
        fcmRemoteMsg.setContent_available(true);

        FCMRemoteMsg.Notification notification = new FCMRemoteMsg.Notification();
        notification.setBody(owner.getName() + " has Honored you as HERO");

        FCMRemoteMsg.Data data = new FCMRemoteMsg.Data();
        data.setClick_action(AppConstant.CLICK_ACTION_PROFILE);

        fcmRemoteMsg.setNotification(notification);
        fcmRemoteMsg.setData(data);

        notificationQueueBundle.putParcelable(AppConstant.NOFICATION_QUEUE_EXTRA, fcmRemoteMsg);
        navigator.startAppCentralService(notificationQueueBundle, AppConstant.ACTION_ADD_NOTIFICATION_TO_QUEUE);
    }
}
