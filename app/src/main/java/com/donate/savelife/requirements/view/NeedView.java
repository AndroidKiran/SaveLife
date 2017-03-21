package com.donate.savelife.requirements.view;

import android.content.Context;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
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
import com.donate.savelife.core.country.model.City;
import com.donate.savelife.core.requirement.displayer.NeedDisplayer;
import com.donate.savelife.core.requirement.model.Need;
import com.donate.savelife.core.user.data.model.User;
import com.donate.savelife.country.city.CityDialog;

import fr.ganfra.materialspinner.MaterialSpinner;


/**
 * Created by ravi on 19/11/16.
 */

public class NeedView extends CoordinatorLayout implements NeedDisplayer {

    private final String[] BLOOD_GROUP;
    private Toolbar toolbar;
    private EditText cityEditText;
    private TextView errCity;
    private MaterialSpinner bloodGroupSpinner;
    private TextView errBloodGroup;
    private AppCompatButton btnComplete;
    private AppCompatActivity activity;
    private MaterialProgressDialog materialProgressDialog;
    private OnNeedInteractionListener onNeedInteractionListener;
    private Need need;
    private TextInputLayout addressTextInputLayout;
    private EditText adderssEditText;
    private TextView errAddress;
    private User user;
    private View successLayout;
    private CityDialog chooseCityDialog;

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

        this.cityEditText = Views.findById(this, R.id.city_et);
        this.errCity = Views.findById(this, R.id.err_city);

        this.bloodGroupSpinner = Views.findById(this, R.id.blood_group_spinner);
        bloodGroupSpinner.setSelection(0);
        this.errBloodGroup = Views.findById(this, R.id.err_blood_group);

        this.addressTextInputLayout = Views.findById(this, R.id.address);
        this.adderssEditText = addressTextInputLayout.getEditText();
        this.errAddress = Views.findById(this, R.id.err_address);

        this.btnComplete = Views.findById(this, R.id.btn_request);
        this.successLayout = Views.findById(this, R.id.success_layout);

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
            need.setCountry("IN");
        }

        need.setTimeStamp(System.currentTimeMillis());
        return valid;
    }

    @Override
    public void attach(OnNeedInteractionListener onNeedInteractionListener) {
        this.onNeedInteractionListener = onNeedInteractionListener;
        btnComplete.setOnClickListener(onClickListener);
        cityEditText.setOnClickListener(onClickListener);
        cityEditText.setOnFocusChangeListener(onFocusChangeListener);
        cityEditText.setKeyListener(null);
        adderssEditText.addTextChangedListener(textWatcher);
        toolbar.setNavigationOnClickListener(onNavigationClickListener);
    }

    @Override
    public void detach(OnNeedInteractionListener onNeedInteractionListener) {
        this.onNeedInteractionListener = onNeedInteractionListener;
        btnComplete.setOnClickListener(null);
        cityEditText.setOnClickListener(null);
        cityEditText.setOnFocusChangeListener(null);
        adderssEditText.addTextChangedListener(null);
        toolbar.setNavigationOnClickListener(null);
    }


    @Override
    public void displayCity(City city) {
        dismissCityDialog();
        cityEditText.setText(city.getName());
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
    public void showCityDialog() {
        chooseCityDialog = CityDialog.newInstance(activity.getSupportFragmentManager());
    }

    @Override
    public void dismissCityDialog() {
        if (chooseCityDialog != null){
            chooseCityDialog.dismiss();
        }
    }

    @Override
    public void displayUser(User user) {
        this.user = user;
    }

    @Override
    public void displaySuccessLayout() {
        successLayout.setVisibility(VISIBLE);
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

                case R.id.city_et:
                    showCityDialog();
                    break;
            }
        }
    };

    OnFocusChangeListener onFocusChangeListener = new OnFocusChangeListener() {
        @Override
        public void onFocusChange(View view, boolean b) {
            switch (view.getId()){
                case R.id.city_et:
                    if (b){
                        showCityDialog();
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
