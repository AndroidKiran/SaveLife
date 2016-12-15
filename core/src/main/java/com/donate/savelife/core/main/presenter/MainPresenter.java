package com.donate.savelife.core.main.presenter;

import android.text.TextUtils;

import com.donate.savelife.core.utils.GsonService;
import com.donate.savelife.core.utils.PreferenceService;
import com.donate.savelife.core.navigation.Navigator;
import com.donate.savelife.core.user.data.model.User;

/**
 * Created by ravi on 12/09/16.
 */
public class MainPresenter {

    private final Navigator navigator;
    private final PreferenceService preferenceService;
    private final GsonService gsonService;

    public MainPresenter(Navigator navigator, PreferenceService preferenceService, GsonService gsonService){
        this.navigator = navigator;
        this.preferenceService = preferenceService;
        this.gsonService = gsonService;
    }

    public void startPresenting(){
        manageFirstFlow();
    }

    public void stopPresenting(){

    }


    private void manageFirstFlow(){
        String userData = preferenceService.getLoginUserPreference();
        if (!TextUtils.isEmpty(userData.trim())){
            User user = gsonService.toUser(userData);
            if (user != null && user.getCity() != null && !TextUtils.isEmpty(user.getCity())) {
                navigator.toHome();
            } else {
                if (user == null){
                    navigator.toLogin();
                } else if (user.getCity() == null || TextUtils.isEmpty(user.getCity())) {
                    navigator.toCompleteProfile();
                }
            }
        } else {
            navigator.toLogin();
        }
    }
}
