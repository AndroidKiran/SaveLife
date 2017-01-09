package com.donate.savelife.core.preferences.displayer;

/**
 * Created by ravi on 24/12/16.
 */

public interface PreferenceDisplayer {

    void attach(PreferenceInteractionListener preferenceInteractionListener);

    void detach(PreferenceInteractionListener preferenceInteractionListener);

    void showAboutUsDialog();

    void dismissAboutUsDialog();

    void showTermsDialog();

    void showNotificationCity(String city);

    interface PreferenceInteractionListener {

        void onNotificationModifyClicked();

        void onAboutClicked();

        void onShareClicked();

        void onRateClicked();

        void onTermsClicked();

    }
}
