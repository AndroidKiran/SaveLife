package com.donate.savelife.login;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.donate.savelife.R;
import com.donate.savelife.core.login.displayer.LoginDisplayer;
import com.donate.savelife.core.login.presenter.LoginPresenter;
import com.donate.savelife.firebase.Dependencies;
import com.donate.savelife.login.view.LoginView;
import com.donate.savelife.navigation.AndroidLoginNavigator;
import com.donate.savelife.navigation.AndroidNavigator;

import static com.donate.savelife.apputils.UtilBundles.BACKGROUND_COLOR;
import static com.donate.savelife.apputils.UtilBundles.PAGE;


public class LoginFragment extends Fragment {

    private static final int RC_SIGN_IN = 42;

    private LoginPresenter presenter;
    private AndroidLoginNavigator navigator;
    private AppCompatActivity activity;
    private int backgroundColor;
    private int page;

    public static LoginFragment newInstance(int backgroundColor, int page) {
        LoginFragment frag = new LoginFragment();
        Bundle b = new Bundle();
        b.putInt(BACKGROUND_COLOR, backgroundColor);
        b.putInt(PAGE, page);
        frag.setArguments(b);
        return frag;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        activity = (AppCompatActivity) context;
    }

    @Override
    public void onDetach() {
        activity = null;
        super.onDetach();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!getArguments().containsKey(BACKGROUND_COLOR))
            throw new RuntimeException("Fragment must contain a \"" + BACKGROUND_COLOR + "\" argument!");
        backgroundColor = getArguments().getInt(BACKGROUND_COLOR);

        if (!getArguments().containsKey(PAGE))
            throw new RuntimeException("Fragment must contain a \"" + PAGE + "\" argument!");
        page = getArguments().getInt(PAGE);

        LoginGoogleApiClient loginGoogleApiClient = new LoginGoogleApiClient(activity);
        loginGoogleApiClient.setupGoogleApiClient();
        navigator = new AndroidLoginNavigator(activity, loginGoogleApiClient, new AndroidNavigator(activity));
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.login_view, container, false);
        view.setTag(page);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        LoginView loginView = (LoginView) view.findViewById(R.id.login_view);
        loginView.setAppCompatActivity(activity);

        presenter = new LoginPresenter(Dependencies.INSTANCE.getLoginService(),
                Dependencies.INSTANCE.getUserService(),
                (LoginDisplayer) loginView,
                navigator,
                Dependencies.INSTANCE.getErrorLogger(),
                Dependencies.INSTANCE.getAnalytics(),
                Dependencies.INSTANCE.getPreference(),
                Dependencies.INSTANCE.getGsonService());

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (!navigator.onActivityResult(requestCode, resultCode, data)) {
            super.onActivityResult(requestCode, resultCode, data);
        }
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
}
