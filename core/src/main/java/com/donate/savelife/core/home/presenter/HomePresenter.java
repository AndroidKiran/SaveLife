package com.donate.savelife.core.home.presenter;


import com.donate.savelife.core.analytics.Analytics;
import com.donate.savelife.core.analytics.ErrorLogger;
import com.donate.savelife.core.home.displayer.HomeDisplayer;
import com.donate.savelife.core.navigation.Navigator;
import com.donate.savelife.core.user.data.model.User;
import com.donate.savelife.core.utils.GsonService;
import com.donate.savelife.core.utils.SharedPreferenceService;

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
        }

        @Override
        public void onProfileClicked() {
            navigator.toProfile("", user.getId());
        }
    };
}
