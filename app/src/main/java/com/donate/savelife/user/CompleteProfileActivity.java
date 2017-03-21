package com.donate.savelife.user;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.widget.Toast;

import com.donate.savelife.R;
import com.donate.savelife.core.analytics.Analytics;
import com.donate.savelife.core.country.OnCityInteractionListener;
import com.donate.savelife.core.country.model.City;
import com.donate.savelife.core.user.data.model.User;
import com.donate.savelife.core.user.displayer.CompleteProfileDisplayer;
import com.donate.savelife.core.user.presenter.CompleteProfilePresenter;
import com.donate.savelife.core.utils.AppConstant;
import com.donate.savelife.core.utils.GsonService;
import com.donate.savelife.core.utils.SharedPreferenceService;
import com.donate.savelife.firebase.Dependencies;
import com.donate.savelife.navigation.AndroidNavigator;
import com.donate.savelife.user.view.CompleteProfileView;

/**
 * Created by ravi on 19/11/16.
 */

public class CompleteProfileActivity extends AppCompatActivity implements OnCityInteractionListener {

    private CompleteProfilePresenter presenter;
    private User user;
    private Analytics analytics;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complete_profile);
        analytics = Dependencies.INSTANCE.getAnalytics();

        if (savedInstanceState == null)
            analytics.trackScreen(this, AppConstant.EDIT_PROFILE_SCREEN, null);

        CompleteProfileDisplayer completeProfileDisplayer = (CompleteProfileDisplayer) findViewById(R.id.complete_profile_view);
        ((CompleteProfileView)completeProfileDisplayer).setActivity(this);
        GsonService gsonService =  Dependencies.INSTANCE.getGsonService();
        SharedPreferenceService sharedPreferenceService = Dependencies.INSTANCE.getPreference();
        user = gsonService.toUser(sharedPreferenceService.getLoginUserPreference());

        presenter = new CompleteProfilePresenter(
                completeProfileDisplayer,
                sharedPreferenceService,
                new AndroidNavigator(this),
                gsonService,
                Dependencies.INSTANCE.getUserService(),
                Dependencies.INSTANCE.getErrorLogger(),
                analytics
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
    public void onCityInteraction(City city) {
        presenter.onFragmentInteractionListener(city);

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            if (user == null || TextUtils.isEmpty(user.getCity()))
                Toast.makeText(this, getString(R.string.str_complete_on_boarding), Toast.LENGTH_SHORT).show();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }


}
