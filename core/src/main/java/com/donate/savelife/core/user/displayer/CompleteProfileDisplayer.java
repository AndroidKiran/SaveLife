package com.donate.savelife.core.user.displayer;

import com.donate.savelife.core.country.model.City;
import com.donate.savelife.core.user.data.model.User;

/**
 * Created by ravi on 19/11/16.
 */

public interface CompleteProfileDisplayer {

    void attach(OnCompleteListener onCompleteListener);

    void detach(OnCompleteListener onCompleteListener);

    void display(User user);

    void displayCity(City city);

    void showProgress();

    void dismissProgress();

    void showCityDialog();

    void dismissCityDialog();

    interface OnCompleteListener {

        void onComplete(User user);

        void onNavigateClick();
    }
}
