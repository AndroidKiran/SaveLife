package com.donate.savelife.core.analytics;

import android.app.Activity;
import android.os.Bundle;

public interface Analytics {

    String PARAM_INTRO_SCREEN = "intro_screen";
    String PARAM_OWNER_ID = "owner_id";
    String PARAM_HERO_ID = "hero_id";
    String PARAM_NEED_ID = "need_id";
    String PARAM_MESSAGE_LENGTH = "message_length";
    String PARAM_BUTTON_NAME = "button_name";
    String PARAM_MOBILE_NUM = "mobile_num";
    String PARAM_ADDRESS = "address";
    String PARAM_LIST_NAME = "list_name";
    String PARAM_MESSAGE_ID = "message_id";

    String PARAM_EVENT_NAME = "event_name";
    String PARAM_CITY = "city";

    //    Events list
    String PARAM_WRITE_MESSAGE = "write_message_event";
    String PARAM_BRIEF_NEED = "brief_need_event";
    String PARAM_CALL_SEEKER = "call_seeker_event";
    String PARAM_OPEN_SEEKER_LOCATION = "open_seeker_location_event";
    String PARAM_VIEW_PROFILE_FROM_CHAT ="view_profile_from_chat_event" ;
    String PARAM_OPEN_BLOOD_REQUEST = "open_blood_request_event";
    String PARAM_OPEN_PROFILE = "open_profile_event";
    String PARAM_OPEN_ABOUT_US = "open_about_us_event";
    String PARAM_INVITE = "invite_event";
    String PARAM_RATE_ON_PLAY_STORE = "rate_on_play_store_event";
    String PARAM_TERMS = "open_terms_event";
    String PARAM_OPEN_CHAT = "open_chat_event";
    String PARAM_COMPLETE_PROFILE = "complete_profile_event";
    String PARAM_OPEN_COMPLETE_PROFILE = "open_complete_profile_event";
    String PARAM_HONOR_HERO = "honor_hero_event";
    String PARAM_SKIP_INTRO = "skip_intro_event";
    String PARAM_OPEN_MY_NEED = "open_my_need_event";
    String PARAM_NOTIFICATION_ENABLED = "notification_enabled";
    String PARAM_TOGGLE_NOTIFICATION = "toggle_notification";
    String PARAM_LOG_OUT = "log_out";


    void trackSignInStarted(String method);

    void trackSignInSuccessful(String method);

    void trackInvitationOpened(String senderId);

    void trackInvitationAccepted(String senderId);

    void trackEventOnClick(Bundle bundle);

    void trackScreen(Activity activity, String screenName, String dummy);


    void setUserLocationProperty(String city);

    void setUserIdProperty(String uid);

}
