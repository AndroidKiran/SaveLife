package com.donate.savelife;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;

import com.donate.savelife.core.chats.model.Message;
import com.donate.savelife.core.user.data.model.User;
import com.donate.savelife.core.utils.AppConstant;
import com.donate.savelife.core.utils.GsonService;
import com.donate.savelife.core.utils.SharedPreferenceService;
import com.donate.savelife.firebase.Dependencies;
import com.donate.savelife.navigation.AndroidNavigator;

/**
 * Created by ravi on 18/01/17.
 */

public abstract class NavigationActivity extends AppCompatActivity{

    protected SharedPreferenceService sharedPreferenceService;
    protected GsonService gsonService;
    protected AndroidNavigator navigator;
    protected User user;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        navigator = new AndroidNavigator(this);
        sharedPreferenceService = Dependencies.INSTANCE.getPreference();
        gsonService = Dependencies.INSTANCE.getGsonService();
        user = gsonService.toUser(sharedPreferenceService.getLoginUserPreference());
        Intent intent = SaveLifeApplication.getInstance().getIndexIntent();
        if (intent != null){
            onNewIntent(intent);
            SaveLifeApplication.getInstance().setIndexIntent(null);
        }
    }


    @Override
    protected void onNewIntent(Intent intent) {

        String clickAction = intent.getStringExtra(AppConstant.CLICK_ACTION_EXTRA);
        Log.e("data value", "value ===="+clickAction);

        if (TextUtils.isEmpty(clickAction))
            return;

        switch (clickAction){
            case AppConstant.CLICK_ACTION_CHAT:
                String needId = intent.getStringExtra(AppConstant.NEED_ID_EXTRA);
                if (!TextUtils.isEmpty(needId)){
                    navigator.toChat(needId);
                }
                break;

            case AppConstant.CLICK_ACTION_PROFILE:
                Message message = new Message();
                message.setNeedId("");
                message.setUserID(user.getId());
                navigator.toProfile(message);
                break;
        }

    }
}
