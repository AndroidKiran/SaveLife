package com.donate.savelife.user;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.donate.savelife.R;
import com.donate.savelife.core.analytics.Analytics;
import com.donate.savelife.core.chats.model.Message;
import com.donate.savelife.core.user.displayer.ProfileDisplayer;
import com.donate.savelife.core.user.presenter.ProfilePresenter;
import com.donate.savelife.core.utils.AppConstant;
import com.donate.savelife.firebase.Dependencies;
import com.donate.savelife.navigation.AndroidNavigator;

import static com.donate.savelife.core.utils.AppConstant.BUNDLE_EXTRA;
import static com.donate.savelife.core.utils.AppConstant.MESSAGE_EXTRA;

/**
 * Created by ravi on 19/11/16.
 */

public class ProfileActivity extends AppCompatActivity {

    private ProfilePresenter presenter;
    private Bundle intentBundle;
    private Message messageExtra;
    private Analytics analytics;

    public static Intent createIntentFor(Context context, Message message) {
        Intent intent = new Intent(context, ProfileActivity.class);
        Bundle bundle = new Bundle();
        bundle.putParcelable(MESSAGE_EXTRA, message);
        intent.putExtras(bundle);
        return intent;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        analytics = Dependencies.INSTANCE.getAnalytics();
        if (savedInstanceState == null){
            intentBundle = getIntent().getExtras();
            analytics.trackScreen(this, AppConstant.PROFILE_SCREEN, null);
        } else {
            intentBundle = savedInstanceState.getBundle(BUNDLE_EXTRA);
        }

        messageExtra = (Message) intentBundle.getParcelable(MESSAGE_EXTRA);

        ProfileDisplayer profileDisplayer = (ProfileDisplayer) findViewById(R.id.profile_view);
        presenter = new ProfilePresenter(
                profileDisplayer,
                Dependencies.INSTANCE.getPreference(),
                new AndroidNavigator(this),
                Dependencies.INSTANCE.getGsonService(),
                Dependencies.INSTANCE.getUserService(),
                Dependencies.INSTANCE.getErrorLogger(),
                analytics,
                messageExtra,
                Dependencies.INSTANCE.getHeroService()
        );
    }

    @Override
    protected void onStart() {
        super.onStart();
        presenter.startPresenting();
    }


    @Override
    protected void onStop() {
        presenter.stopPresenting();
        super.onStop();
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBundle(BUNDLE_EXTRA, intentBundle);
    }
}
