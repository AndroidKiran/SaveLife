package com.donate.savelife.user;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.donate.savelife.R;
import com.donate.savelife.core.user.displayer.ProfileDisplayer;
import com.donate.savelife.core.user.presenter.ProfilePresenter;
import com.donate.savelife.firebase.Dependencies;
import com.donate.savelife.navigation.AndroidNavigator;

/**
 * Created by ravi on 19/11/16.
 */

public class ProfileActivity extends AppCompatActivity {
    private static final String NEED_EXTRA = "need";
    private static final String USER_EXTRA = "user";
    private static final String BUNDLE_EXTRA = "bundle_extra";


    private ProfilePresenter presenter;
    private Bundle intentBundle;
    private String needID;
    private String userID;

    public static Intent createIntentFor(Context context, String needID, String userID) {
        Intent intent = new Intent(context, ProfileActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString(NEED_EXTRA, needID);
        bundle.putString(USER_EXTRA, userID);
        intent.putExtras(bundle);
        return intent;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        if (savedInstanceState == null){
            intentBundle = getIntent().getExtras();
        } else {
            intentBundle = savedInstanceState.getBundle(BUNDLE_EXTRA);
        }

        needID = intentBundle.getString(NEED_EXTRA, "");
        userID = intentBundle.getString(USER_EXTRA, "");

        ProfileDisplayer profileDisplayer = (ProfileDisplayer) findViewById(R.id.profile_view);
        presenter = new ProfilePresenter(
                profileDisplayer,
                Dependencies.INSTANCE.getPreference(),
                new AndroidNavigator(this),
                Dependencies.INSTANCE.getGsonService(),
                Dependencies.INSTANCE.getUserService(),
                Dependencies.INSTANCE.getErrorLogger(),
                Dependencies.INSTANCE.getAnalytics(),
                userID,
                needID,
                Dependencies.INSTANCE.getHeroService()
        );
        presenter.initPresenter();
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
