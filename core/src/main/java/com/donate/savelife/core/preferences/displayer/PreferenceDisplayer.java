package com.donate.savelife.core.preferences.displayer;

/**
 * Created by ravi on 24/12/16.
 */

public interface PreferenceDisplayer {

    void attach(PreferenceInteractionListener preferenceInteractionListener);

    void detach(PreferenceInteractionListener preferenceInteractionListener);

    interface PreferenceInteractionListener {

        void onNotificationModifyClicked();

        void onAboutClicked();

        void onShareClicked();

    }
}
