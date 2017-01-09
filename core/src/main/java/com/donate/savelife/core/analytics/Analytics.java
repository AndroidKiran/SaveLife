package com.donate.savelife.core.analytics;

import android.app.Activity;
import android.os.Bundle;

public interface Analytics {

    public static final String PARAM_INTRO_SCREEN = "intro_screen";
    public static final String PARAM_OWNER_ID = "owner_id";
    public static final String PARAM_HERO_ID = "hero_id";
    public static final String PARAM_NEED_ID = "need_id";
    public static final String PARAM_MESSAGE_LENGTH = "message_length";
    public static final String PARAM_BUTTON_NAME = "button_name";
    public static final String PARAM_MOBILE_NUM = "mobile_num";
    public static final String PARAM_ADDRESS = "address";
    public static final String PARAM_LIST_NAME = "list_name";
    public static final String PARAM_MESSAGE_ID = "message_id";

    public static final String EVENT_BUTTON_CLICK = "button_click";
    public static final String EVENT_ON_LIST_ITEM_CLICK = "list_item_click";
    public static final String PARAM_CITY = "city";


    void trackSignInStarted(String method);

    void trackSignInSuccessful(String method);

    void trackInvitationOpened(String senderId);

    void trackInvitationAccepted(String senderId);

    void trackButtonClick(Bundle bundle);

    void trackScreen(Activity activity, String screenName, String dummy);

    void trackListItemClick(Bundle bundle);

    void setUserLocationProperty(String city);

    void setUserIdProperty(String uid);

}
