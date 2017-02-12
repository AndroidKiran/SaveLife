package com.donate.savelife.intro;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;

import com.donate.savelife.R;
import com.donate.savelife.login.LoginFragment;

/**
 * Created by ravi on 24/12/16.
 */

public class IntroAdapter extends FragmentPagerAdapter {

    private static final int NUM_OF_FRAGMENT = 3;
    private final Context context;

    public IntroAdapter(FragmentManager fm, Context context) {
        super(fm);
        this.context = context;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return IntroFragment.newInstance(ContextCompat.getColor(context,R.color.material_red), position); // blue

            case 1:
                return IntroFragment.newInstance(ContextCompat.getColor(context,R.color.material_gold), position); // green

            default:
                return LoginFragment.newInstance(ContextCompat.getColor(context,R.color.primary), position);
        }
    }

    @Override
    public int getCount() {
        return NUM_OF_FRAGMENT;
    }

}