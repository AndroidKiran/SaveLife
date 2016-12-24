package com.donate.savelife.core.login.presenter;


import com.donate.savelife.core.utils.GsonService;
import com.donate.savelife.core.utils.SharedPreferenceService;
import com.donate.savelife.core.analytics.Analytics;
import com.donate.savelife.core.analytics.ErrorLogger;
import com.donate.savelife.core.database.DatabaseResult;
import com.donate.savelife.core.login.data.model.Authentication;
import com.donate.savelife.core.login.displayer.LoginDisplayer;
import com.donate.savelife.core.login.service.LoginService;
import com.donate.savelife.core.navigation.LoginNavigator;
import com.donate.savelife.core.user.data.model.User;
import com.donate.savelife.core.user.service.UserService;

import rx.Observable;
import rx.Subscription;
import rx.functions.Action1;
import rx.functions.Func1;

public class LoginPresenter {

    private final LoginService loginService;
    private final LoginDisplayer loginDisplayer;
    private final LoginNavigator navigator;
    private final ErrorLogger errorLogger;
    private final Analytics analytics;
    private final SharedPreferenceService preferenceService;
    private final GsonService gsonService;
    private final UserService userService;

    private Subscription subscription;

    public LoginPresenter(LoginService loginService,
                          UserService userService,
                          LoginDisplayer loginDisplayer,
                          LoginNavigator navigator,
                          ErrorLogger errorLogger,
                          Analytics analytics,
                          SharedPreferenceService preferenceService,
                          GsonService gsonService) {
        this.loginService = loginService;
        this.userService = userService;
        this.loginDisplayer = loginDisplayer;
        this.navigator = navigator;
        this.errorLogger = errorLogger;
        this.analytics = analytics;
        this.preferenceService = preferenceService;
        this.gsonService = gsonService;
    }

    public void startPresenting() {
        navigator.attach(loginResultListener);
        loginDisplayer.attach(actionListener);
        subscription = loginService.getAuthentication()
                .filter(successfullyAuthenticated())
                .flatMap(getUser())
                .subscribe(new Action1<DatabaseResult<User>>() {
                    @Override
                    public void call(DatabaseResult<User> userDatabaseResult) {
                        loginDisplayer.dismissProgress();
                        if (userDatabaseResult.isSuccess()){
                            User user = userDatabaseResult.getData();
                            preferenceService.setLoginUserPreference(gsonService.toString(user));
                            navigator.toParent();
                        } else {
                            errorLogger.reportError(userDatabaseResult.getFailure(), "Login failed");
                            loginDisplayer.showAuthenticationError("Please retry");
                        }
                    }
                });
    }

    public void stopPresenting() {
        loginDisplayer.dismissProgress();
        navigator.detach(loginResultListener);
        loginDisplayer.detach(actionListener);
        subscription.unsubscribe(); //TODO handle checks
    }

    private final LoginDisplayer.LoginActionListener actionListener = new LoginDisplayer.LoginActionListener() {

        @Override
        public void onGooglePlusLoginSelected() {
            analytics.trackSignInStarted("google");
            navigator.toGooglePlusLogin();
        }

    };

    private final LoginNavigator.LoginResultListener loginResultListener = new LoginNavigator.LoginResultListener() {
        @Override
        public void onGooglePlusLoginSuccess(String tokenId) {
            analytics.trackSignInSuccessful("google");
            loginDisplayer.showProgress();
            loginService.loginWithGoogle(tokenId);
        }

        @Override
        public void onGooglePlusLoginFailed(String statusMessage) {
            loginDisplayer.dismissProgress();
            loginDisplayer.showAuthenticationError(statusMessage);
        }
    };

    private Func1<Authentication, Boolean> successfullyAuthenticated() {
        return new Func1<Authentication, Boolean>() {
            @Override
            public Boolean call(Authentication authentication) {
                return authentication.isSuccess();
            }
        };
    }


    private Func1<Authentication, Observable<DatabaseResult<User>>> getUser(){
        return new Func1<Authentication, Observable<DatabaseResult<User>>>() {
            @Override
            public Observable<DatabaseResult<User>> call(Authentication authentication) {
                return userService.observeUser(authentication.getUser().getId());
            }
        };
    }
}
