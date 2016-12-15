package com.donate.savelife.home.view;

import android.content.Context;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.view.MenuItem;
import android.view.View;

import com.donate.savelife.R;
import com.donate.savelife.apputils.Utils;
import com.donate.savelife.component.BlurTransformation;
import com.donate.savelife.component.HomeTabLayout;
import com.donate.savelife.component.ViewPagerAdapter;
import com.donate.savelife.component.text.TextView;
import com.donate.savelife.core.home.displayer.HomeDisplayer;
import com.donate.savelife.core.user.data.model.User;
import com.novoda.notils.caster.Views;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.donate.savelife.R.id.title_container;

/**
 * Created by ravi on 09/09/16.
 */
public class HomeView extends CoordinatorLayout implements HomeDisplayer {

    private static final int PERCENTAGE_TO_ANIMATE_AVATAR = 20;
    private boolean mIsAvatarShown = true;

    private HomeTabLayout tabLayout;
    private ViewPager viewPager;
    private AppCompatActivity appCompatActivity;
    private ViewPagerAdapter viewPagerAdapter;
    private HomeInteractionListener homeInteractionListener;
    private FloatingActionButton fabButton;
    private CircleImageView profileImage;
    private TextView profileName;
    private AppBarLayout appbarLayout;

    private Toolbar toolBar;
    private int maxScrollSize;
    private AppCompatImageView profileBackdrop;
    private TextView msgTitle;
    private View titleContainer;


    public HomeView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        View.inflate(getContext(), R.layout.merge_home_view, this);
        setToolbar();
        initControls();
    }


    private void setToolbar() {
        toolBar = Views.findById(this, R.id.toolbar);
        toolBar.inflateMenu(R.menu.home_menu);
        toolBar.setNavigationIcon(R.drawable.ic_error_outline_white_24dp);
    }

    private void initControls() {
        profileBackdrop = Views.findById(this, R.id.profile_backdrop);
        profileImage = Views.findById(this, R.id.profile_image);
        profileName = Views.findById(this, R.id.profile_name);
        appbarLayout = Views.findById(this, R.id.appbar);
        tabLayout = Views.findById(this, R.id.tab_layout);
        viewPager = Views.findById(this, R.id.vp_home);
        fabButton = Views.findById(this, R.id.fab_button);
        msgTitle = Views.findById(this, R.id.msg_title);
        titleContainer = Views.findById(this, title_container);
        maxScrollSize = appbarLayout.getTotalScrollRange();

        if (Utils.hasLollipop()) {
            Utils.setMargins(toolBar, 0, -48, 0, 0);
        }

    }

    @Override
    public void setProfile(User user) {
        Picasso.with(getAppCompatActivity()).load(user.getPhotoUrl()).into(profileImage);
        Picasso.with(getAppCompatActivity()).load(user.getPhotoUrl()).transform(new BlurTransformation(getContext(), 3, 1)).into(profileBackdrop);
        profileName.setText(user.getName());
    }

    @Override
    public void setUpViewPager() {
        addTabs();
        viewPager.setAdapter(getViewPagerAdapter());
        viewPager.setOffscreenPageLimit(2);

    }

    private void addTabs() {
        List<String> titles = getViewPagerAdapter().getFragmentTitles();
        for (String title : titles) {
            tabLayout.addTab(title, title);
        }
        tabLayout.setupWithViewPager(viewPager);
    }

    @Override
    public void attach(HomeInteractionListener homeInteractionListener) {
        this.homeInteractionListener = homeInteractionListener;
        viewPager.addOnPageChangeListener(onPageChangeListener);
        fabButton.setOnClickListener(onClickListener);
        appbarLayout.addOnOffsetChangedListener(onOffsetChangedListener);
        titleContainer.setOnClickListener(onClickListener);
        toolBar.setOnMenuItemClickListener(onMenuItemClickListener);
        toolBar.setNavigationOnClickListener(onNavigationClickListener);
    }

    @Override
    public void detach(HomeInteractionListener homeInteractionListener) {
        viewPager.addOnPageChangeListener(null);
        fabButton.setOnClickListener(null);
        this.homeInteractionListener = null;
        appbarLayout.addOnOffsetChangedListener(null);
        titleContainer.setOnClickListener(null);
        toolBar.setOnMenuItemClickListener(null);
        toolBar.setNavigationOnClickListener(null);
    }


    final OnClickListener onClickListener = new OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.fab_button:
                    homeInteractionListener.onFabBtnClicked(tabLayout.getSelectedTabPosition());
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
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };

    AppBarLayout.OnOffsetChangedListener onOffsetChangedListener = new AppBarLayout.OnOffsetChangedListener() {
        @Override
        public void onOffsetChanged(AppBarLayout appBarLayout, int i) {
            if (maxScrollSize == 0)
                maxScrollSize = appBarLayout.getTotalScrollRange();

            int percentage = (Math.abs(i)) * 100 / maxScrollSize;

            if (percentage >= PERCENTAGE_TO_ANIMATE_AVATAR && mIsAvatarShown) {
                mIsAvatarShown = false;
                profileImage.animate().scaleY(0).scaleX(0).setDuration(200).start();
            }

            if (percentage <= PERCENTAGE_TO_ANIMATE_AVATAR && !mIsAvatarShown) {
                mIsAvatarShown = true;

                profileImage.animate()
                        .scaleY(1).scaleX(1)
                        .start();
            }
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
