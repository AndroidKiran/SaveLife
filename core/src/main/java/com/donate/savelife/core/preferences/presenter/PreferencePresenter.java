package com.donate.savelife.core.preferences.presenter;

import com.donate.savelife.core.analytics.Analytics;
import com.donate.savelife.core.analytics.ErrorLogger;
import com.donate.savelife.core.link.LinkFactory;
import com.donate.savelife.core.navigation.Navigator;
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

    public PreferencePresenter(PreferenceDisplayer preferenceDisplayer,
                               Navigator navigator,
                               ErrorLogger errorLogger,
                               Analytics analytics,
                               GsonService gsonService,
                               SharedPreferenceService preferenceService,
                               LinkFactory linkFactory){
        this.preferenceDisplayer = preferenceDisplayer;
        this.navigator = navigator;
        this.errorLogger = errorLogger;
        this.analytics = analytics;
        this.user = gsonService.toUser(preferenceService.getLoginUserPreference());
        this.linkFactory = linkFactory;
    }

    public void startPresenting(){
        preferenceDisplayer.attach(preferenceInteractionListener);
    }

    public void stopPresenting(){
        preferenceDisplayer.detach(null);
        preferenceDisplayer.dismissAboutUsDialog();
    }


    final PreferenceDisplayer.PreferenceInteractionListener preferenceInteractionListener = new PreferenceDisplayer.PreferenceInteractionListener() {
        @Override
        public void onNotificationModifyClicked() {

        }

        @Override
        public void onAboutClicked() {
            preferenceDisplayer.showAboutUsDialog();
        }

        @Override
        public void onShareClicked() {
            analytics.trackSendInvitesSelected(user.getId());
            navigator.toShareInvite(linkFactory.inviteLinkFrom(user).toString());
        }

        @Override
        public void onRateClicked() {
            navigator.toRateUs();
        }

        @Override
        public void onTermsClicked() {
            preferenceDisplayer.showTermsDialog();
        }
    };
}
