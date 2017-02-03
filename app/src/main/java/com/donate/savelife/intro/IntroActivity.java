package com.donate.savelife.intro;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Toast;

import com.donate.savelife.R;
import com.donate.savelife.core.analytics.Analytics;
import com.donate.savelife.core.user.data.model.User;
import com.donate.savelife.core.utils.GsonService;
import com.donate.savelife.core.utils.SharedPreferenceService;
import com.donate.savelife.firebase.Dependencies;
import com.donate.savelife.login.LoginFragment;

import me.relex.circleindicator.CircleIndicator;


/**
 * Created by ravi on 24/12/16.
 */

public class IntroActivity extends AppCompatActivity implements ViewPager.OnPageChangeListener {
    private ViewPager viewPager;
    private CircleIndicator indicator;
    private View skip;
    private Analytics analytics;
    private GsonService gsonService;
    private SharedPreferenceService sharedPreference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.indicator_view_pager_layout);
        analytics = Dependencies.INSTANCE.getAnalytics();
        gsonService = Dependencies.INSTANCE.getGsonService();
        sharedPreference = Dependencies.INSTANCE.getPreference();
        initControls();
        initPager();
    }
    
    private void initControls(){
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        indicator = (CircleIndicator) findViewById(R.id.radius_indicator);
        skip = findViewById(R.id.skip);
        skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewPager.setCurrentItem(2, true);
                User user = gsonService.toUser(sharedPreference.getLoginUserPreference());
                Bundle skipToLoginBundle = new Bundle();

                skipToLoginBundle.putInt(Analytics.PARAM_INTRO_SCREEN, viewPager.getCurrentItem());
                skipToLoginBundle.putString(Analytics.PARAM_EVENT_NAME, Analytics.PARAM_SKIP_INTRO);
                analytics.trackEventOnClick(skipToLoginBundle);
            }
        });
        
    }

    private void initPager(){
        viewPager.setOffscreenPageLimit(3);
        viewPager.addOnPageChangeListener(this);
        viewPager.setAdapter(new IntroAdapter(getSupportFragmentManager(), this));
        viewPager.setPageTransformer(false, new IntroPageTransformer(this));
        indicator.setViewPager(viewPager);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            for (Fragment fragment : getSupportFragmentManager().getFragments()) {
                fragment.onActivityResult(requestCode, resultCode, data);
            }
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        skip.setVisibility(position == 2 ? View.GONE : View.VISIBLE);
        switch (position){
            case 0:
                analytics.trackScreen(this, "Intro screen 1", null);
                break;

            case 1:
                analytics.trackScreen(this, "Intro screen 2", null);
                break;

            case 2:
                analytics.trackScreen(this, LoginFragment.TAG, null);
                break;
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            Toast.makeText(this, getString(R.string.str_complete_on_boarding), Toast.LENGTH_SHORT).show();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
