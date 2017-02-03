package com.donate.savelife.core.home.displayer;

import com.donate.savelife.core.notifications.database.FCMRemoteMsg;
import com.donate.savelife.core.user.data.model.User;

/**
 * Created by ravi on 09/09/16.
 */
public interface HomeDisplayer {

    void setProfile(User user);

    void setUpViewPager();

    void attach(HomeInteractionListener homeInteractionListener);

    void detach(HomeInteractionListener homeInteractionListener);

    void onTabSelected(int position);

    void toggleMyNeedVisibility(boolean toggle);

    interface HomeInteractionListener {
        void onFabBtnClicked();

        void onProfileClicked();

        void onTabSelected(int postion);

        void onMyNeedClicked();

        void onNotificationClicked(FCMRemoteMsg fcmRemoteMsg);
    }
}
