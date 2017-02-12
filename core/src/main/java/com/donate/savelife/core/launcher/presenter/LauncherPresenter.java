package com.donate.savelife.core.launcher.presenter;

import android.text.TextUtils;

import com.donate.savelife.core.database.DatabaseResult;
import com.donate.savelife.core.launcher.displayer.LauncherDisplayer;
import com.donate.savelife.core.launcher.model.AppStatus;
import com.donate.savelife.core.launcher.service.AppStatusService;
import com.donate.savelife.core.navigation.Navigator;
import com.donate.savelife.core.user.data.model.User;
import com.donate.savelife.core.utils.GsonService;
import com.donate.savelife.core.utils.SharedPreferenceService;

import rx.functions.Action1;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by ravi on 12/09/16.
 */
public class LauncherPresenter {

    private final Navigator navigator;
    private final SharedPreferenceService sharedPreferenceService;
    private final GsonService gsonService;
    private final AppStatusService appStatusService;
    private final LauncherDisplayer launcherDisplayer;
    private final float appVersion;
    private CompositeSubscription compositeSubscription = new CompositeSubscription();

    public LauncherPresenter(Navigator navigator, SharedPreferenceService preferenceService,
                             GsonService gsonService, AppStatusService appStatusService,
                             LauncherDisplayer launcherDisplayer, float appVersion) {
        this.navigator = navigator;
        this.sharedPreferenceService = preferenceService;
        this.gsonService = gsonService;
        this.appStatusService = appStatusService;
        this.launcherDisplayer = launcherDisplayer;
        this.appVersion = appVersion;
    }

    public void startPresenting() {
        launcherDisplayer.attach(launcherInteractionListener);
        doInit();

    }

    public void stopPresenting() {
        launcherDisplayer.detach(null);
    }

    private void doInit(){
        compositeSubscription.add(
                appStatusService.observeLatestStatus()
                        .subscribe(new Action1<DatabaseResult<AppStatus>>() {
                            @Override
                            public void call(DatabaseResult<AppStatus> appStatusDatabaseResult) {
                                if (appStatusDatabaseResult.isSuccess()){
                                    AppStatus appStatus = appStatusDatabaseResult.getData();
                                    if (appStatus.isVersionDeprecated() && Float.parseFloat(appStatus.getId()) <= appVersion ){
                                        launcherDisplayer.display(appStatus);
                                        sharedPreferenceService.setVersionDeprecated(true);
                                    } else {
                                            manageFirstFlow();
                                    }
                                } else {
                                    AppStatus appStatus = new AppStatus();
                                    appStatus.setVersionDeprecated(false);
                                    appStatus.setError(true);
                                    appStatus.setUpdateAvailable(false);
                                    launcherDisplayer.display(appStatus);
                                }
                            }
                        })
        );
    }


    private void manageFirstFlow() {

        String userData = sharedPreferenceService.getLoginUserPreference();
        if (!TextUtils.isEmpty(userData.trim())) {
            User user = gsonService.toUser(userData);
            if (user != null && !TextUtils.isEmpty(user.getCity())) {
                navigator.toHome();
            } else {
                if (user == null) {
                    navigator.toIntro();
                } else if (TextUtils.isEmpty(user.getCity())) {
                    navigator.toCompleteProfile();
                }
            }
        } else {
            navigator.toIntro();
        }
    }

    LauncherDisplayer.LauncherInteractionListener launcherInteractionListener =  new LauncherDisplayer.LauncherInteractionListener() {
        @Override
        public void onUpdate() {
            navigator.toMarketPlace();
        }

        @Override
        public void onRetry() {
            doInit();
        }
    };
}
