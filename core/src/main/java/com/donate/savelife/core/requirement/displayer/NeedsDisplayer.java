package com.donate.savelife.core.requirement.displayer;


import com.donate.savelife.core.requirement.model.Need;
import com.donate.savelife.core.requirement.model.Needs;
import com.donate.savelife.core.user.data.model.User;

public interface NeedsDisplayer {

    void attach(NeedInteractionListener needInteractionListener);

    void detach(NeedInteractionListener needInteractionListener);

    void display(Needs needs, User owner);

//    void displayMore(Needs needs, User owner);

    void displayLoading();

    void displayContent();

    void displayError();

    void displayEmpty();

    //retrieve methods
    Needs getNeeds();

//    Need getlastNeedItem();
//
//    void setLastNeedItem(Need lastNeedItem);

    interface NeedInteractionListener {

        void onNeedSelected(Need need);

//        void onLoadMore(Need need);

        void onContentLoaded();

        void onError();

        void onEmpty();
    }
}
