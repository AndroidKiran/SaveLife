package com.donate.savelife.core.home.presenter;


import android.os.Bundle;

import com.donate.savelife.core.analytics.Analytics;
import com.donate.savelife.core.analytics.ErrorLogger;
import com.donate.savelife.core.chats.model.Message;
import com.donate.savelife.core.home.displayer.HomeDisplayer;
import com.donate.savelife.core.navigation.Navigator;
import com.donate.savelife.core.user.data.model.User;
import com.donate.savelife.core.utils.GsonService;
import com.donate.savelife.core.utils.SharedPreferenceService;
import com.donate.savelife.core.utils.AppConstant;

/**
 * Created by ravi on 09/09/16.
 */
public class HomePresenter {

    private final HomeDisplayer homeDisplayer;
    private final Navigator navigator;
    private final Analytics analytics;
    private final ErrorLogger errorLogger;
    private final SharedPreferenceService preferenceService;
    private final GsonService gsonService;
    private final User user;

    public HomePresenter(HomeDisplayer homeDisplayer,
                         SharedPreferenceService preferenceService,
                         GsonService gsonService,
                         Navigator navigator,
                         Analytics analytics,
                         ErrorLogger errorLogger){
        this.homeDisplayer = homeDisplayer;
        this.preferenceService = preferenceService;
        this.gsonService = gsonService;
        this.navigator = navigator;
        this.analytics = analytics;
        this.errorLogger = errorLogger;
        this.user = gsonService.toUser(preferenceService.getLoginUserPreference());
        homeDisplayer.setUpViewPager();
        homeDisplayer.setProfile(user);
        analytics.setUserIdProperty(user.getId());
    }

    public void startPresenting(){
        homeDisplayer.attach(homeInteractionListener);
    }

    public void stopPresenting(){
        homeDisplayer.detach(homeInteractionListener);
    }

    private final HomeDisplayer.HomeInteractionListener homeInteractionListener = new HomeDisplayer.HomeInteractionListener() {

        @Override
        public void onFabBtnClicked() {
            navigator.toNeed();
            Bundle toPostNeedBundle = new Bundle();
            toPostNeedBundle.putString(Analytics.PARAM_OWNER_ID, user.getId());
            toPostNeedBundle.putString(Analytics.PARAM_BUTTON_NAME, AppConstant.TO_POST_NEED_BUTTON);
            analytics.trackButtonClick(toPostNeedBundle);
        }

        @Override
        public void onProfileClicked() {
            Message message = new Message();
            message.setNeedId("");
            message.setUserId(user.getId());
            navigator.toProfile(message);
            Bundle toProfileBundle = new Bundle();
            toProfileBundle.putString(Analytics.PARAM_OWNER_ID, user.getId());
            toProfileBundle.putString(Analytics.PARAM_BUTTON_NAME, AppConstant.TO_PROFILE_BUTTON);
            analytics.trackButtonClick(toProfileBundle);
        }

        @Override
        public void onTabSelected(int postion) {
            homeDisplayer.onTabSelected(postion);
            switch (postion){
                case 0:
                    analytics.trackScreen(navigator.getActivity(), AppConstant.NEEDS_SCREEN, null);
                    break;

                case 1:
                    analytics.trackScreen(navigator.getActivity(), AppConstant.HEROS_SCREEN, null);
                    break;

                case 2:
                    analytics.trackScreen(navigator.getActivity(), AppConstant.PREFERENCES_SCREEN, null);
                    break;
            }
        }
    };
}
