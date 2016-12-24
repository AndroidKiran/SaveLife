package com.donate.savelife.core.navigation;


import android.net.Uri;

import com.donate.savelife.core.requirement.model.Need;

public interface Navigator {

    void toHome();

    void toLogin();

    void toParent();

    void toChat(Need need);

    void toCompleteProfile();

    void toNeed();

    void toMain();

    void toShareInvite(String sharingLink);

    void toProfile(String needID, String userID);

    void toDialNumber(String dialPhoneNumber);

    void toMap(Uri geoLocation);

    void toRateUs();
}
