package com.donate.savelife.core.requirement.displayer;

import com.donate.savelife.core.country.model.Country;
import com.donate.savelife.core.requirement.model.Need;
import com.donate.savelife.core.user.data.model.User;

/**
 * Created by ravi on 19/11/16.
 */

public interface NeedDisplayer {

    void attach(OnNeedInteractionListener onNeedInteractionListener);

    void detach(OnNeedInteractionListener onNeedInteractionListener);

    void displayCountry(Country country);

    void showProgress();

    void dismissProgress();

    void showCountryDialog();

    void dismissCountryDialog();

    void displayUser(User user);

    void displaySuccessLayout();

    interface OnNeedInteractionListener {

        void onNeedPost(Need need);

        void onNavigateClick();

    }
}
