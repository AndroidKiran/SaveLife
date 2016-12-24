package com.donate.savelife.intro;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.donate.savelife.R;

import me.relex.circleindicator.CircleIndicator;

/**
 * Created by ravi on 24/12/16.
 */

public class IntroActivity extends AppCompatActivity{
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.indicator_view_pager_layout);

        mViewPager = (ViewPager) findViewById(R.id.viewpager);

        CircleIndicator indicator = (CircleIndicator) findViewById(R.id.indicator);

        // Set an Adapter on the ViewPager
        mViewPager.setAdapter(new IntroAdapter(getSupportFragmentManager()));

        // Set a PageTransformer
        mViewPager.setPageTransformer(false, new IntroPageTransformer());

        indicator.setViewPager(mViewPager);

    }

}
