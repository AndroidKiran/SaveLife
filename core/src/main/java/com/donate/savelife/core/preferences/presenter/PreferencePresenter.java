package com.donate.savelife.core.preferences.presenter;

import android.os.Bundle;

import com.donate.savelife.core.analytics.Analytics;
import com.donate.savelife.core.analytics.ErrorLogger;
import com.donate.savelife.core.link.LinkFactory;
import com.donate.savelife.core.login.service.LoginService;
import com.donate.savelife.core.navigation.Navigator;
import com.donate.savelife.core.notifications.service.AppNotificationService;
import com.donate.savelife.core.preferences.displayer.PreferenceDisplayer;
import com.donate.savelife.core.user.data.model.User;
import com.donate.savelife.core.utils.GsonService;
import com.donate.savelife.core.utils.SharedPreferenceService;

/**
 * Created by ravi on 24/12/16.
 */

public class PreferencePresenter {

    private final PreferenceDisplayer preferenceDisplayer;
    private final ErrorLogger errorLogger;
    private final Analytics analytics;
    private final Navigator navigator;
    private final User user;
    private final LinkFactory linkFactory;
    private final GsonService gsonService;
    private final SharedPreferenceService preferenceService;
    private final AppNotificationService appNotificationService;
    private final LoginService loginService;

    public PreferencePresenter(PreferenceDisplayer preferenceDisplayer, Navigator navigator,
                               ErrorLogger errorLogger, Analytics analytics,
                               GsonService gsonService, SharedPreferenceService preferenceService,
                               LinkFactory linkFactory, AppNotificationService appNotificationService,
                               LoginService loginService){
        this.preferenceDisplayer = preferenceDisplayer;
        this.navigator = navigator;
        this.errorLogger = errorLogger;
        this.analytics = analytics;
        this.gsonService = gsonService;
        this.preferenceService = preferenceService;
        this.user = gsonService.toUser(preferenceService.getLoginUserPreference());
        this.linkFactory = linkFactory;
        this.appNotificationService = appNotificationService;
        this.loginService = loginService;
    }

    public void startPresenting(){
        preferenceDisplayer.attach(preferenceInteractionListener);
        preferenceDisplayer.showNotificationCity(user.getCity());
        preferenceDisplayer.showNotificationStatus(preferenceService.isNotificationEnabled());
        analytics.setUserLocationProperty(user.getCity());
    }

    public void stopPresenting(){
        preferenceDisplayer.detach(null);
        preferenceDisplayer.dismissAboutUsDialog();
    }


    final PreferenceDisplayer.PreferenceInteractionListener preferenceInteractionListener = new PreferenceDisplayer.PreferenceInteractionListener() {
        @Override
        public void onNotificationModifyClicked(boolean toggle) {
            appNotificationService.toggleNotificationStatus(toggle);

            Bundle notificationSettingBundle = new Bundle();
            notificationSettingBundle.putString(Analytics.PARAM_OWNER_ID, user.getId());
            notificationSettingBundle.putBoolean(Analytics.PARAM_NOTIFICATION_ENABLED, toggle);
            notificationSettingBundle.putString(Analytics.PARAM_EVENT_NAME, Analytics.PARAM_TOGGLE_NOTIFICATION);
            analytics.trackEventOnClick(notificationSettingBundle);
        }

        @Override
        public void onAboutClicked() {
            preferenceDisplayer.showAboutUsDialog();

            Bundle aboutUsBundle = new Bundle();
            aboutUsBundle.putString(Analytics.PARAM_OWNER_ID, user.getId());
            aboutUsBundle.putString(Analytics.PARAM_EVENT_NAME, Analytics.PARAM_OPEN_ABOUT_US);
            analytics.trackEventOnClick(aboutUsBundle);
        }

        @Override
        public void onShareClicked() {
            navigator.toShareInvite(linkFactory.inviteLinkFrom(user).toString());

            Bundle inviteBundle = new Bundle();
            inviteBundle.putString(Analytics.PARAM_OWNER_ID, user.getId());
            inviteBundle.putString(Analytics.PARAM_EVENT_NAME, Analytics.PARAM_INVITE);
            analytics.trackEventOnClick(inviteBundle);
        }

        @Override
        public void onRateClicked() {
            navigator.toMarketPlace();

            Bundle rateBundle = new Bundle();
            rateBundle.putString(Analytics.PARAM_OWNER_ID, user.getId());
            rateBundle.putString(Analytics.PARAM_EVENT_NAME, Analytics.PARAM_RATE_ON_PLAY_STORE);
            analytics.trackEventOnClick(rateBundle);
        }

        @Override
        public void onTermsClicked() {
            preferenceDisplayer.showTermsDialog();

            Bundle termsBundle = new Bundle();
            termsBundle.putString(Analytics.PARAM_OWNER_ID, user.getId());
            termsBundle.putString(Analytics.PARAM_EVENT_NAME, Analytics.PARAM_TERMS);
            analytics.trackEventOnClick(termsBundle);
        }

        @Override
        public void onLogoutPressed() {
            signOut();

            Bundle logOut = new Bundle();
            logOut.putString(Analytics.PARAM_OWNER_ID, user.getId());
            logOut.putString(Analytics.PARAM_EVENT_NAME, Analytics.PARAM_LOG_OUT);
            analytics.trackEventOnClick(logOut);
        }
    };


    private void signOut(){
        loginService.signOut();
        preferenceService.clear();
        navigator.toMain();
    }
}
