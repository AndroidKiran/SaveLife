package com.donate.savelife.core.navigation;

public interface LoginNavigator {

    void toParent();

    void toGooglePlusLogin();

    void attach(LoginResultListener loginResultListener);

    void detach(LoginResultListener loginResultListener);

    interface LoginResultListener {

        void onGooglePlusLoginSuccess(String tokenId);

        void onGooglePlusLoginFailed(String statusMessage);

    }

}
