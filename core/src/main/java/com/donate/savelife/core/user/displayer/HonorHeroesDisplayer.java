package com.donate.savelife.core.user.displayer;

import com.donate.savelife.core.user.data.model.User;
import com.donate.savelife.core.user.data.model.Users;

/**
 * Created by ravi on 26/02/17.
 */

public interface HonorHeroesDisplayer {

    void attach(HonorHeroesInteractionListener honorHeroesInteractionListener);

    void detach(HonorHeroesInteractionListener honorHeroesInteractionListener);

    void display(Users users);

    Users getUsers();

    interface HonorHeroesInteractionListener {

        void onHeroesHonored(User user, int value);
    }
}
