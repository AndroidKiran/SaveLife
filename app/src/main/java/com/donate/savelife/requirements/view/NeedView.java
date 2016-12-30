package com.donate.savelife.requirements.view;

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
import com.donate.savelife.apputils.Views;
import com.donate.savelife.component.materialcomponent.MaterialProgressDialog;
import com.donate.savelife.component.text.TextView;
import com.donate.savelife.core.country.model.Country;
import com.donate.savelife.core.requirement.displayer.NeedDisplayer;
import com.donate.savelife.core.requirement.model.Need;
import com.donate.savelife.core.user.data.model.User;
import com.donate.savelife.country.CountriesDialog;

import fr.ganfra.materialspinner.MaterialSpinner;


/**
 * Created by ravi on 19/11/16.
 */

public class NeedView extends CoordinatorLayout implements NeedDisplayer {

    private final String[] BLOOD_GROUP;
    private Toolbar toolbar;
    private TextInputLayout cityTextInputLayout;
    private EditText cityEditText;
    private TextView errCountry;
    private TextView errCity;
    private MaterialSpinner bloodGroupSpinner;
    private TextView errBloodGroup;
    private AppCompatButton btnComplete;
    private AppCompatImageView profilePic;
    private TextInputLayout countryTextInputLayout;
    private EditText countryEditText;
    private AppCompatActivity activity;
    private MaterialProgressDialog materialProgressDialog;
    private CountriesDialog countriesDialog;
    private Country country;
    private OnNeedInteractionListener onNeedInteractionListener;
    private Need need;
    private TextInputLayout addressTextInputLayout;
    private EditText adderssEditText;
    private TextView errAddress;
    private User user;

    public NeedView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setFitsSystemWindows(true);
        BLOOD_GROUP = getResources().getStringArray(R.array.blood_groups);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        View.inflate(getContext(), R.layout.merge_need_view, this);
        setToolbar();
        initControls();
        setBloodGroupAdapter();
    }

    public void setActivity(AppCompatActivity activity){
        this.activity = activity;
    }

    void setToolbar() {
        toolbar = Views.findById(this, R.id.toolbar);
        toolbar.setTitle(R.string.str_request);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
    }

    void initControls() {

        cityTextInputLayout = Views.findById(this, R.id.city);
        cityEditText = cityTextInputLayout.getEditText();
        errCity = Views.findById(this, R.id.err_city);

        bloodGroupSpinner = Views.findById(this, R.id.blood_group_spinner);
        bloodGroupSpinner.setSelection(0);
        errBloodGroup = Views.findById(this, R.id.err_blood_group);

        addressTextInputLayout = Views.findById(this, R.id.address);
        adderssEditText = addressTextInputLayout.getEditText();
        errAddress = Views.findById(this, R.id.err_address);

        countryTextInputLayout = Views.findById(this, R.id.country);
        countryEditText = countryTextInputLayout.getEditText();
        errCountry = Views.findById(this, R.id.err_country);

        btnComplete = Views.findById(this, R.id.btn_request);
        profilePic = Views.findById(this, R.id.profile_pic);

    }

    void setBloodGroupAdapter(){
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, BLOOD_GROUP);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        bloodGroupSpinner.setAdapter(adapter);
    }

    boolean validateForm(){
        boolean valid = true;
        need = new Need();
        need.setUserID(user.getId());

        String selectedBloodGroup = (String) bloodGroupSpinner.getSelectedItem();
        if (selectedBloodGroup.equals(getResources().getString(R.string.str_blood_group))){
            errBloodGroup.setVisibility(VISIBLE);
            valid = false;
        } else {
            errBloodGroup.setVisibility(GONE);
            need.setBloodGroup(selectedBloodGroup);
        }

        String address = adderssEditText.getText().toString();
        if (TextUtils.isEmpty(address)){
            errAddress.setVisibility(VISIBLE);
            valid = false;
        } else {
            errAddress.setVisibility(GONE);
            need.setAddress(address);
        }

        String city = cityEditText.getText().toString();
        if (TextUtils.isEmpty(city)){
            errCity.setVisibility(VISIBLE);
            valid = false;
        } else {
            errCity.setVisibility(GONE);
            need.setCity(city);
        }

        String country = countryEditText.getText().toString();
        if (TextUtils.isEmpty(country)){
            errCountry.setVisibility(VISIBLE);
            valid = false;
        } else {
            errCountry.setVisibility(GONE);
            need.setCountry(this.country.getIsoCode());
        }

        need.setTimeStamp(System.currentTimeMillis());

        return valid;
    }



    @Override
    public void attach(OnNeedInteractionListener onNeedInteractionListener) {
        this.onNeedInteractionListener = onNeedInteractionListener;
        cityEditText.addTextChangedListener(textWatcher);
        btnComplete.setOnClickListener(onClickListener);
        countryEditText.setOnClickListener(onClickListener);
        countryEditText.setOnFocusChangeListener(onFocusChangeListener);
        countryEditText.setKeyListener(null);
        adderssEditText.addTextChangedListener(textWatcher);
        toolbar.setNavigationOnClickListener(onNavigationClickListener);
    }

    @Override
    public void detach(OnNeedInteractionListener onNeedInteractionListener) {
        this.onNeedInteractionListener = onNeedInteractionListener;
        cityEditText.addTextChangedListener(null);
        btnComplete.setOnClickListener(null);
        countryEditText.setOnClickListener(null);
        countryEditText.setOnFocusChangeListener(null);
        adderssEditText.addTextChangedListener(null);
        toolbar.setNavigationOnClickListener(null);
    }


    @Override
    public void displayCountry(Country country) {
        dismissCountryDialog();
        this.country = country;
        countryEditText.setText(country.getCountryName(getContext()));
    }

    @Override
    public void showProgress() {
        materialProgressDialog = new MaterialProgressDialog(activity);
        DialogUtils.showMaterialProgressDialog(materialProgressDialog, activity.getString(R.string.str_post_request), activity.getString(R.string.str_progress_wait), activity);
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

    @Override
    public void displayUser(User user) {
        this.user = user;
    }


    OnClickListener onClickListener = new OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.btn_request:
                    if (validateForm() && need != null){
                        onNeedInteractionListener.onNeedPost(need);
                    }
                    break;

                case R.id.country_et:
                    showCountryDialog();
                    break;
            }
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

            if (adderssEditText.isFocused()){
                if (length > 10){
                    errAddress.setVisibility(GONE);
                }
            }
        }
    };

    OnClickListener onNavigationClickListener = new OnClickListener() {
        @Override
        public void onClick(View view) {
            onNeedInteractionListener.onNavigateClick();
        }
    };


}
