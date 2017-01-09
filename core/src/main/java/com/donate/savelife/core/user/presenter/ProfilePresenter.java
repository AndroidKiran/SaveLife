package com.donate.savelife.core.user.presenter;

import android.os.Bundle;
import android.text.TextUtils;

import com.donate.savelife.core.analytics.Analytics;
import com.donate.savelife.core.analytics.ErrorLogger;
import com.donate.savelife.core.chats.model.Message;
import com.donate.savelife.core.database.DatabaseResult;
import com.donate.savelife.core.navigation.Navigator;
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
            HeroService heroService
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
        this.owner = gsonService.toUser(preferenceService.getLoginUserPreference());
    }

    public void startPresenting() {
        profileDisplayer.attach(onProfileInteractionListener);
        profileDisplayer.toggleMenu(TextUtils.isEmpty(message.getNeedId()));

        subscriptions.add(
                userService.observeUser(message.getUserId())
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
            toEditProfileBundle.putString(Analytics.PARAM_BUTTON_NAME, AppConstant.TO_EDIT_PROFILE_BUTTON);
            analytics.trackButtonClick(toEditProfileBundle);
        }

        @Override
        public void onHonorClick() {
            subscriptions.add(
                    heroService.honorHero(message)
                            .subscribe(new Action1<DatabaseResult<User>>() {
                                @Override
                                public void call(DatabaseResult<User> userDatabaseResult) {
                                    if (userDatabaseResult.isSuccess()) {

                                    } else {

                                    }
                                }
                            })

            );

            Bundle onHonorBunlde = new Bundle();
            onHonorBunlde.putString(Analytics.PARAM_OWNER_ID, owner.getId());
            onHonorBunlde.putString(Analytics.PARAM_HERO_ID, message.getUserId());
            onHonorBunlde.putString(Analytics.PARAM_BUTTON_NAME, AppConstant.HONOR_BUTTON);
            analytics.trackButtonClick(onHonorBunlde);
        }

        @Override
        public void onNavigateClick() {
            navigator.toParent();
        }
    };

    private boolean isAppOwner(){
        return owner.getId().equals(message.getUserId());
    }
}
