package com.donate.savelife.login.view;

import android.content.Context;
import android.graphics.Color;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.donate.savelife.R;
import com.donate.savelife.SaveLifeApplication;
import com.donate.savelife.apputils.ConnectivityReceiver;
import com.donate.savelife.apputils.DialogUtils;
import com.donate.savelife.apputils.Views;
import com.donate.savelife.component.materialcomponent.MaterialProgressDialog;
import com.donate.savelife.core.login.displayer.LoginDisplayer;


public class LoginView extends FrameLayout implements LoginDisplayer, ConnectivityReceiver.ConnectivityReceiverListener {

    private View loginButton;
    private AppCompatActivity appCompatActivity;
    private MaterialProgressDialog materialProgressDialog;

    public LoginView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        setBackgroundColor(ContextCompat.getColor(getContext(), R.color.primary));
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
        SaveLifeApplication.getInstance().setConnectivityListener(this);
        loginButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ConnectivityReceiver.isConnected()){
                    actionListener.onGooglePlusLoginSelected();
                } else {
                    showSnack();
                }
            }
        });
    }

    @Override
    public void detach(LoginActionListener actionListener) {
        SaveLifeApplication.getInstance().setConnectivityListener(null);
        loginButton.setOnClickListener(null);
    }

    @Override
    public void showAuthenticationError(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_LONG).show(); //TODO improve error display
    }

    public AppCompatActivity getAppCompatActivity() {
        return appCompatActivity;
    }

    public void setAppCompatActivity(AppCompatActivity appCompatActivity) {
        this.appCompatActivity = appCompatActivity;
    }


    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        if (!isConnected){
            showSnack();
        }
    }

    private void showSnack() {
        String message = "Sorry! Not connected to internet";
        int color = Color.WHITE;
        Snackbar snackbar = Snackbar
                .make(findViewById(android.R.id.content), message, Snackbar.LENGTH_LONG);

        View sbView = snackbar.getView();
        AppCompatTextView textView = (AppCompatTextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
        textView.setTextColor(color);
        snackbar.show();
    }
}
