package com.donate.savelife;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.donate.savelife.core.main.presenter.MainPresenter;
import com.donate.savelife.firebase.Dependencies;
import com.donate.savelife.navigation.AndroidNavigator;

public class MainActivity extends AppCompatActivity {

    private MainPresenter mainPresenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainPresenter = new MainPresenter(new AndroidNavigator(this),
                Dependencies.INSTANCE.getPreference(),
                Dependencies.INSTANCE.getGsonService());

    }

    @Override
    protected void onStart() {
        super.onStart();
        mainPresenter.startPresenting();
    }

    @Override
    protected void onStop() {
        mainPresenter.stopPresenting();
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
