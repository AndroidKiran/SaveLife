package com.donate.savelife.core.user.presenter;

import com.donate.savelife.core.analytics.Analytics;
import com.donate.savelife.core.analytics.ErrorLogger;
import com.donate.savelife.core.database.DatabaseResult;
import com.donate.savelife.core.navigation.Navigator;
import com.donate.savelife.core.user.data.model.User;
import com.donate.savelife.core.user.displayer.ProfileDisplayer;
import com.donate.savelife.core.user.service.HeroService;
import com.donate.savelife.core.user.service.UserService;
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
    private final String needID;
    private final String userID;
    private final HeroService heroService;

    private CompositeSubscription subscriptions = new CompositeSubscription();
    private User user;


    public ProfilePresenter(
            ProfileDisplayer profileDisplayer,
            SharedPreferenceService preferenceService,
            Navigator navigator,
            GsonService gsonService,
            UserService userService,
            ErrorLogger errorLogger,
            Analytics analytics,
            String userID,
            String needID,
            HeroService heroService
    ) {
        this.profileDisplayer = profileDisplayer;
        this.preferenceService = preferenceService;
        this.gsonService = gsonService;
        this.userService = userService;
        this.errorLogger = errorLogger;
        this.analytics = analytics;
        this.navigator = navigator;
        this.needID = needID;
        this.userID = userID;
        this.heroService = heroService;
    }

    public void startPresenting() {
        profileDisplayer.attach(onProfileInteractionListener);
        subscriptions.add(
                userService.observeUser(userID)
                .subscribe(new Action1<DatabaseResult<User>>() {
                    @Override
                    public void call(DatabaseResult<User> userDatabaseResult) {
                        if (userDatabaseResult.isSuccess()){
                            profileDisplayer.display(userDatabaseResult.getData());
                        } else {
                            errorLogger.reportError(userDatabaseResult.getFailure(), "Failed to fetch the user");
                            profileDisplayer.displayError();
                        }
                    }
                })
        );

        subscriptions.add(
                heroService.observeHero(needID, userID)
                .subscribe(new Action1<DatabaseResult<User>>() {
                    @Override
                    public void call(DatabaseResult<User> userDatabaseResult) {
                        if (userDatabaseResult.isSuccess()) {
                            profileDisplayer.displayHero(userDatabaseResult.getData(), userID);
                        } else {
                            profileDisplayer.displayHero(new User(), userID);
                            errorLogger.reportError(userDatabaseResult.getFailure(), "Failed to fetch the hero");
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
        }

        @Override
        public void onHonorClick() {
            subscriptions.add(
                    heroService.saveHero(needID, userID)
                            .subscribe(new Action1<DatabaseResult<User>>() {
                                @Override
                                public void call(DatabaseResult<User> userDatabaseResult) {
                                    if (userDatabaseResult.isSuccess()) {

                                    } else {

                                    }
                                }
                            })

            );
        }

        @Override
        public void onNavigateClick() {
            navigator.toParent();
        }
    };
}
