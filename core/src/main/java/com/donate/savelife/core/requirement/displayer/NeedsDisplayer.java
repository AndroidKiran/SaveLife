package com.donate.savelife.core.requirement.displayer;


import com.donate.savelife.core.requirement.model.Need;
import com.donate.savelife.core.user.data.model.User;

public interface NeedsDisplayer {

    void attach(NeedInteractionListener needInteractionListener);

    void detach(NeedInteractionListener needInteractionListener);

    void display(Need need, User owner);

    void displayLoading();

    void displayContent();

    void displayError();

    void displayEmpty();

    interface NeedInteractionListener {

        void onNeedSelected(Need need);

        void onContentLoaded();

        void onError();

        void onEmpty();
    }
}
