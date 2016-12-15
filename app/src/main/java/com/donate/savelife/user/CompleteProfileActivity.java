package com.donate.savelife.user;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.donate.savelife.R;
import com.donate.savelife.core.country.OnFragmentInteractionListener;
import com.donate.savelife.core.country.model.Country;
import com.donate.savelife.core.user.displayer.CompleteProfileDisplayer;
import com.donate.savelife.core.user.presenter.CompleteProfilePresenter;
import com.donate.savelife.firebase.Dependencies;
import com.donate.savelife.navigation.AndroidNavigator;
import com.donate.savelife.user.view.CompleteProfileView;

/**
 * Created by ravi on 19/11/16.
 */

public class CompleteProfileActivity extends AppCompatActivity implements OnFragmentInteractionListener{

    private CompleteProfilePresenter presenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complete_profile);
        CompleteProfileDisplayer completeProfileDisplayer = (CompleteProfileDisplayer) findViewById(R.id.complete_profile_view);
        ((CompleteProfileView)completeProfileDisplayer).setActivity(this);
        presenter = new CompleteProfilePresenter(
                completeProfileDisplayer,
                Dependencies.INSTANCE.getPreference(),
                new AndroidNavigator(this),
                Dependencies.INSTANCE.getGsonService(),
                Dependencies.INSTANCE.getUserService(),
                Dependencies.INSTANCE.getErrorLogger(),
                Dependencies.INSTANCE.getAnalytics()
        );
        presenter.initPresenter();
    }

    @Override
    protected void onStart() {
        super.onStart();
        presenter.startPresenting();
    }

    @Override
    protected void onPause() {
        presenter.onPause();
        super.onPause();
    }

    @Override
    protected void onStop() {
        presenter.stopPresenting();
        super.onStop();
    }

    @Override
    public void onFragmentInteraction(Country country) {
        presenter.onFragmentInteractionListener(country);
    }
}
