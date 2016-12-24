package com.donate.savelife.core.user.presenter;

import android.text.TextUtils;

import com.donate.savelife.core.utils.GsonService;
import com.donate.savelife.core.utils.SharedPreferenceService;
import com.donate.savelife.core.analytics.Analytics;
import com.donate.savelife.core.analytics.ErrorLogger;
import com.donate.savelife.core.country.model.Country;
import com.donate.savelife.core.database.DatabaseResult;
import com.donate.savelife.core.navigation.Navigator;
import com.donate.savelife.core.user.data.model.User;
import com.donate.savelife.core.user.displayer.CompleteProfileDisplayer;
import com.donate.savelife.core.user.service.UserService;

import rx.functions.Action1;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by ravi on 19/11/16.
 */

public class CompleteProfilePresenter {

    private final CompleteProfileDisplayer completeProfileDisplayer;
    private final SharedPreferenceService preferenceService;
    private final GsonService gsonService;
    private final UserService userService;
    private final ErrorLogger errorLogger;
    private final Analytics analytics;
    private final Navigator navigator;

    private CompositeSubscription subscriptions = new CompositeSubscription();


    public CompleteProfilePresenter(
            CompleteProfileDisplayer completeProfileDisplayer,
            SharedPreferenceService preferenceService,
            Navigator navigator,
            GsonService gsonService,
            UserService userService,
            ErrorLogger errorLogger,
            Analytics analytics
    ) {
        this.completeProfileDisplayer = completeProfileDisplayer;
        this.preferenceService = preferenceService;
        this.gsonService = gsonService;
        this.userService = userService;
        this.errorLogger = errorLogger;
        this.analytics = analytics;
        this.navigator = navigator;
    }

    public void initPresenter(){
        String userData = preferenceService.getLoginUserPreference();
        if (userData != null && !TextUtils.isEmpty(userData)) {
            User user = gsonService.toUser(userData);
            completeProfileDisplayer.display(user);
        }
    }

    public void startPresenting(){
        completeProfileDisplayer.attach(onCompleteListener);
    }

    public void onPause(){
        completeProfileDisplayer.dismissCountryDialog();
    }


    public void stopPresenting(){
        completeProfileDisplayer.detach(null);
        subscriptions.clear();
        subscriptions = new CompositeSubscription();

    }


    public void onFragmentInteractionListener(Country country){
        completeProfileDisplayer.displayCountry(country);
        analytics.trackSelectCountry(country.toString());
    }

    CompleteProfileDisplayer.OnCompleteListener onCompleteListener = new CompleteProfileDisplayer.OnCompleteListener() {
        @Override
        public void onComplete(User user) {
            completeProfileDisplayer.showProgress();
            subscriptions.add( userService.updateUser(user)
                    .subscribe(new Action1<DatabaseResult<User>>() {
                        @Override
                        public void call(DatabaseResult<User> userDatabaseResult) {
                            completeProfileDisplayer.dismissProgress();
                            if (userDatabaseResult.isSuccess()){
                                preferenceService.setLoginUserPreference(gsonService.toString(userDatabaseResult.getData()));
                                navigator.toParent();
                            } else {
                                errorLogger.reportError(userDatabaseResult.getFailure(), "Complete profile failed");
                            }
                        }
                    }));

        }

        @Override
        public void onNavigateClick() {
            navigator.toParent();
        }
    };

}
