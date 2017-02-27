package com.donate.savelife.core.navigation;


import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;

import com.donate.savelife.core.chats.model.Message;
import com.donate.savelife.core.user.data.model.Users;

public interface Navigator {

    public static final int SEEK_NEED = 011;
    public static final int SEEK_NEED_SUCCESS = 111;
    public static final int SEEK_NEED_FAILED = 112;
    public static final int PLACE_PICKER_REQUEST = 3;


    void toHome();

    void toIntro();

    void toParent();

    void toChat(String needId);

    void toCompleteProfile();

    void toNeed();

    void toMain();

    void toShareInvite(String sharingLink);

    void toProfile(Message message);

    void toDialNumber(String dialPhoneNumber);

    void toMap(Uri geoLocation);

    void toMarketPlace();

    void toMyNeeds(boolean finish);

    void toWelcome();

    void toMapPicker();

    void toHonor(Users users, String needId);

    void startAppCentralService(Bundle bundle, String action);

    Activity getActivity();

}
