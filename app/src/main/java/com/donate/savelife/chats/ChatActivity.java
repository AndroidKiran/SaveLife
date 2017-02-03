package com.donate.savelife.chats;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.transition.TransitionInflater;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.donate.savelife.R;
import com.donate.savelife.chats.view.ChatView;
import com.donate.savelife.core.analytics.Analytics;
import com.donate.savelife.core.chats.displayer.ChatDisplayer;
import com.donate.savelife.core.chats.presenter.ChatPresenter;
import com.donate.savelife.core.utils.AppConstant;
import com.donate.savelife.firebase.Dependencies;
import com.donate.savelife.navigation.AndroidNavigator;

import static com.donate.savelife.core.utils.AppConstant.NEED_EXTRA;


public class ChatActivity extends AppCompatActivity {

    private ChatPresenter presenter;
    private Analytics analytics;
    private LinearLayout contentView;
    private ChatView chatView;
    private TransitionInflater transitionInflater;
    private RelativeLayout toolbarContent;

    public static Intent createIntentFor(Context context, String needId) {
        Intent intent = new Intent(context, ChatActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString(AppConstant.NEED_EXTRA, needId);
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

        chatView = (ChatView) findViewById(R.id.chat_view);
        contentView = (LinearLayout) chatView.findViewById(R.id.content);
        toolbarContent = (RelativeLayout) chatView.findViewById(R.id.toolbar_content);
        String needId = getIntent().getExtras().getString(NEED_EXTRA);
        presenter = new ChatPresenter(
                Dependencies.INSTANCE.getLoginService(),
                Dependencies.INSTANCE.getChatService(),
                (ChatDisplayer) chatView,
                needId,
                analytics,
                new AndroidNavigator(this),
                Dependencies.INSTANCE.getErrorLogger(),
                Dependencies.INSTANCE.getNeedService(),
                Dependencies.INSTANCE.getGsonService(),
                Dependencies.INSTANCE.getPreference(),
                Dependencies.INSTANCE.getNotificationRegistrationService()
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

}
