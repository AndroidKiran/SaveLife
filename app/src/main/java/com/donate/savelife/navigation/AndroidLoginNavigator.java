package com.donate.savelife.navigation;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;

import com.donate.savelife.core.navigation.LoginNavigator;
import com.donate.savelife.core.navigation.Navigator;
import com.donate.savelife.login.LoginGoogleApiClient;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.novoda.notils.logger.simple.Log;

public class AndroidLoginNavigator implements LoginNavigator {

    private static final int RC_SIGN_IN = 242;

    private final AppCompatActivity activity;
    private final LoginGoogleApiClient googleApiClient;
    private final Navigator navigator;
    private LoginResultListener loginResultListener;

    public AndroidLoginNavigator(AppCompatActivity activity, LoginGoogleApiClient googleApiClient, Navigator navigator) {
        this.activity = activity;
        this.googleApiClient = googleApiClient;
        this.navigator = navigator;
    }


    @Override
    public void toParent() {
        navigator.toParent();
    }

    @Override
    public void toGooglePlusLogin() {
        Intent signInIntent = googleApiClient.getSignInIntent();
        activity.startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void attach(LoginResultListener loginResultListener) {
        this.loginResultListener = loginResultListener;
    }

    @Override
    public void detach(LoginResultListener loginResultListener) {
        this.loginResultListener = null;
    }

    public boolean onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode != RC_SIGN_IN) {
            return false;
        }
        GoogleSignInResult result = googleApiClient.getSignInResultFromIntent(data);
        if (result.isSuccess()) {
            GoogleSignInAccount account = result.getSignInAccount();
            loginResultListener.onGooglePlusLoginSuccess(account.getIdToken());
        } else {
            Log.e("Failed to authenticate GooglePlus", result.getStatus().getStatusCode());
            loginResultListener.onGooglePlusLoginFailed(result.getStatus().getStatusMessage());
        }
        return true;
    }
}
