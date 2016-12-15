package com.donate.savelife.core.user.displayer;

import com.donate.savelife.core.user.data.model.User;

/**
 * Created by ravi on 19/11/16.
 */

public interface ProfileDisplayer {

    void attach(OnProfileInteractionListener profileInteractionListener);

    void detach(OnProfileInteractionListener profileInteractionListener);

    void display(User user);

    void displayHero(User user, String uid);

    interface OnProfileInteractionListener {

        void onEditClick();

        void onHonorClick();

        void onNavigateClick();
    }
}
