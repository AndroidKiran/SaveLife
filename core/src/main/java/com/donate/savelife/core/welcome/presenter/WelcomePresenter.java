package com.donate.savelife.core.welcome.presenter;


import com.donate.savelife.core.analytics.Analytics;
import com.donate.savelife.core.analytics.ErrorLogger;
import com.donate.savelife.core.database.DatabaseResult;
import com.donate.savelife.core.navigation.Navigator;
import com.donate.savelife.core.user.data.model.User;
import com.donate.savelife.core.user.service.UserService;
import com.donate.savelife.core.welcome.displayer.WelcomeDisplayer;

import rx.functions.Action1;
import rx.subscriptions.CompositeSubscription;

public class WelcomePresenter {

    private final UserService userService;
    private final WelcomeDisplayer welcomeDisplayer;
    private final Navigator navigator;
    private final Analytics analytics;
    private final String senderId;
    private final ErrorLogger errorLogger;

    private CompositeSubscription subscriptions = new CompositeSubscription();

    public WelcomePresenter(UserService userService, WelcomeDisplayer welcomeDisplayer, Navigator navigator, Analytics analytics, String senderId, ErrorLogger errorLogger) {
        this.userService = userService;
        this.welcomeDisplayer = welcomeDisplayer;
        this.navigator = navigator;
        this.analytics = analytics;
        this.senderId = senderId;
        this.errorLogger = errorLogger;
    }

    public void startPresenting() {
        welcomeDisplayer.attach(interactionListener);
        analytics.trackInvitationOpened(senderId);
        subscriptions.add(
                userService.observeUser(senderId)
                        .subscribe(new Action1<DatabaseResult<User>>() {
                            @Override
                            public void call(DatabaseResult<User> userDatabaseResult) {
                                if (userDatabaseResult.isSuccess()){
                                    welcomeDisplayer.display(userDatabaseResult.getData());
                                } else {
                                    errorLogger.reportError(userDatabaseResult.getFailure(), "Failed to get user details");
                                }
                            }
                        })

        );
    }

    public void stopPresenting() {
        welcomeDisplayer.detach(interactionListener);
        subscriptions.clear(); //TODO sort out checks
        subscriptions = new CompositeSubscription();
    }

    private final WelcomeDisplayer.InteractionListener interactionListener = new WelcomeDisplayer.InteractionListener() {
        @Override
        public void onGetStartedClicked() {
            analytics.trackInvitationAccepted(senderId);
            navigator.toMain();
        }
    };
}
