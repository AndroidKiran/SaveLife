package com.donate.savelife.launcher.view;

import android.content.Context;
import android.support.v7.widget.AppCompatButton;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ProgressBar;

import com.donate.savelife.R;
import com.donate.savelife.apputils.Views;
import com.donate.savelife.component.text.TextView;
import com.donate.savelife.core.launcher.displayer.LauncherDisplayer;
import com.donate.savelife.core.launcher.model.AppStatus;

/**
 * Created by ravi on 03/02/17.
 */

public class LauncherView extends FrameLayout implements LauncherDisplayer {

    private final Context context;
    private TextView statusMsg;
    private ProgressBar progressBar;
    private AppCompatButton statusBtn;
    private LauncherInteractionListener launcherInteractionListener;

    public LauncherView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        View.inflate(getContext(), R.layout.merge_launcher_view, this);
        initControls();
    }

    private void initControls() {
        this.statusMsg = Views.findById(this, R.id.launch_msg);
        this.progressBar = Views.findById(this, R.id.loader);
        this.statusBtn = Views.findById(this, R.id.launch_action_btn);
    }


    @Override
    public boolean isVersionDeprecated() {
        return false;
    }

    @Override
    public void display(AppStatus appStatus) {
        progressBar.setVisibility(GONE);
        statusBtn.setVisibility(VISIBLE);
        statusMsg.setVisibility(VISIBLE);
        if (appStatus.isVersionDeprecated()) {
            statusMsg.setText(context.getString(R.string.str_app_deprecated_msg));
            statusBtn.setText(context.getString(R.string.str_btn_update_txt));
        }

        if (appStatus.isError()) {
            statusMsg.setText(context.getString(R.string.str_app_error_msg));
            statusBtn.setText(context.getString(R.string.str_btn_try_again_txt));
        }
    }

    public void attach(LauncherDisplayer.LauncherInteractionListener launcherInteractionListener) {
        this.launcherInteractionListener = launcherInteractionListener;
        statusBtn.setOnClickListener(onClickListener);
    }

    public void detach(LauncherDisplayer.LauncherInteractionListener launcherInteractionListener) {
        this.launcherInteractionListener = launcherInteractionListener;
        statusBtn.setOnClickListener(null);
    }

    View.OnClickListener onClickListener = new OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.launch_action_btn:
                    progressBar.setVisibility(VISIBLE);
                    statusBtn.setVisibility(GONE);
                    statusMsg.setVisibility(GONE);
                    if (statusBtn.getText().toString().equals(context.getString(R.string.str_btn_update_txt))) {
                        launcherInteractionListener.onUpdate();
                    }

                    if (statusBtn.getText().toString().equals(context.getString(R.string.str_btn_try_again_txt))) {
                        launcherInteractionListener.onRetry();
                    }

                    break;
            }
        }
    };
}
