package com.donate.savelife.user.view;

import android.content.Context;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;

import com.donate.savelife.R;
import com.donate.savelife.apputils.DialogUtils;
import com.donate.savelife.apputils.Utils;
import com.donate.savelife.component.BlurTransformation;
import com.donate.savelife.component.materialcomponent.MaterialProgressDialog;
import com.donate.savelife.component.text.TextView;
import com.donate.savelife.core.country.model.Country;
import com.donate.savelife.core.user.data.model.User;
import com.donate.savelife.core.user.displayer.CompleteProfileDisplayer;
import com.donate.savelife.country.CountriesDialog;
import com.novoda.notils.caster.Views;
import com.squareup.picasso.Picasso;

import fr.ganfra.materialspinner.MaterialSpinner;

import static com.donate.savelife.R.id.blood_group_spinner;
import static com.donate.savelife.R.id.city;

/**
 * Created by ravi on 19/11/16.
 */

public class CompleteProfileView extends CoordinatorLayout implements CompleteProfileDisplayer {


    private final String[] BLOOD_GROUP;
    private Toolbar toolbar;
    private TextInputLayout mobileTextInputLayout;
    private EditText mobileNumEditText;
    private TextInputLayout cityTextInputLayout;
    private EditText cityEditText;
    private TextView errMobileNumber;
    private TextView errCity;
    private MaterialSpinner bloodGroupSpinner;
    private TextView errBloodGroup;
    private AppCompatButton btnComplete;
    private OnCompleteListener onCompleteListener;
    private AppCompatImageView profilePic;
    private User user;
    private TextInputLayout mobileExtTextInputLayout;
    private EditText mobileExtNumEditText;
    private AppCompatActivity activity;
    private MaterialProgressDialog materialProgressDialog;
    private CountriesDialog countriesDialog;
    private Country country;

    public CompleteProfileView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setFitsSystemWindows(true);
        BLOOD_GROUP = getResources().getStringArray(R.array.blood_groups);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        View.inflate(getContext(), R.layout.merge_complete_profile_view, this);
        setToolbar();
        initControls();
        setBloodGroupAdapter();
    }

    public void setActivity(AppCompatActivity activity){
        this.activity = activity;
    }

    void setToolbar() {
        toolbar = Views.findById(this, R.id.toolbar);
        toolbar.setTitle(R.string.str_complete_profile);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
    }

    void initControls() {
        mobileExtTextInputLayout = Views.findById(this, R.id.country);
        mobileExtNumEditText = mobileExtTextInputLayout.getEditText();

        mobileTextInputLayout = Views.findById(this, R.id.mobile_num);
        mobileNumEditText = mobileTextInputLayout.getEditText();
        errMobileNumber = Views.findById(this, R.id.err_country);

        cityTextInputLayout = Views.findById(this, city);
        cityEditText = cityTextInputLayout.getEditText();
        errCity = Views.findById(this, R.id.err_city);

        bloodGroupSpinner = Views.findById(this, blood_group_spinner);
        bloodGroupSpinner.setSelection(0);
        errBloodGroup = Views.findById(this, R.id.err_blood_group);

        btnComplete = Views.findById(this, R.id.btn_complete);
        profilePic = Views.findById(this, R.id.profile_pic);

    }

    void setBloodGroupAdapter(){
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, BLOOD_GROUP);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        bloodGroupSpinner.setAdapter(adapter);
    }

    boolean validateForm(){
        boolean valid = true;

        String selectedBloodGroup = (String) bloodGroupSpinner.getSelectedItem();
        if (selectedBloodGroup.equals(getResources().getString(R.string.str_blood_group))){
            errBloodGroup.setVisibility(VISIBLE);
            valid = false;
        } else {
            errBloodGroup.setVisibility(GONE);
            user.setBloodGroup(selectedBloodGroup);
        }

        String mobileNum = mobileNumEditText.getText().toString();
        String mobileExt = mobileExtNumEditText.getText().toString();
        if (TextUtils.isEmpty(mobileNum) || TextUtils.isEmpty(mobileExt)){
            errMobileNumber.setVisibility(VISIBLE);
            valid = false;
        } else {
            if (!Utils.isValidMobile(mobileNum, mobileExt)){
                errMobileNumber.setVisibility(VISIBLE);
                valid = false;
            } else {
                errMobileNumber.setVisibility(GONE);
                user.setMobileNum(mobileNum);
                user.setCountry(mobileExt);
            }
        }

        String city = cityEditText.getText().toString();
        if (TextUtils.isEmpty(city)){
            errCity.setVisibility(VISIBLE);
            valid = false;
        } else {
            errCity.setVisibility(GONE);
            user.setCity(city);
        }
        return valid;
    }



    @Override
    public void attach(OnCompleteListener onCompleteListener) {
        this.onCompleteListener = onCompleteListener;
        mobileNumEditText.addTextChangedListener(textWatcher);
        cityEditText.addTextChangedListener(textWatcher);
        btnComplete.setOnClickListener(onClickListener);
        mobileExtNumEditText.setOnClickListener(onClickListener);
        mobileNumEditText.requestFocus();
        mobileExtNumEditText.setOnFocusChangeListener(onFocusChangeListener);
        mobileExtNumEditText.setKeyListener(null);
        toolbar.setNavigationOnClickListener(onNavigationClicklistener);
    }

    @Override
    public void detach(OnCompleteListener onCompleteListener) {
        this.onCompleteListener = onCompleteListener;
        mobileNumEditText.addTextChangedListener(null);
        cityEditText.addTextChangedListener(null);
        btnComplete.setOnClickListener(null);
        mobileExtNumEditText.setOnClickListener(null);
        mobileExtNumEditText.setOnFocusChangeListener(null);
        toolbar.setNavigationOnClickListener(null);
    }

    @Override
    public void display(User user) {
        this.user = user;
        String url = user.getPhotoUrl();
        if (!TextUtils.isEmpty(url)){
            Picasso.with(getContext())
                    .load(url)
                    .transform(new BlurTransformation(getContext(), 1, 1))
                    .into(profilePic);
        }

        String name = user.getName();
        if (!TextUtils.isEmpty(name)){
            toolbar.setTitle(name);
        }

        String bloodGroup = user.getBloodGroup();
        if (!TextUtils.isEmpty(bloodGroup)){
            for(int i = 0; i < BLOOD_GROUP.length; i++){
                if (BLOOD_GROUP[i].equals(bloodGroup)){
                    bloodGroupSpinner.setSelection(i + 1);
                }
            }
        }

        String countryCode = user.getCountry();
        if (!TextUtils.isEmpty(countryCode)){
            mobileExtNumEditText.setText(countryCode);
        }

        String mobileNum = user.getMobileNum();
        if (!TextUtils.isEmpty(mobileNum)){
            mobileNumEditText.setText(mobileNum);
        }

        String city = user.getCity();
        if (!TextUtils.isEmpty(city)){
            cityEditText.setText(city);
        }

    }

    @Override
    public void displayCountry(Country country) {
        dismissCountryDialog();
        this.country = country;
        mobileExtNumEditText.setText(country.getIsoCode());
    }

    @Override
    public void showProgress() {
        materialProgressDialog = new MaterialProgressDialog(activity);
        DialogUtils.showMaterialProgressDialog(materialProgressDialog, activity.getString(R.string.str_update_profile), activity.getString(R.string.str_progress_wait), activity);
    }

    @Override
    public void dismissProgress() {
        if (materialProgressDialog != null) {
            materialProgressDialog.dismiss();
        }
    }

    @Override
    public void showCountryDialog() {
        countriesDialog = CountriesDialog.newInstance(activity.getSupportFragmentManager());
    }

    @Override
    public void dismissCountryDialog() {
        if (countriesDialog != null){
            countriesDialog.dismiss();
        }
    }


    OnClickListener onClickListener = new OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.btn_complete:
                    if (validateForm()){
                        onCompleteListener.onComplete(user);
                    }
                    break;

                case R.id.country_et:
                    showCountryDialog();
                    break;
            }
        }
    };


    OnClickListener onNavigationClicklistener = new OnClickListener() {
        @Override
        public void onClick(View view) {
            onCompleteListener.onNavigateClick();
        }
    };



    OnFocusChangeListener onFocusChangeListener = new OnFocusChangeListener() {
        @Override
        public void onFocusChange(View view, boolean b) {
            switch (view.getId()){
                case R.id.country_et:
                    if (b){
                        showCountryDialog();
                    }
                    break;
            }
        }
    };


    TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void afterTextChanged(Editable editable) {
            int length = editable.length();
            if (cityEditText.isFocused()){
                if (length > 0){
                    errCity.setVisibility(GONE);
                }
            }
        }
    };


}
