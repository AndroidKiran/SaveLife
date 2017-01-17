package com.donate.savelife.preferences;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.donate.savelife.BuildConfig;
import com.donate.savelife.R;
import com.donate.savelife.apputils.UtilBundles;
import com.donate.savelife.core.analytics.Analytics;
import com.donate.savelife.core.analytics.ErrorLogger;
import com.donate.savelife.core.notifications.service.AppNotificationService;
import com.donate.savelife.core.preferences.displayer.PreferenceDisplayer;
import com.donate.savelife.core.preferences.presenter.PreferencePresenter;
import com.donate.savelife.core.utils.GsonService;
import com.donate.savelife.core.utils.SharedPreferenceService;
import com.donate.savelife.firebase.Dependencies;
import com.donate.savelife.link.FirebaseDynamicLinkFactory;
import com.donate.savelife.navigation.AndroidNavigator;
import com.donate.savelife.notifications.services.AppNotificationServiceImpl;

/**
 * Created by ravi on 24/12/16.
 */

public class PreferenceFragment extends Fragment {

    public static final String TAG = UtilBundles.PREFERENCES_SCREEN;
    private FirebaseDynamicLinkFactory firebaseDynamicLinkFactory;
    private PreferencePresenter presenter;
    private ErrorLogger errorLogger;
    private Analytics analytics;
    private GsonService gsonService;
    private SharedPreferenceService preference;
    private AppNotificationServiceImpl appNotificationSerive;

    public static PreferenceFragment newInstance(Bundle bundle) {
        PreferenceFragment instance = new PreferenceFragment();
        instance.setArguments(bundle);
        return instance;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        firebaseDynamicLinkFactory = new FirebaseDynamicLinkFactory(
                getResources().getString(R.string.dynamicLinkDomain),
                getResources().getString(R.string.deepLinkBaseUrl),
                BuildConfig.APPLICATION_ID
        );

        errorLogger = Dependencies.INSTANCE.getErrorLogger();
        analytics = Dependencies.INSTANCE.getAnalytics();
        gsonService = Dependencies.INSTANCE.getGsonService();
        preference = Dependencies.INSTANCE.getPreference();
        appNotificationSerive = new AppNotificationServiceImpl(Dependencies.INSTANCE.getFirebaseMessageInstance(), gsonService, preference);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.preference_view, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        PreferenceDisplayer preferenceDisplayer = (PreferenceDisplayer) view.findViewById(R.id.preference_view);
        presenter = new PreferencePresenter(preferenceDisplayer,
                new AndroidNavigator(getActivity()),
                errorLogger,
                analytics,
                gsonService,
                preference,
                firebaseDynamicLinkFactory,
                (AppNotificationService) appNotificationSerive);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    @Override
    public void onStart() {
        super.onStart();
        presenter.startPresenting();
    }

    @Override
    public void onStop() {
        super.onStop();
        presenter.stopPresenting();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }
}
