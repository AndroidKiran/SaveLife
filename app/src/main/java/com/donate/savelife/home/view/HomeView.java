package com.donate.savelife.home.view;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;
import android.view.View;

import com.aurelhubert.ahbottomnavigation.AHBottomNavigation;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationAdapter;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationViewPager;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.donate.savelife.R;
import com.donate.savelife.SaveLifeApplication;
import com.donate.savelife.apputils.ConnectivityReceiver;
import com.donate.savelife.apputils.Views;
import com.donate.savelife.component.BlurTransformation;
import com.donate.savelife.component.ViewPagerAdapter;
import com.donate.savelife.component.text.TextView;
import com.donate.savelife.core.home.displayer.HomeDisplayer;
import com.donate.savelife.core.user.data.model.User;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.donate.savelife.R.id.title_container;

/**
 * Created by ravi on 09/09/16.
 */
public class HomeView extends CoordinatorLayout implements HomeDisplayer, ConnectivityReceiver.ConnectivityReceiverListener {


    private final int[] tabColors;
    private AHBottomNavigationViewPager viewPager;
    private AppCompatActivity appCompatActivity;
    private ViewPagerAdapter viewPagerAdapter;
    private HomeInteractionListener homeInteractionListener;
    private FloatingActionButton fabButton;
    private CircleImageView profileImage;
    private TextView profileName;

    private AppCompatImageView profileBackdrop;
    private View titleContainer;
    private AHBottomNavigation bottomNavigation;
    private AHBottomNavigationAdapter navigationAdapter;


    public HomeView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setFitsSystemWindows(true);
        tabColors = getResources().getIntArray(R.array.tab_colors);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        View.inflate(getContext(), R.layout.merge_home_view, this);
        initControls();
    }

    private void initControls() {
        profileBackdrop = Views.findById(this, R.id.profile_backdrop);
        profileImage = Views.findById(this, R.id.profile_image);
        profileName = Views.findById(this, R.id.profile_name);
        viewPager = Views.findById(this, R.id.vp_home);
        fabButton = Views.findById(this, R.id.fab_button);
        fabButton.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(getContext(), R.color.material_red)));
        titleContainer = Views.findById(this, title_container);
        bottomNavigation = Views.findById(this, R.id.bottom_navigation);
        bottomNavigation.setBehaviorTranslationEnabled(true);
        bottomNavigation.setTranslucentNavigationEnabled(true);
        bottomNavigation.manageFloatingActionButtonBehavior(fabButton);
        bottomNavigation.setColored(true);
    }

    @Override
    public void setProfile(final User user) {
        Glide.with(getAppCompatActivity()).load(user.getPhotoUrl()).thumbnail(0.8f)
                .crossFade().fitCenter().
        diskCacheStrategy(DiskCacheStrategy.ALL).into(profileImage);
        Glide.with(getAppCompatActivity()).load(user.getPhotoUrl()).diskCacheStrategy(DiskCacheStrategy.ALL).bitmapTransform(new BlurTransformation(getContext(), 2, 2))
                .into(profileBackdrop);
        profileName.setText(user.getName());
    }

    @Override
    public void setUpViewPager() {
        navigationAdapter = new AHBottomNavigationAdapter(getAppCompatActivity(), R.menu.menu_bottom_navi);
        navigationAdapter.setupWithBottomNavigation(bottomNavigation, tabColors);
        viewPager.setAdapter(getViewPagerAdapter());
        viewPager.setOffscreenPageLimit(3);
    }

    @Override
    public void attach(HomeInteractionListener homeInteractionListener) {
        this.homeInteractionListener = homeInteractionListener;
        fabButton.setOnClickListener(onClickListener);
        titleContainer.setOnClickListener(onClickListener);
        bottomNavigation.setOnTabSelectedListener(onTabSelectedListener);
        SaveLifeApplication.getInstance().setConnectivityListener(this);
        checkConnection();
    }

    @Override
    public void detach(HomeInteractionListener homeInteractionListener) {
        this.homeInteractionListener = null;
        fabButton.setOnClickListener(null);
        titleContainer.setOnClickListener(null);
        bottomNavigation.setOnTabSelectedListener(null);
        SaveLifeApplication.getInstance().setConnectivityListener(null);
    }

    @Override
    public void onTabSelected(int position) {
        setTheme(position);
        viewPager.setCurrentItem(position, true);
    }


    final OnClickListener onClickListener = new OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.fab_button:
                    homeInteractionListener.onFabBtnClicked();
                    break;

                case R.id.title_container:
                    homeInteractionListener.onProfileClicked();
                    break;
            }
        }
    };


    final AHBottomNavigation.OnTabSelectedListener onTabSelectedListener = new AHBottomNavigation.OnTabSelectedListener() {
        @Override
        public boolean onTabSelected(int position, boolean wasSelected) {
            homeInteractionListener.onTabSelected(position);
            return true;
        }
    };




    public AppCompatActivity getAppCompatActivity() {
        return appCompatActivity;
    }

    public void setAppCompatActivity(AppCompatActivity appCompatActivity) {
        this.appCompatActivity = appCompatActivity;
    }

    public ViewPagerAdapter getViewPagerAdapter() {
        return viewPagerAdapter;
    }

    public void setViewPagerAdapter(ViewPagerAdapter viewPagerAdapter) {
        this.viewPagerAdapter = viewPagerAdapter;
    }

    private void setTheme(int position){
        int color = R.color.material_red;
        switch (position){
            case 1:
                color = R.color.material_gold;
                break;

            case 2:
                color = R.color.material_green;
                break;

        }
        fabButton.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(getContext(), color)));
    }

    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        showSnack();
    }

    private void checkConnection() {
        boolean isConnected = ConnectivityReceiver.isConnected();
        if (!isConnected){
            showSnack();
        }
    }

    private void showSnack() {
            String message = "Sorry! Not connected to internet";
            int color = Color.WHITE;
            Snackbar snackbar = Snackbar
                    .make(bottomNavigation, message, Snackbar.LENGTH_LONG);

            View sbView = snackbar.getView();
            AppCompatTextView textView = (AppCompatTextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
            textView.setTextColor(color);
            snackbar.show();
    }
}
