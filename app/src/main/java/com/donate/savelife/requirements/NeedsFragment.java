package com.donate.savelife.requirements;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.donate.savelife.R;
import com.donate.savelife.apputils.UtilBundles;
import com.donate.savelife.core.requirement.displayer.NeedsDisplayer;
import com.donate.savelife.core.requirement.presenter.NeedsPresenter;
import com.donate.savelife.core.utils.GsonService;
import com.donate.savelife.core.utils.SharedPreferenceService;
import com.donate.savelife.firebase.Dependencies;
import com.donate.savelife.navigation.AndroidNavigator;

/**
 * Created by ravi on 20/11/16.
 */

public class NeedsFragment extends Fragment {

    public static final String TAG = UtilBundles.NEEDS_SCREEN;

    private NeedsPresenter presenter;
    private SharedPreferenceService preference;
    private GsonService gsonService;

    public static NeedsFragment newInstance(Bundle bundle) {
        NeedsFragment instance = new NeedsFragment();
        instance.setArguments(bundle);
        return instance;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        preference = Dependencies.INSTANCE.getPreference();
        gsonService = Dependencies.INSTANCE.getGsonService();
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.needs_view, container, false);
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        NeedsDisplayer needsDisplayer = (NeedsDisplayer) view.findViewById(R.id.needs_view);
        presenter = new NeedsPresenter(
                needsDisplayer,
                Dependencies.INSTANCE.getNeedService(),
                Dependencies.INSTANCE.getErrorLogger(),
                Dependencies.INSTANCE.getAnalytics(),
                new AndroidNavigator(getActivity()),
                preference,
                gsonService
        );
    }

    @Override
    public void onStart() {
        super.onStart();
        presenter.startPresenting();
    }

    @Override
    public void onStop() {
        presenter.stopPresenting();
        super.onStop();
    }
}
