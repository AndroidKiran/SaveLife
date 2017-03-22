package com.donate.savelife.core.welcome.presenter;


import com.donate.savelife.core.analytics.Analytics;
import com.donate.savelife.core.analytics.ErrorLogger;
import com.donate.savelife.core.navigation.Navigator;
import com.donate.savelife.core.user.data.model.User;
import com.donate.savelife.core.user.service.UserService;
import com.donate.savelife.core.welcome.displayer.WelcomeDisplayer;

import rx.subscriptions.CompositeSubscription;

public class WelcomePresenter {

    private final UserService userService;
    private final WelcomeDisplayer welcomeDisplayer;
    private final Navigator navigator;
    private final Analytics analytics;
    private final ErrorLogger errorLogger;
    private final String senderName;
    private final String senderPhoto;
    private final String sender;

    private CompositeSubscription subscriptions = new CompositeSubscription();

    public WelcomePresenter(UserService userService, WelcomeDisplayer welcomeDisplayer, Navigator navigator, Analytics analytics, String senderName, String sender, String senderPhoto, ErrorLogger errorLogger) {
        this.userService = userService;
        this.welcomeDisplayer = welcomeDisplayer;
        this.navigator = navigator;
        this.analytics = analytics;
        this.sender = sender;
        this.senderName = senderName;
        this.senderPhoto = senderPhoto;
        this.errorLogger = errorLogger;
    }

    public void startPresenting() {
        welcomeDisplayer.attach(interactionListener);
        User user = new User();
        user.setId(sender);
        user.setName(senderName);
        user.setPhotoUrl(senderPhoto);
        welcomeDisplayer.display(user);
        analytics.trackInvitationOpened(sender);
    }

    public void stopPresenting() {
        welcomeDisplayer.detach(interactionListener);
        subscriptions.clear(); //TODO sort out checks
        subscriptions = new CompositeSubscription();
    }

    private final WelcomeDisplayer.InteractionListener interactionListener = new WelcomeDisplayer.InteractionListener() {
        @Override
        public void onGetStartedClicked() {
            navigator.toMain();
            analytics.trackInvitationAccepted(sender);
        }
    };
}
