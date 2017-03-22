package com.donate.savelife.welcome;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.donate.savelife.R;
import com.donate.savelife.core.analytics.Analytics;
import com.donate.savelife.core.utils.AppConstant;
import com.donate.savelife.core.welcome.displayer.WelcomeDisplayer;
import com.donate.savelife.core.welcome.presenter.WelcomePresenter;
import com.donate.savelife.firebase.Dependencies;
import com.donate.savelife.link.FirebaseDynamicLinkFactory;
import com.donate.savelife.navigation.AndroidNavigator;


public class WelcomeActivity extends AppCompatActivity {

    private WelcomePresenter welcomePresenter;
    private Analytics analytics;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        analytics = Dependencies.INSTANCE.getAnalytics();

        if (savedInstanceState == null)
            analytics.trackScreen(this, AppConstant.WELCOME_SCREEN, null);

        String sender = getIntent().getData().getQueryParameter(FirebaseDynamicLinkFactory.SENDER);
        String senderName = getIntent().getData().getQueryParameter(FirebaseDynamicLinkFactory.SENDER_NAME);
        String senderPhoto = getIntent().getData().getQueryParameter(FirebaseDynamicLinkFactory.SENDER_photo);

        welcomePresenter = new WelcomePresenter(
                Dependencies.INSTANCE.getUserService(),
                (WelcomeDisplayer) findViewById(R.id.welcome_view),
                new AndroidNavigator(this),
                analytics,
                senderName,
                sender,
                senderPhoto,
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
