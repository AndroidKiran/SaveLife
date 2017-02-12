package com.donate.savelife.user.view;

import android.content.Context;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.MenuItem;
import android.view.View;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.donate.savelife.R;
import com.donate.savelife.apputils.Views;
import com.donate.savelife.component.BlurTransformation;
import com.donate.savelife.component.MultiStateView;
import com.donate.savelife.component.text.TextView;
import com.donate.savelife.core.user.data.model.User;
import com.donate.savelife.core.user.displayer.ProfileDisplayer;

import java.util.Locale;


/**
 * Created by ravi on 05/12/16.
 */

public class ProfileView extends CoordinatorLayout implements ProfileDisplayer {

    private Toolbar toolbar;
    private AppCompatImageView profileBackDrop;
    private TextView bloodGroupText;
    private TextView mobileNumText;
    private TextView addressText;
    private OnProfileInteractionListener profileInteractionListener;
    private User user;
    private FloatingActionButton fabButton;
    private TextView msg1;
    private TextView msg2;
    private TextView saveCountText;
    private View honorCardView;
    private MultiStateView multiView;


    public ProfileView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setFitsSystemWindows(true);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        View.inflate(getContext(), R.layout.merge_profile_view, this);
        setToolbar();
        initControl();
    }


    private void setToolbar() {
        toolbar = Views.findById(this, R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
        toolbar.inflateMenu(R.menu.menu_profile);
        toggleMenu(false);
    }

    private void initControl(){
        profileBackDrop = Views.findById(this, R.id.profile_pic);
        bloodGroupText = Views.findById(this, R.id.blood_group);
        mobileNumText = Views.findById(this, R.id.mobile_number);
        addressText = Views.findById(this, R.id.address);
        fabButton = Views.findById(this, R.id.fab_button);
        msg1 = Views.findById(this, R.id.msg1);
        msg2 = Views.findById(this, R.id.msg2);
        saveCountText = Views.findById(this, R.id.save_count);
        honorCardView = Views.findById(this, R.id.honor_cardview);
        multiView = Views.findById(this, R.id.multi_view);

    }

    @Override
    public void attach(OnProfileInteractionListener profileInteractionListener) {
        this.profileInteractionListener = profileInteractionListener;
        fabButton.setOnClickListener(onClickListener);
        toolbar.setNavigationOnClickListener(onNavigationClickListener);
        toolbar.setOnMenuItemClickListener(onMenuItemClickListener);
    }

    @Override
    public void detach(OnProfileInteractionListener profileInteractionListener) {
        this.profileInteractionListener = profileInteractionListener;
        fabButton.setOnClickListener(null);
        toolbar.setNavigationOnClickListener(null);
        toolbar.setOnMenuItemClickListener(null);

    }

    @Override
    public void display(User user) {
        this.user = user;
        int saveCount = user.getLifeCount();
        if (saveCount == 0){
            honorCardView.setVisibility(GONE);
        } else {
            saveCountText.setText(String.valueOf(saveCount));
            msg2.setText(getResources().getQuantityString(R.plurals.num_of_lives,saveCount));
            honorCardView.setVisibility(VISIBLE);
        }

        final String url = user.getPhotoUrl();
        if (!TextUtils.isEmpty(url)){
            Glide.with(getContext())
                    .load(url)
                    .placeholder(R.drawable.blood_banner)
                    .centerCrop()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .bitmapTransform(new BlurTransformation(getContext(), 1, 1))
                    .into(profileBackDrop);
        }

        String name = user.getName();
        if (!TextUtils.isEmpty(name)){
            toolbar.setTitle(name);
        }

        String bloodGroup = user.getBloodGroup();
        if (!TextUtils.isEmpty(bloodGroup)){
           bloodGroupText.setText(String.format(getResources().getString(R.string.str_wat_blood_group), bloodGroup));
        }

        String mobileNum = user.getMobileNum();
        if (!TextUtils.isEmpty(mobileNum)){
            mobileNumText.setText(mobileNum);
        }

        String countryCode = user.getCountry();
        String city = user.getCity();
        if (!TextUtils.isEmpty(city)){
            addressText.setText(String.format(getResources().getString(R.string.str_wat_address), city, new Locale("", countryCode).getDisplayCountry()));
        }
        displayContent();
    }

    @Override
    public void displayHero(boolean alreadyHonored, boolean isOwner) {
        if (!alreadyHonored && !isOwner){
            fabButton.setVisibility(View.VISIBLE);
        } else {
            fabButton.setVisibility(View.GONE);
        }
    }

    @Override
    public void displayLoading() {
        multiView.setViewState(MultiStateView.VIEW_STATE_LOADING);
    }

    @Override
    public void displayContent() {
        multiView.setViewState(MultiStateView.VIEW_STATE_CONTENT);
    }

    @Override
    public void displayError() {
        multiView.setViewState(MultiStateView.VIEW_STATE_ERROR);
    }

    @Override
    public void displayEmpty() {
        multiView.setViewState(MultiStateView.VIEW_STATE_EMPTY);
    }

    @Override
    public void toggleMenu(boolean toggle) {
        MenuItem menuItem = toolbar.getMenu().findItem(R.id.action_edit);
        menuItem.setVisible(toggle);
    }


    OnClickListener onClickListener = new OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.fab_button:
                    profileInteractionListener.onHonorClick();
                    break;
            }
        }
    };

    OnClickListener onNavigationClickListener = new OnClickListener() {
        @Override
        public void onClick(View view) {
            profileInteractionListener.onNavigateClick();
        }
    };

    Toolbar.OnMenuItemClickListener onMenuItemClickListener = new Toolbar.OnMenuItemClickListener() {
        @Override
        public boolean onMenuItemClick(MenuItem item) {
            switch (item.getItemId()){
                case R.id.action_edit:
                    profileInteractionListener.onEditClick();
                    return true;
            }
            return false;
        }
    };
}
