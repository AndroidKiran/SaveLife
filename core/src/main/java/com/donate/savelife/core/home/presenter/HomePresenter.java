package com.donate.savelife.core.home.presenter;


import android.os.Bundle;

import com.donate.savelife.core.analytics.Analytics;
import com.donate.savelife.core.analytics.ErrorLogger;
import com.donate.savelife.core.chats.model.Message;
import com.donate.savelife.core.database.DatabaseResult;
import com.donate.savelife.core.home.displayer.HomeDisplayer;
import com.donate.savelife.core.navigation.Navigator;
import com.donate.savelife.core.notifications.database.FCMRemoteMsg;
import com.donate.savelife.core.requirement.model.Needs;
import com.donate.savelife.core.requirement.service.NeedService;
import com.donate.savelife.core.user.data.model.User;
import com.donate.savelife.core.utils.AppConstant;
import com.donate.savelife.core.utils.GsonService;
import com.donate.savelife.core.utils.SharedPreferenceService;

import rx.functions.Action1;
import rx.subscriptions.CompositeSubscription;

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
    private final NeedService needService;
    private CompositeSubscription compositeSubscription = new CompositeSubscription();

    public HomePresenter(HomeDisplayer homeDisplayer,
                         SharedPreferenceService preferenceService,
                         GsonService gsonService,
                         Navigator navigator,
                         Analytics analytics,
                         ErrorLogger errorLogger,
                         NeedService needService){
        this.homeDisplayer = homeDisplayer;
        this.preferenceService = preferenceService;
        this.gsonService = gsonService;
        this.navigator = navigator;
        this.analytics = analytics;
        this.errorLogger = errorLogger;
        this.needService = needService;
        this.user = gsonService.toUser(preferenceService.getLoginUserPreference());
        homeDisplayer.setUpViewPager();
        homeDisplayer.setProfile(user);
        analytics.setUserIdProperty(user.getId());
    }

    public void startPresenting(){
        homeDisplayer.attach(homeInteractionListener);
        compositeSubscription.add(
                needService.observeLatestNeedsFor(user)
                .subscribe(new Action1<DatabaseResult<Needs>>() {
                    @Override
                    public void call(DatabaseResult<Needs> needsDatabaseResult) {
                        if (needsDatabaseResult.isSuccess()){
                            homeDisplayer.toggleMyNeedVisibility(needsDatabaseResult.getData().size() > 0);
                        } else {
                            errorLogger.reportError(needsDatabaseResult.getFailure(), "Unable to fetch user latest need");
                        }
                    }
                })
        );
    }

    public void stopPresenting(){
        homeDisplayer.detach(homeInteractionListener);
        compositeSubscription.clear();
        compositeSubscription = new CompositeSubscription();
    }

    private final HomeDisplayer.HomeInteractionListener homeInteractionListener = new HomeDisplayer.HomeInteractionListener() {

        @Override
        public void onFabBtnClicked() {
            navigator.toNeed();
            Bundle toPostNeedBundle = new Bundle();
            toPostNeedBundle.putString(Analytics.PARAM_OWNER_ID, user.getId());
            toPostNeedBundle.putString(Analytics.PARAM_EVENT_NAME, Analytics.PARAM_OPEN_BLOOD_REQUEST);
            analytics.trackEventOnClick(toPostNeedBundle);
        }

        @Override
        public void onProfileClicked() {
            Message message = new Message();
            message.setNeedId("");
            message.setUserId(user.getId());
            navigator.toProfile(message);
            Bundle toProfileBundle = new Bundle();
            toProfileBundle.putString(Analytics.PARAM_OWNER_ID, user.getId());
            toProfileBundle.putString(Analytics.PARAM_EVENT_NAME, Analytics.PARAM_OPEN_PROFILE);
            analytics.trackEventOnClick(toProfileBundle);
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

        @Override
        public void onMyNeedClicked() {
            navigator.toMyNeeds();
            Bundle toProfileBundle = new Bundle();
            toProfileBundle.putString(Analytics.PARAM_OWNER_ID, user.getId());
            toProfileBundle.putString(Analytics.PARAM_EVENT_NAME, Analytics.PARAM_OPEN_MY_NEED);
            analytics.trackEventOnClick(toProfileBundle);
        }

        @Override
        public void onNotificationClicked(FCMRemoteMsg fcmRemoteMsg) {
            FCMRemoteMsg.Data data = fcmRemoteMsg.getData();
            switch (data.getClick_action()){
                case AppConstant.CLICK_ACTION_CHAT:
                    navigator.toChat(data.getNeed_id());
                    break;

                case AppConstant.CLICK_ACTION_PROFILE:
                    Message message = new Message();
                    message.setNeedId("");
                    message.setUserId(user.getId());
                    navigator.toProfile(message);
                    break;
            }
        }
    };
}
