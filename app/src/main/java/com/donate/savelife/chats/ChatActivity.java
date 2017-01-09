package com.donate.savelife.chats;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.donate.savelife.R;
import com.donate.savelife.core.analytics.Analytics;
import com.donate.savelife.core.chats.displayer.ChatDisplayer;
import com.donate.savelife.core.chats.presenter.ChatPresenter;
import com.donate.savelife.core.requirement.model.Need;
import com.donate.savelife.core.utils.AppConstant;
import com.donate.savelife.firebase.Dependencies;
import com.donate.savelife.navigation.AndroidNavigator;

import static com.donate.savelife.core.utils.AppConstant.NEED_EXTRA;


public class ChatActivity extends AppCompatActivity {

    private ChatPresenter presenter;
    private Analytics analytics;

    public static Intent createIntentFor(Context context, Need need) {
        Intent intent = new Intent(context, ChatActivity.class);
        Bundle bundle = new Bundle();
        bundle.putParcelable(AppConstant.NEED_EXTRA, need);
        intent.putExtras(bundle);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        analytics = Dependencies.INSTANCE.getAnalytics();

        if (savedInstanceState == null){
            analytics.trackScreen(this, AppConstant.CHAT_SCREEN, null);
        }

        ChatDisplayer chatDisplayer = (ChatDisplayer) findViewById(R.id.chat_view);
        Need need = (Need) getIntent().getExtras().getParcelable(NEED_EXTRA);
        presenter = new ChatPresenter(
                Dependencies.INSTANCE.getLoginService(),
                Dependencies.INSTANCE.getChatService(),
                chatDisplayer,
                need,
                analytics,
                new AndroidNavigator(this),
                Dependencies.INSTANCE.getErrorLogger()
        );
    }

    @Override
    protected void onStart() {
        super.onStart();
        presenter.startPresenting();
    }

    @Override
    protected void onStop() {
        presenter.stopPresenting();
        super.onStop();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        presenter.onSaveInstanceState(outState);
        super.onSaveInstanceState(outState);
    }
}
