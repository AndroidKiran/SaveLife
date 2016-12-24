package com.donate.savelife.login;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.donate.savelife.R;
import com.donate.savelife.core.login.displayer.LoginDisplayer;
import com.donate.savelife.core.login.presenter.LoginPresenter;
import com.donate.savelife.firebase.Dependencies;
import com.donate.savelife.login.view.LoginView;
import com.donate.savelife.navigation.AndroidLoginNavigator;
import com.donate.savelife.navigation.AndroidNavigator;


public class LoginActivity extends AppCompatActivity {

    private static final int RC_SIGN_IN = 42;

    private LoginPresenter presenter;
    private AndroidLoginNavigator navigator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        LoginView loginView = (LoginView) findViewById(R.id.login_view);
        loginView.setAppCompatActivity(this);

        LoginGoogleApiClient loginGoogleApiClient = new LoginGoogleApiClient(this);
        loginGoogleApiClient.setupGoogleApiClient();

        navigator = new AndroidLoginNavigator(this, loginGoogleApiClient, new AndroidNavigator(this));

        presenter = new LoginPresenter(Dependencies.INSTANCE.getLoginService(),
                Dependencies.INSTANCE.getUserService(),
                (LoginDisplayer) loginView,
                navigator,
                Dependencies.INSTANCE.getErrorLogger(),
                Dependencies.INSTANCE.getAnalytics(),
                Dependencies.INSTANCE.getPreference(),
                Dependencies.INSTANCE.getGsonService());

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (!navigator.onActivityResult(requestCode, resultCode, data)) {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        presenter.startPresenting();
    }

    @Override
    protected void onStop() {
        super.onStop();
        presenter.stopPresenting();
    }
}
