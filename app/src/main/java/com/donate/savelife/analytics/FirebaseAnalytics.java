package com.donate.savelife.analytics;

import android.os.Bundle;

import com.donate.savelife.core.analytics.Analytics;

import static com.google.firebase.analytics.FirebaseAnalytics.Param;
import static com.google.firebase.analytics.FirebaseAnalytics.Event;

public class FirebaseAnalytics implements Analytics {

    private static final String CONTENT_TYPE_COUNTRY = "country";
    private static final String CONTENT_TYPE_NEED = "country";
    private static final String PARAM_NEED = "need_id";
    private static final String PARAM_SENDER = "sender";
    private static final String PARAM_USER_ID = "user_id";
    private static final String PARAM_ADDED_OWNER = "added_owner";
    private static final String PARAM_REMOVED_OWNER = "removed_owner";
    private static final String EVENT_SIGN_UP_SUCCESS = "sign_up_success";
    private static final String EVENT_MESSAGE_LENGTH = "message_length";
    private static final String EVENT_INVITE_OPENED = "invite_opened";
    private static final String EVENT_INVITE_ACCEPTED = "invite_accepted";
    private static final String EVENT_MANAGE_OWNERS = "manage_owners";
    private static final String EVENT_ADD_CHANNEL_OWNER = "add_channel_owner";
    private static final String EVENT_REMOVE_CHANNEL_OWNER = "remove_channel_owner";
    private static final String EVENT_SEND_INVITES = "send_invites";
    private static final String EVENT_CREATE_CHANNEL = "create_channel";
    private static final String CONTENT_TYPE_CHANNEL = "channel";



    private final com.google.firebase.analytics.FirebaseAnalytics firebaseAnalytics;

    public FirebaseAnalytics(com.google.firebase.analytics.FirebaseAnalytics firebaseAnalytics) {
        this.firebaseAnalytics = firebaseAnalytics;
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
    public void trackSelectCountry(String country) {
        Bundle bundle = new Bundle();
        bundle.putString(Param.ITEM_ID, country);
        bundle.putString(Param.CONTENT_TYPE, CONTENT_TYPE_COUNTRY);
        firebaseAnalytics.logEvent(Event.SELECT_CONTENT, bundle);
    }

    @Override
    public void trackProfileSelection(String country) {
        Bundle bundle = new Bundle();
        bundle.putString(Param.ITEM_ID, country);
        bundle.putString(Param.CONTENT_TYPE, CONTENT_TYPE_COUNTRY);
        firebaseAnalytics.logEvent(Event.SELECT_CONTENT, bundle);
    }

    @Override
    public void trackSelectNeed(String userId, String needId) {
        Bundle bundle = new Bundle();
        bundle.putString(PARAM_NEED, needId);
        bundle.putString(PARAM_USER_ID, userId);
        bundle.putString(Param.CONTENT_TYPE, CONTENT_TYPE_NEED);
        firebaseAnalytics.logEvent(Event.SELECT_CONTENT, bundle);
    }

    @Override
    public void trackMessageLength(int messageLength, String userId, String needId) {
        Bundle bundle = new Bundle();
        bundle.putInt(Param.VALUE, messageLength);
        bundle.putString(PARAM_NEED, needId);
        bundle.putString(PARAM_USER_ID, userId);
        firebaseAnalytics.logEvent(EVENT_MESSAGE_LENGTH, bundle);
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
    public void trackSendInvitesSelected(String userId) {
        Bundle bundle = new Bundle();
        bundle.putString(PARAM_USER_ID, userId);
        firebaseAnalytics.logEvent(EVENT_SEND_INVITES, bundle);
    }
}
