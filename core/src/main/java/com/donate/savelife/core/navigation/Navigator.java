package com.donate.savelife.core.navigation;


import android.app.Activity;
import android.net.Uri;

import com.donate.savelife.core.chats.model.Message;
import com.donate.savelife.core.requirement.model.Need;

public interface Navigator {

    public static final int SEEK_NEED = 011;
    public static final int SEEK_NEED_SUCCESS = 111;
    public static final int SEEK_NEED_FAILED = 112;

    void toHome();

    void toIntro();

    void toParent();

    void toChat(Need need);

    void toCompleteProfile();

    void toNeed();

    void toMain();

    void toShareInvite(String sharingLink);

    void toProfile(Message message);

    void toDialNumber(String dialPhoneNumber);

    void toMap(Uri geoLocation);

    void toRateUs();

    void onSetResults(int resultCode);

    Activity getActivity();
}
