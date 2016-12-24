package com.donate.savelife.preferences.view;

import android.content.Context;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

import com.donate.savelife.R;
import com.donate.savelife.core.preferences.displayer.PreferenceDisplayer;
import com.novoda.notils.caster.Views;


/**
 * Created by ravi on 24/12/16.
 */

public class PreferenceView extends LinearLayout implements PreferenceDisplayer {

    private PreferenceInteractionListener preferenceInteractionListener;
    private View notificationSetting;
    private AppCompatTextView notificationStatus;
    private View aboutUs;
    private View invite;

    public PreferenceView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setOrientation(VERTICAL);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        View.inflate(getContext(), R.layout.merge_preference_view, this);
        initControls();
    }

    private void initControls() {
        notificationSetting = Views.findById(this, R.id.notification_setting);
        notificationStatus = Views.findById(this, R.id.notification_status);
        aboutUs = Views.findById(this, R.id.about_us);
        invite = Views.findById(this, R.id.invite);
    }

    @Override
    public void attach(PreferenceInteractionListener preferenceInteractionListener) {
        this.preferenceInteractionListener = preferenceInteractionListener;
        notificationSetting.setOnClickListener(onClickListener);
        aboutUs.setOnClickListener(onClickListener);
        invite.setOnClickListener(onClickListener);
    }

    @Override
    public void detach(PreferenceInteractionListener preferenceInteractionListener) {
        this.preferenceInteractionListener = preferenceInteractionListener;
        notificationSetting.setOnClickListener(null);
        aboutUs.setOnClickListener(null);
        invite.setOnClickListener(null);
    }

    final OnClickListener onClickListener = new OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()){

                case R.id.notification_setting:
                    preferenceInteractionListener.onNotificationModifyClicked();
                    break;

                case R.id.about_us:
                    preferenceInteractionListener.onAboutClicked();
                    break;

                case R.id.invite:
                    preferenceInteractionListener.onShareClicked();
                    break;
            }
        }
    };
}
