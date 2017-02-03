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
    private CompositeSubscription compositeSubscription = new CompositeSubscription();

    public LauncherPresenter(Navigator navigator, SharedPreferenceService preferenceService,
                             GsonService gsonService, AppStatusService appStatusService,
                             LauncherDisplayer launcherDisplayer) {
        this.navigator = navigator;
        this.sharedPreferenceService = preferenceService;
        this.gsonService = gsonService;
        this.appStatusService = appStatusService;
        this.launcherDisplayer = launcherDisplayer;
    }

    public void startPresenting() {
        launcherDisplayer.attach(launcherInteractionListener);
        doInit();

    }

    public void stopPresenting() {
        launcherDisplayer.detach(null);
    }

    public void doInit(){
        AppStatus appStatus = observeAppStatus();
        if (!appStatus.isDeprecated() || !appStatus.isError()){
            manageFirstFlow();
        } else {
            launcherDisplayer.display(appStatus);
        }
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

    private AppStatus observeAppStatus() {
        final AppStatus[] appStatus = {new AppStatus()};
            compositeSubscription.add(
                 appStatusService.observeLatestStatus()
                    .subscribe(new Action1<DatabaseResult<AppStatus>>() {
                        @Override
                        public void call(DatabaseResult<AppStatus> appStatusDatabaseResult) {
                            if (appStatusDatabaseResult.isSuccess()){
                                appStatus[0] = appStatusDatabaseResult.getData();
                            } else {
                                AppStatus appStatus1 = new AppStatus();
                                appStatus1.setId("0");
                                appStatus1.setError(true);
                                appStatus1.setDeprecated(false);
                                appStatus1.setUpdateAvailable(false);
                                appStatus[0] = appStatus1;
                            }
                        }
                    })
            );

        return appStatus[0];
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
