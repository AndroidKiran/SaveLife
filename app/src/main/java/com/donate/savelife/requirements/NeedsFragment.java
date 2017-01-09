package com.donate.savelife.requirements;

import android.content.Intent;
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
import com.donate.savelife.firebase.Dependencies;
import com.donate.savelife.navigation.AndroidNavigator;

import static com.donate.savelife.navigation.AndroidNavigator.SEEK_NEED;
import static com.donate.savelife.navigation.AndroidNavigator.SEEK_NEED_SUCCESS;

/**
 * Created by ravi on 20/11/16.
 */

public class NeedsFragment extends Fragment {

    public static final String TAG = UtilBundles.NEEDS_SCREEN;

    private NeedsPresenter presenter;

    public static NeedsFragment newInstance(Bundle bundle) {
        NeedsFragment instance = new NeedsFragment();
        instance.setArguments(bundle);
        return instance;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
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
                Dependencies.INSTANCE.getPreference(),
                Dependencies.INSTANCE.getGsonService()
        );
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState == null){
            presenter.initPresenter();
        } else {
            presenter.onRestoreInstanceState(savedInstanceState);
        }
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == SEEK_NEED && resultCode == SEEK_NEED_SUCCESS){
            presenter.initPresenter();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        presenter.onSaveInstanceState(outState);
    }

}
