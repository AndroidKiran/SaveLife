package com.donate.savelife.notifications.services;

import com.donate.savelife.core.notifications.service.AppNotificationService;
import com.donate.savelife.core.user.data.model.User;
import com.donate.savelife.core.utils.GsonService;
import com.donate.savelife.core.utils.SharedPreferenceService;
import com.google.firebase.messaging.FirebaseMessaging;

/**
 * Created by ravi on 10/01/17.
 */

public class AppNotificationServiceImpl implements AppNotificationService {

    private final FirebaseMessaging firebaseMessaging;
    private final GsonService gsonService;
    private final SharedPreferenceService sharedPreferenceService;
    private final User user;

    public AppNotificationServiceImpl(FirebaseMessaging firebaseMessaging, GsonService gsonService, SharedPreferenceService sharedPreferenceService){
        this.firebaseMessaging = firebaseMessaging;
        this.gsonService = gsonService;
        this.sharedPreferenceService = sharedPreferenceService;
        user = gsonService.toUser(sharedPreferenceService.getLoginUserPreference());
    }

    @Override
    public void toggleNotificationStatus(boolean toggle) {
        String needTopic = user.getCity().toLowerCase() + user.getCountry().toLowerCase();
        if (toggle){
            firebaseMessaging.subscribeToTopic(needTopic);
        } else {
            firebaseMessaging.unsubscribeFromTopic(needTopic);
        }
        sharedPreferenceService.setNotificationEnabled(toggle);
    }

    @Override
    public void toggleNeedNotificationStatus(boolean toggle) {
        String needTopic = user.getCity().toLowerCase() + user.getCountry().toLowerCase();
        if (toggle){
            firebaseMessaging.subscribeToTopic(needTopic);
        } else {
            firebaseMessaging.unsubscribeFromTopic(needTopic);
        }
    }
}
