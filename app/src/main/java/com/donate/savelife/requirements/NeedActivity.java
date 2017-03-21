package com.donate.savelife.requirements;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.donate.savelife.R;
import com.donate.savelife.core.country.OnCityInteractionListener;
import com.donate.savelife.core.country.model.City;
import com.donate.savelife.core.requirement.displayer.NeedDisplayer;
import com.donate.savelife.core.requirement.presenter.NeedPresenter;
import com.donate.savelife.firebase.Dependencies;
import com.donate.savelife.navigation.AndroidNavigator;
import com.donate.savelife.requirements.view.NeedView;

/**
 * Created by ravi on 19/11/16.
 */

public class NeedActivity extends AppCompatActivity implements OnCityInteractionListener {

    private NeedPresenter presenter;

    public static Intent createIntentFor(Context context) {
        Intent intent = new Intent(context, NeedActivity.class);
        return intent;
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_need);
        NeedDisplayer needDisplayer = (NeedDisplayer) findViewById(R.id.need_view);
        ((NeedView) needDisplayer).setActivity(this);

        presenter = new NeedPresenter(
                needDisplayer,
                new AndroidNavigator(this),
                Dependencies.INSTANCE.getNeedService(),
                Dependencies.INSTANCE.getErrorLogger(),
                Dependencies.INSTANCE.getAnalytics(),
                Dependencies.INSTANCE.getPreference(),
                Dependencies.INSTANCE.getGsonService()
        );

    }

    @Override
    protected void onStart() {
        super.onStart();
        presenter.startPresenting();
    }

    @Override
    protected void onPause() {
        presenter.pausePresenting();
        super.onPause();
    }

    @Override
    protected void onStop() {
        presenter.stopPresenting();
        super.onStop();
    }

    @Override
    public void onCityInteraction(City city) {
        presenter.onFragmentInteractionListener(city);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
    }
}
