package com.donate.savelife.user;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.donate.savelife.R;
import com.donate.savelife.apputils.UtilBundles;
import com.donate.savelife.core.user.displayer.HeroesDisplayer;
import com.donate.savelife.core.user.presenter.HeroPresenter;
import com.donate.savelife.firebase.Dependencies;
import com.donate.savelife.navigation.AndroidNavigator;

/**
 * Created by ravi on 20/11/16.
 */

public class HeroesFragment extends Fragment {

    public static final String TAG = UtilBundles.HEROS_SCREEN;
    private HeroPresenter presenter;

    public static HeroesFragment newInstance(Bundle bundle) {
        HeroesFragment instance = new HeroesFragment();
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
        return inflater.inflate(R.layout.heroes_view, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        HeroesDisplayer heroesDisplayer = (HeroesDisplayer) view.findViewById(R.id.heros_view);
        presenter = new HeroPresenter(heroesDisplayer, Dependencies.INSTANCE.getUserService(),
                new AndroidNavigator(getActivity()),
                Dependencies.INSTANCE.getErrorLogger(),
                Dependencies.INSTANCE.getAnalytics());
    }

    @Override
    public void onStart() {
        super.onStart();
        presenter.startPresenting();
    }


    @Override
    public void onStop() {
        presenter.startPresenting();
        super.onStop();
    }
}
