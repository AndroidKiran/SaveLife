package com.donate.savelife.core.welcome.displayer;


import com.donate.savelife.core.user.data.model.User;

public interface WelcomeDisplayer {

    void attach(InteractionListener interactionListener);

    void detach(InteractionListener interactionListener);

    void display(User sender);

    interface InteractionListener {
        void onGetStartedClicked();
    }
}
