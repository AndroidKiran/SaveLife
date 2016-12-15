package com.donate.savelife.core.user.displayer;


import com.donate.savelife.core.user.data.model.Users;

public interface HerosDisplayer {

    void attach(HeroInteractionListener heroInteractionListener);

    void detach(HeroInteractionListener heroInteractionListener);

    void display(Users users);

    void displayLoading();

    void displayContent();

    void displayError();

    void displayEmpty();

    interface HeroInteractionListener {

        void onContentLoaded();

        void onError();

        void onEmpty();
    }
}
