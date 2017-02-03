package com.donate.savelife.home;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;

import com.donate.savelife.CentralAppServiceIml;
import com.donate.savelife.NavigationActivity;
import com.donate.savelife.R;
import com.donate.savelife.apputils.UtilBundles;
import com.donate.savelife.component.ViewPagerAdapter;
import com.donate.savelife.core.home.displayer.HomeDisplayer;
import com.donate.savelife.core.home.presenter.HomePresenter;
import com.donate.savelife.core.utils.AppConstant;
import com.donate.savelife.firebase.Dependencies;
import com.donate.savelife.home.view.HomeView;
import com.donate.savelife.navigation.AndroidNavigator;
import com.donate.savelife.notifications.Config;
import com.donate.savelife.preferences.PreferenceFragment;
import com.donate.savelife.requirements.NeedsFragment;
import com.donate.savelife.user.HerosFragment;

/**
 * Created by ravi on 09/09/16.
 */
public class HomeActivity extends NavigationActivity {

    private NeedsFragment needsFragment;
    private HerosFragment herosFragment;
    private HomePresenter homePresenter;
    private PreferenceFragment preferenceFragment;
    private LocalBroadcastManager localBroadcastManager;
    private AppBroadcastReceiver reciever;
    private IntentFilter intentFilter;

    public static Intent createIntentFor(Context context) {
        Intent intent = new Intent(context, HomeActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        return intent;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (proceed){

            setContentView(R.layout.activity_home);

            if (!sharedPreferenceService.isRegistrationComplete()) {
                initRegistration();
            }

            HomeDisplayer homeDisplayer = (HomeDisplayer) findViewById(R.id.home);
            HomeView homeView = ((HomeView) homeDisplayer);
            homeView.setAppCompatActivity(this);
            homeView.setViewPagerAdapter(getViewPagerAdapter(savedInstanceState));

            homePresenter = new HomePresenter(homeDisplayer,
                    sharedPreferenceService,
                    gsonService,
                    new AndroidNavigator(this),
                    Dependencies.INSTANCE.getAnalytics(),
                    Dependencies.INSTANCE.getErrorLogger(),
                    Dependencies.INSTANCE.getNeedService()
            );

            intentFilter = new IntentFilter();
            intentFilter.addAction(Config.REGISTRATION_COMPLETE);
            intentFilter.addAction(Config.PUSH_NOTIFICATION);
            localBroadcastManager = LocalBroadcastManager.getInstance(this);
            reciever = new AppBroadcastReceiver();
        }

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
        viewPagerAdapter.addFragment(preferenceFragment, getString(R.string.str_preference_tab));

        return viewPagerAdapter;
    }

    @Override
    protected void onStart() {
        super.onStart();
        homePresenter.startPresenting();
        if (localBroadcastManager != null && reciever != null && intentFilter != null)
            localBroadcastManager.registerReceiver(reciever, intentFilter);
    }

    @Override
    protected void onPause() {
        homePresenter.stopPresenting();
        if (localBroadcastManager != null && reciever != null)
            localBroadcastManager.unregisterReceiver(reciever);
        super.onStop();
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


    private void initRegistration() {
        String regId = sharedPreferenceService.getRegistrationId();
        Bundle regBundle = new Bundle();
        regBundle.putString(UtilBundles.USER_EXTRA, user.getId());
        regBundle.putString(UtilBundles.REG_EXTRA, regId);
        CentralAppServiceIml.startActionSend(this, regBundle, AppConstant.ACTION_SEND_REG_ID_TO_SERVER);
    }

    public class AppBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {

            switch (intent.getAction()) {
                case Config.REGISTRATION_COMPLETE:
                    initRegistration();
                    break;

                case Config.PUSH_NOTIFICATION:

                    break;
            }

        }
    }
}
