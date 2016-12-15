package com.donate.savelife.welcome;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.donate.savelife.R;
import com.donate.savelife.core.welcome.displayer.WelcomeDisplayer;
import com.donate.savelife.core.welcome.presenter.WelcomePresenter;
import com.donate.savelife.firebase.Dependencies;
import com.donate.savelife.link.FirebaseDynamicLinkFactory;
import com.donate.savelife.navigation.AndroidNavigator;


public class WelcomeActivity extends AppCompatActivity {

    private WelcomePresenter welcomePresenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        String sender = getIntent().getData().getQueryParameter(FirebaseDynamicLinkFactory.SENDER);
        welcomePresenter = new WelcomePresenter(
                Dependencies.INSTANCE.getUserService(),
                (WelcomeDisplayer) findViewById(R.id.welcome_view),
                new AndroidNavigator(this),
                Dependencies.INSTANCE.getAnalytics(),
                sender,
                Dependencies.INSTANCE.getErrorLogger()
        );
    }

    @Override
    protected void onStart() {
        super.onStart();
        welcomePresenter.startPresenting();
    }

    @Override
    protected void onStop() {
        super.onStop();
        welcomePresenter.stopPresenting();
    }
}
