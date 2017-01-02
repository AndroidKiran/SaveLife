package com.donate.savelife.intro;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Toast;

import com.donate.savelife.R;

import me.relex.circleindicator.CircleIndicator;


/**
 * Created by ravi on 24/12/16.
 */

public class IntroActivity extends AppCompatActivity implements ViewPager.OnPageChangeListener {
    private ViewPager viewPager;
    private CircleIndicator indicator;
    private View skip;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.indicator_view_pager_layout);
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
        for (Fragment fragment : getSupportFragmentManager().getFragments()) {
            if (fragment.isAdded()){
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
