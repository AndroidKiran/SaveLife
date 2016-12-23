package com.donate.savelife.home.view;

import android.content.Context;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.view.MenuItem;
import android.view.View;

import com.aurelhubert.ahbottomnavigation.AHBottomNavigation;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationAdapter;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationViewPager;
import com.donate.savelife.R;
import com.donate.savelife.component.BlurTransformation;
import com.donate.savelife.component.ViewPagerAdapter;
import com.donate.savelife.component.text.TextView;
import com.donate.savelife.core.home.displayer.HomeDisplayer;
import com.donate.savelife.core.user.data.model.User;
import com.novoda.notils.caster.Views;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.donate.savelife.R.id.title_container;

/**
 * Created by ravi on 09/09/16.
 */
public class HomeView extends CoordinatorLayout implements HomeDisplayer {


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
        titleContainer = Views.findById(this, title_container);
        bottomNavigation = Views.findById(this, R.id.bottom_navigation);
        bottomNavigation.setBehaviorTranslationEnabled(true);
        bottomNavigation.setTranslucentNavigationEnabled(true);
        bottomNavigation.manageFloatingActionButtonBehavior(fabButton);
        bottomNavigation.setAccentColor(ResourcesCompat.getColor(getResources(),R.color.material_login_background, null));
    }

    @Override
    public void setProfile(User user) {
        Picasso.with(getAppCompatActivity()).load(user.getPhotoUrl()).into(profileImage);
        Picasso.with(getAppCompatActivity()).load(user.getPhotoUrl()).transform(new BlurTransformation(getContext(), 3, 1)).into(profileBackdrop);
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
        viewPager.addOnPageChangeListener(onPageChangeListener);
        fabButton.setOnClickListener(onClickListener);
        titleContainer.setOnClickListener(onClickListener);
        bottomNavigation.setOnTabSelectedListener(onTabSelectedListener);
    }

    @Override
    public void detach(HomeInteractionListener homeInteractionListener) {
        viewPager.addOnPageChangeListener(null);
        fabButton.setOnClickListener(null);
        this.homeInteractionListener = null;
        titleContainer.setOnClickListener(null);
        bottomNavigation.setOnTabSelectedListener(null);
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


    final ViewPager.OnPageChangeListener onPageChangeListener = new ViewPager.OnPageChangeListener() {

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            bottomNavigation.setCurrentItem(position, true);
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };

    final AHBottomNavigation.OnTabSelectedListener onTabSelectedListener = new AHBottomNavigation.OnTabSelectedListener() {
        @Override
        public boolean onTabSelected(int position, boolean wasSelected) {
            viewPager.setCurrentItem(position, true);
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

    Toolbar.OnMenuItemClickListener onMenuItemClickListener = new Toolbar.OnMenuItemClickListener() {
        @Override
        public boolean onMenuItemClick(MenuItem item) {
            switch (item.getItemId()) {
                case R.id.invite:
                    homeInteractionListener.onInviteUsersClicked();
                    return true;
            }
            return false;
        }
    };

    OnClickListener onNavigationClickListener = new OnClickListener() {
        @Override
        public void onClick(View view) {
            homeInteractionListener.onAboutUsClicked();
        }
    };
}
