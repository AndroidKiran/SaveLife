package com.donate.savelife.login.view;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;

import com.donate.savelife.R;
import com.donate.savelife.apputils.DialogUtils;
import com.donate.savelife.component.materialcomponent.MaterialProgressDialog;
import com.donate.savelife.core.login.displayer.LoginDisplayer;
import com.novoda.notils.caster.Views;


public class LoginView extends FrameLayout implements LoginDisplayer {

    private View loginButton;
    private AppCompatActivity appCompatActivity;
    private MaterialProgressDialog materialProgressDialog;

    public LoginView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        setBackgroundColor(ContextCompat.getColor(getContext(), R.color.material_red));
        View.inflate(getContext(), R.layout.merge_login_view, this);
        loginButton = Views.findById(this, R.id.sign_in_button);
    }

    @Override
    public void showProgress() {
        materialProgressDialog =  new MaterialProgressDialog(getAppCompatActivity());
        DialogUtils.showMaterialProgressDialog(materialProgressDialog, getAppCompatActivity().getString(R.string.str_progress_login_title), getAppCompatActivity().getString(R.string.str_progress_wait), getAppCompatActivity());
    }

    @Override
    public void dismissProgress() {
        if (materialProgressDialog != null){
            materialProgressDialog.dismiss();
        }
    }

    @Override
    public void attach(final LoginActionListener actionListener) {
        loginButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                actionListener.onGooglePlusLoginSelected();
            }
        });
    }

    @Override
    public void detach(LoginActionListener actionListener) {
        loginButton.setOnClickListener(null);
    }

    @Override
    public void showAuthenticationError(String message) {
//        Toast.makeText(getContext(), message, Toast.LENGTH_LONG).show(); //TODO improve error display
    }

    public AppCompatActivity getAppCompatActivity() {
        return appCompatActivity;
    }

    public void setAppCompatActivity(AppCompatActivity appCompatActivity) {
        this.appCompatActivity = appCompatActivity;
    }


}
