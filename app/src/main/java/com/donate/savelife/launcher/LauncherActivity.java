package com.donate.savelife.launcher;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.donate.savelife.R;
import com.donate.savelife.SaveLifeApplication;
import com.donate.savelife.core.launcher.displayer.LauncherDisplayer;
import com.donate.savelife.core.launcher.presenter.LauncherPresenter;
import com.donate.savelife.firebase.Dependencies;
import com.donate.savelife.launcher.view.LauncherView;
import com.donate.savelife.navigation.AndroidNavigator;

public class LauncherActivity extends AppCompatActivity {

    private LauncherPresenter launcherPresenter;

    public static Intent createIntentFor(Context context) {
        Intent intent = new Intent(context, LauncherActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        return intent;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lauch_view);
        SaveLifeApplication.getInstance().setIndexIntent(getIntent());
        LauncherView launcherView = (LauncherView) findViewById(R.id.launch_view);
        launcherPresenter = new LauncherPresenter(new AndroidNavigator(this),
                Dependencies.INSTANCE.getPreference(),
                Dependencies.INSTANCE.getGsonService(),
                Dependencies.INSTANCE.getAppStatusService(),
                (LauncherDisplayer) launcherView,
                Float.parseFloat(SaveLifeApplication.APP_VERSION));
    }

    @Override
    protected void onStart() {
        super.onStart();
        launcherPresenter.startPresenting();
    }

    @Override
    protected void onStop() {
        launcherPresenter.stopPresenting();
        super.onStop();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == AndroidNavigator.FIRST_FLOW_RESPONSE_CODE && requestCode == AndroidNavigator.FIRST_FLOW_REQUEST_CODE){
//            mainPresenter.stopPresenting();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

}
