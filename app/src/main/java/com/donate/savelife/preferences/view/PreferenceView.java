package com.donate.savelife.preferences.view;

import android.content.Context;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.SwitchCompat;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.LinearLayout;

import com.donate.savelife.R;
import com.donate.savelife.SaveLifeApplication;
import com.donate.savelife.apputils.DialogUtils;
import com.donate.savelife.apputils.Views;
import com.donate.savelife.component.text.TextView;
import com.donate.savelife.core.preferences.displayer.PreferenceDisplayer;


/**
 * Created by ravi on 24/12/16.
 */

public class PreferenceView extends LinearLayout implements PreferenceDisplayer {

    private PreferenceInteractionListener preferenceInteractionListener;
    private SwitchCompat notificationSwitch;
    private AppCompatTextView notificationStatus;
    private View aboutUs;
    private View invite;
    private View rateUs;
    private View termsCondition;
    private AlertDialog aboutDialog;
    private TextView notificationCity;

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
        notificationSwitch = Views.findById(this, R.id.notification_switch);
        notificationStatus = Views.findById(this, R.id.notification_city);
        aboutUs = Views.findById(this, R.id.about_us);
        invite = Views.findById(this, R.id.invite);
        rateUs = Views.findById(this, R.id.play_store_rate);
        termsCondition = Views.findById(this, R.id.terms_conditions);
        notificationCity = Views.findById(this, R.id.notification_city);
    }

    @Override
    public void attach(PreferenceInteractionListener preferenceInteractionListener) {
        this.preferenceInteractionListener = preferenceInteractionListener;
        notificationSwitch.setOnCheckedChangeListener(onCheckedChangeListener);
        aboutUs.setOnClickListener(onClickListener);
        invite.setOnClickListener(onClickListener);
        rateUs.setOnClickListener(onClickListener);
        termsCondition.setOnClickListener(onClickListener);
    }

    @Override
    public void detach(PreferenceInteractionListener preferenceInteractionListener) {
        this.preferenceInteractionListener = preferenceInteractionListener;
        notificationSwitch.setOnCheckedChangeListener(null);
        aboutUs.setOnClickListener(null);
        invite.setOnClickListener(null);
        rateUs.setOnClickListener(null);
        termsCondition.setOnClickListener(null);
    }

    @Override
    public void showAboutUsDialog() {
        aboutDialog = DialogUtils.showDialog(getContext(), createAboutUsView());
        aboutDialog.show();
    }

    @Override
    public void dismissAboutUsDialog() {
        if (aboutDialog != null && aboutDialog.isShowing()) {
            aboutDialog.dismiss();
        }
    }

    @Override
    public void showTermsDialog() {
        View dialogView = LayoutInflater.from(getContext()).inflate(R.layout.dialog_terms_conditions, null);
        aboutDialog = DialogUtils.showDialog(getContext(), dialogView);
        aboutDialog.show();
    }

    @Override
    public void showNotificationCity(String city) {
        notificationCity.setText(city);
    }

    @Override
    public void showNotificationStatus(boolean status) {
        notificationSwitch.setChecked(status);
    }

    final OnClickListener onClickListener = new OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()){

                case R.id.about_us:
                    preferenceInteractionListener.onAboutClicked();
                    break;

                case R.id.invite:
                    preferenceInteractionListener.onShareClicked();
                    break;

                case R.id.play_store_rate:
                    preferenceInteractionListener.onRateClicked();
                    break;

                case R.id.terms_conditions:
                    preferenceInteractionListener.onTermsClicked();
                    break;
            }
        }
    };

    CompoundButton.OnCheckedChangeListener onCheckedChangeListener = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
            preferenceInteractionListener.onNotificationModifyClicked(b);
        }
    };

    private View createAboutUsView(){
        View dialogView = LayoutInflater.from(getContext()).inflate(R.layout.dialog_about_us, null);
        TextView versionTxt = (TextView) dialogView.findViewById(R.id.app_version);
        String versionString = getResources().getString(R.string.str_version) + SaveLifeApplication.APP_VERSION;
        versionTxt.setText(versionString);
        return dialogView;
    }
}
