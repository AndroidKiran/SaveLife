package com.donate.savelife.user;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.donate.savelife.R;
import com.donate.savelife.apputils.UtilBundles;
import com.donate.savelife.core.user.displayer.HerosDisplayer;
import com.donate.savelife.core.user.presenter.HeroPresenter;
import com.donate.savelife.firebase.Dependencies;
import com.donate.savelife.navigation.AndroidNavigator;

/**
 * Created by ravi on 20/11/16.
 */

public class HerosFragment extends Fragment {

    public static final String TAG = UtilBundles.HEROS_SCREEN;
    private HeroPresenter presenter;

    public static HerosFragment newInstance(Bundle bundle) {
        HerosFragment instance = new HerosFragment();
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
        return inflater.inflate(R.layout.heros_view, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        HerosDisplayer herosDisplayer = (HerosDisplayer) view.findViewById(R.id.heros_view);
        presenter = new HeroPresenter(herosDisplayer, Dependencies.INSTANCE.getUserService(),
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
        super.onStop();
        presenter.startPresenting();
    }
}
