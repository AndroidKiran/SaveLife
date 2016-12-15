package com.donate.savelife.login;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.donate.savelife.R;
import com.donate.savelife.apputils.UtilBundles;
import com.donate.savelife.component.ViewPagerAdapter;
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
    private OnBoardFragment onBoardFragment1;
    private OnBoardFragment onBoardFragment2;
    private OnBoardFragment onBoardFragment3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        LoginView loginView = (LoginView) findViewById(R.id.login_view);
        loginView.setAppCompatActivity(this);
        loginView.setViewPagerAdapter(getViewPagerAdapter(savedInstanceState));
        loginView.setUpViewPager();

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

    private ViewPagerAdapter getViewPagerAdapter(Bundle savedInstanceState) {
        if (savedInstanceState == null) {
            Bundle bundle = new Bundle();
            bundle.putInt(UtilBundles.EXTRA_INT, OnBoardFragment.TYPE_ONE);
            onBoardFragment1 = OnBoardFragment.newInstance(bundle);
            bundle = new Bundle();
            bundle.putInt(UtilBundles.EXTRA_INT, OnBoardFragment.TYPE_TWO);
            onBoardFragment2 = OnBoardFragment.newInstance(bundle);
            bundle = new Bundle();
            bundle.putInt(UtilBundles.EXTRA_INT, OnBoardFragment.TYPE_THREE);
            onBoardFragment3 = OnBoardFragment.newInstance(bundle);
        } else {
            onBoardFragment1 = (OnBoardFragment) getSupportFragmentManager().getFragment(savedInstanceState, OnBoardFragment.TAG_1);
            onBoardFragment2 = (OnBoardFragment) getSupportFragmentManager().getFragment(savedInstanceState, OnBoardFragment.TAG_2);
            onBoardFragment3 = (OnBoardFragment) getSupportFragmentManager().getFragment(savedInstanceState, OnBoardFragment.TAG_3);
        }
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(), this);
        viewPagerAdapter.addFragment(onBoardFragment1, getString(R.string.str_boarding_heading_type_one));
        viewPagerAdapter.addFragment(onBoardFragment2, getString(R.string.str_boarding_heading_type_two));
        viewPagerAdapter.addFragment(onBoardFragment3, getString(R.string.str_boarding_heading_type_three));
        return viewPagerAdapter;
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

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (onBoardFragment1 != null && onBoardFragment1.isAdded()) {
            getSupportFragmentManager().putFragment(outState, OnBoardFragment.TAG_1, onBoardFragment1);
        }

        if (onBoardFragment2 != null && onBoardFragment2.isAdded()) {
            getSupportFragmentManager().putFragment(outState, OnBoardFragment.TAG_2, onBoardFragment2);
        }

        if (onBoardFragment3 != null && onBoardFragment3.isAdded()) {
            getSupportFragmentManager().putFragment(outState, OnBoardFragment.TAG_3, onBoardFragment3);
        }
    }
}
