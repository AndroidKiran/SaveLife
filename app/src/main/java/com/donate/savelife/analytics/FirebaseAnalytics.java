package com.donate.savelife.analytics;

import android.app.Activity;
import android.os.Bundle;

import com.donate.savelife.core.analytics.Analytics;

import static com.google.firebase.analytics.FirebaseAnalytics.Param;

public class FirebaseAnalytics implements Analytics {

    public static final String PARAM_SENDER = "sender";
    public static final String EVENT_SIGN_UP_SUCCESS = "sign_up_success";
    public static final String EVENT_INVITE_OPENED = "invite_opened";
    public static final String EVENT_INVITE_ACCEPTED = "invite_accepted";


    private final com.google.firebase.analytics.FirebaseAnalytics firebaseAnalytics;

    public FirebaseAnalytics(com.google.firebase.analytics.FirebaseAnalytics firebaseAnalytics) {
        this.firebaseAnalytics = firebaseAnalytics;
        firebaseAnalytics.setAnalyticsCollectionEnabled(true);
    }

    @Override
    public void trackSignInStarted(String method) {
        Bundle bundle = new Bundle();
        bundle.putString(Param.SIGN_UP_METHOD, method);
        firebaseAnalytics.logEvent(com.google.firebase.analytics.FirebaseAnalytics.Event.SIGN_UP, bundle);
    }

    @Override
    public void trackSignInSuccessful(String method) {
        Bundle bundle = new Bundle();
        bundle.putString(Param.SIGN_UP_METHOD, method);
        firebaseAnalytics.logEvent(EVENT_SIGN_UP_SUCCESS, bundle);
    }

    @Override
    public void trackInvitationOpened(String senderId) {
        Bundle bundle = new Bundle();
        bundle.putString(PARAM_SENDER, senderId);
        firebaseAnalytics.logEvent(EVENT_INVITE_OPENED, bundle);
    }

    @Override
    public void trackInvitationAccepted(String senderId) {
        Bundle bundle = new Bundle();
        bundle.putString(PARAM_SENDER, senderId);
        firebaseAnalytics.logEvent(EVENT_INVITE_ACCEPTED, bundle);
    }

    @Override
    public void trackEventOnClick(Bundle bundle) {
        String event = bundle.getString(PARAM_EVENT_NAME);
        bundle.remove(PARAM_EVENT_NAME);
        firebaseAnalytics.logEvent(event, bundle);
    }

    @Override
    public void trackScreen(Activity activity, String screenName, String dummy) {
        firebaseAnalytics.setCurrentScreen(activity, screenName, null);
    }

    @Override
    public void setUserLocationProperty(String city) {
        firebaseAnalytics.setUserProperty(Analytics.PARAM_CITY, city);
    }

    @Override
    public void setUserIdProperty(String uid) {
        firebaseAnalytics.setUserId(uid);
    }


}
