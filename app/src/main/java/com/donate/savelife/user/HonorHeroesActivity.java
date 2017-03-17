package com.donate.savelife.user;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;

import com.donate.savelife.R;
import com.donate.savelife.core.user.displayer.HonorHeroesDisplayer;
import com.donate.savelife.core.user.presenter.HonorHeroesPresenter;
import com.donate.savelife.core.utils.AppConstant;
import com.donate.savelife.firebase.Dependencies;
import com.donate.savelife.navigation.AndroidNavigator;

/**
 * Created by ravi on 26/02/17.
 */

public class HonorHeroesActivity extends AppCompatActivity{

    private HonorHeroesPresenter presenter;
    private Bundle mArgs;

    public static Intent createIntentFor(Context context, String needId) {
        Intent intent = new Intent(context, HonorHeroesActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString(AppConstant.NEED_EXTRA, needId);
        intent.putExtras(bundle);
        return intent;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.heroes_honor_view);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH, WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH);

        if (savedInstanceState == null){
            mArgs = getIntent().getExtras();
        } else {
            mArgs = savedInstanceState;
        }
        initControls();
    }


    private void initControls(){
        HonorHeroesDisplayer honorHeroesDisplayer = (HonorHeroesDisplayer) findViewById(R.id.heroes_honor_view);
        presenter = new HonorHeroesPresenter(Dependencies.INSTANCE.getHeroService(),Dependencies.INSTANCE.getChatService(), new AndroidNavigator(this),
                honorHeroesDisplayer, mArgs, Dependencies.INSTANCE.getAnalytics(), Dependencies.INSTANCE.getErrorLogger());
    }

    @Override
    protected void onStart() {
        super.onStart();
        presenter.startPresenting();
    }


    @Override
    protected void onStop() {
        presenter.stopPresenting();
        super.onStop();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putAll(mArgs);
        presenter.onSavedInstanceStatePresneter(outState);
        super.onSaveInstanceState(outState);
    }
}
