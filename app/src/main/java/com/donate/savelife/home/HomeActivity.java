package com.donate.savelife.home;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;

import com.donate.savelife.R;
import com.donate.savelife.component.ViewPagerAdapter;
import com.donate.savelife.core.home.displayer.HomeDisplayer;
import com.donate.savelife.core.home.presenter.HomePresenter;
import com.donate.savelife.core.utils.GsonService;
import com.donate.savelife.core.utils.SharedPreferenceService;
import com.donate.savelife.firebase.Dependencies;
import com.donate.savelife.home.view.HomeView;
import com.donate.savelife.navigation.AndroidNavigator;
import com.donate.savelife.preferences.PreferenceFragment;
import com.donate.savelife.requirements.NeedsFragment;
import com.donate.savelife.user.HerosFragment;

/**
 * Created by ravi on 09/09/16.
 */
public class HomeActivity extends AppCompatActivity {

    private NeedsFragment needsFragment;
    private HerosFragment herosFragment;
    private HomePresenter homePresenter;
    private PreferenceFragment preferenceFragment;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        SharedPreferenceService preferenceService = Dependencies.INSTANCE.getPreference();
        GsonService gsonService = Dependencies.INSTANCE.getGsonService();

        HomeDisplayer homeDisplayer = (HomeDisplayer) findViewById(R.id.home);
        HomeView homeView = ((HomeView) homeDisplayer);
        homeView.setAppCompatActivity(this);
        homeView.setViewPagerAdapter(getViewPagerAdapter(savedInstanceState));

        homePresenter = new HomePresenter(homeDisplayer,
                preferenceService,
                gsonService,
                new AndroidNavigator(this),
                Dependencies.INSTANCE.getAnalytics(),
                Dependencies.INSTANCE.getErrorLogger()
        );

    }


    private ViewPagerAdapter getViewPagerAdapter(Bundle savedInstanceState) {
        if (savedInstanceState == null) {
            needsFragment = NeedsFragment.newInstance(null);
            herosFragment = HerosFragment.newInstance(null);
            preferenceFragment = PreferenceFragment.newInstance(null);

        } else {
            needsFragment = (NeedsFragment) getSupportFragmentManager().getFragment(savedInstanceState, NeedsFragment.TAG);
            herosFragment = (HerosFragment) getSupportFragmentManager().getFragment(savedInstanceState, HerosFragment.TAG);
            preferenceFragment = (PreferenceFragment) getSupportFragmentManager().getFragment(savedInstanceState, PreferenceFragment.TAG);

        }
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(), this);
        viewPagerAdapter.addFragment(needsFragment, getString(R.string.str_requirement_tab));
        viewPagerAdapter.addFragment(herosFragment, getString(R.string.str_heros_tab));
        viewPagerAdapter.addFragment(preferenceFragment, "Prefernces");

        return viewPagerAdapter;
    }

    @Override
    protected void onStart() {
        super.onStart();
        homePresenter.startPresenting();
    }

    @Override
    protected void onPause() {
        homePresenter.stopPresenting();
        super.onStop();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        for (Fragment fragment : getSupportFragmentManager().getFragments()) {
            fragment.onActivityResult(requestCode, resultCode, data);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        if (needsFragment != null && needsFragment.isAdded()) {
            getSupportFragmentManager().putFragment(outState, NeedsFragment.TAG, needsFragment);
        }
        if (herosFragment != null && herosFragment.isAdded()) {
            getSupportFragmentManager().putFragment(outState, HerosFragment.TAG, herosFragment);
        }
        if (preferenceFragment != null && preferenceFragment.isAdded()) {
            getSupportFragmentManager().putFragment(outState, PreferenceFragment.TAG, preferenceFragment);
        }
        super.onSaveInstanceState(outState);
    }

}
