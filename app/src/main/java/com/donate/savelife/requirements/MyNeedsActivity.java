package com.donate.savelife.requirements;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.donate.savelife.R;
import com.donate.savelife.apputils.UtilBundles;
import com.donate.savelife.core.requirement.displayer.NeedsDisplayer;
import com.donate.savelife.core.requirement.presenter.MyNeedsPresenter;
import com.donate.savelife.core.utils.GsonService;
import com.donate.savelife.core.utils.SharedPreferenceService;
import com.donate.savelife.firebase.Dependencies;
import com.donate.savelife.navigation.AndroidNavigator;

/**
 * Created by ravi on 20/11/16.
 */

public class MyNeedsActivity extends AppCompatActivity {

    public static final String TAG = UtilBundles.NEEDS_SCREEN;

    private MyNeedsPresenter presenter;
    private SharedPreferenceService preference;
    private GsonService gsonService;
    private AndroidNavigator androidNavigator;

    public static Intent createIntentFor(Context context) {
        Intent intent = new Intent(context, MyNeedsActivity.class);
        return intent;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.needs_view);
        preference = Dependencies.INSTANCE.getPreference();
        gsonService = Dependencies.INSTANCE.getGsonService();
        androidNavigator = new AndroidNavigator(this);

        NeedsDisplayer needsDisplayer = (NeedsDisplayer) findViewById(R.id.needs_view);
        presenter = new MyNeedsPresenter(
                needsDisplayer,
                Dependencies.INSTANCE.getNeedService(),
                Dependencies.INSTANCE.getErrorLogger(),
                Dependencies.INSTANCE.getAnalytics(),
                androidNavigator,
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


//    @Override
//    public boolean onKeyDown(int keyCode, KeyEvent event) {
//        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
//            androidNavigator.toParent();
//            return true;
//        }
//        return false;
//    }
}
