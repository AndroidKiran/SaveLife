package com.donate.savelife.chats.view;

import android.content.Context;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.Html;
import android.text.SpannableString;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.donate.savelife.R;
import com.donate.savelife.apputils.DialogUtils;
import com.donate.savelife.apputils.Utils;
import com.donate.savelife.apputils.Views;
import com.donate.savelife.component.MultiStateView;
import com.donate.savelife.component.paginate.Paginate;
import com.donate.savelife.component.text.EditText;
import com.donate.savelife.component.text.TextView;
import com.donate.savelife.core.chats.database.ChatDatabase;
import com.donate.savelife.core.chats.displayer.ChatDisplayer;
import com.donate.savelife.core.chats.model.Chat;
import com.donate.savelife.core.chats.model.Message;
import com.donate.savelife.core.requirement.model.Need;
import com.donate.savelife.core.user.data.model.User;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatView extends LinearLayout implements ChatDisplayer {
    private Message lastItemMessage;

    private final ChatAdapter chatAdapter;
    private EditText messageView;
    private ImageView submitButton;
    private RecyclerView recyclerView;
    private Toolbar toolbar;

    private ChatActionListener actionListener;
    private CircleImageView profileImage;
    private TextView toolbarTitle;
    private LinearLayoutManager linearLayoutManager;
    private boolean isloading;
    private Paginate paginateInteraction;
    private TextView toolbarSubTitle;
    private View toolbarContent;
    private AlertDialog needDialog;
    private MultiStateView multiView;
    private TextView emptyViewTxt;
    private AppCompatImageView emptyViewIcon;
    private AppCompatImageView mapAttachBtn;

    public ChatView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setOrientation(VERTICAL);
        chatAdapter = new ChatAdapter(LayoutInflater.from(context));
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        View.inflate(getContext(), R.layout.merge_chat_view, this);
        initToolbar();
        initControl();
        initRecyclerView();
        setPagination();
    }

    private void initToolbar() {
        toolbar = Views.findById(this, R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
        toolbar.inflateMenu(R.menu.menu_honor);
    }

    private void initControl() {
        messageView = Views.findById(this, R.id.message_edit);
        submitButton = Views.findById(this, R.id.submit_button);
        recyclerView = Views.findById(this, R.id.messages_recycler_view);
        toolbarTitle = Views.findById(this, R.id.toolbar_title);
        toolbarSubTitle = Views.findById(this, R.id.toolbar_sub_title);
        profileImage = Views.findById(this, R.id.profile_image);
        toolbarContent = Views.findById(this, R.id.toolbar_content);
        multiView = Views.findById(this, R.id.multi_view);
        emptyViewTxt = (TextView) multiView.findViewById(R.id.txt_empty);
        emptyViewIcon = (AppCompatImageView) multiView.findViewById(R.id.img_empty);
        mapAttachBtn = Views.findById(this, R.id.map_attach_button);
        mapAttachBtn.setColorFilter(getResources().getColor(R.color.primary), PorterDuff.Mode.SRC_ATOP);


    }

    private void initRecyclerView() {
        recyclerView.addItemDecoration(new ChatItemDecoration());
        linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setReverseLayout(true);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(chatAdapter);
    }

    private void setPagination() {
        if (paginateInteraction != null) {
            paginateInteraction.unbind();
        }
        paginateInteraction = Paginate.with(recyclerView, callbacks)
                .setLoadingTriggerThreshold(5)
                .addLoadingListItem(true)
                .build();
    }

    public void setTitleLayout(Need need, final User user) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(need.getAddress() + ", " + need.getCity() + "\n");
        stringBuilder.append(need.getCountryName(getContext()));

        toolbarTitle.setText(String.format(getResources().getString(R.string.str_blood_required_msg), need.getBloodGroup()));
        toolbarSubTitle.setText(stringBuilder.toString());
        Glide.with(getContext()).load(user.getPhotoUrl()).diskCacheStrategy(DiskCacheStrategy.ALL).into(profileImage);
    }

    @Override
    public void scrollChat() {
        recyclerView.scrollToPosition(0);
    }

    @Override
    public void showNeedDialog(Need need, User user) {
        needDialog = DialogUtils.showRequirementDialog(getContext(), createView(need, user));
        needDialog.show();
    }


    @Override
    public void displayLoading() {
        multiView.setViewState(MultiStateView.VIEW_STATE_LOADING);
    }

    @Override
    public void displayContent() {
        multiView.setViewState(MultiStateView.VIEW_STATE_CONTENT);
    }

    @Override
    public void displayError() {
        multiView.setViewState(MultiStateView.VIEW_STATE_ERROR);
    }

    @Override
    public void displayEmpty() {
        emptyViewTxt.setText(getContext().getString(R.string.str_chat_empty_state));
        emptyViewIcon.setImageResource(R.drawable.ic_stars_24dp);
        multiView.setViewState(MultiStateView.VIEW_STATE_EMPTY);
    }

    @Override
    public Chat getChat() {
        return chatAdapter.getChat();
    }

    @Override
    public User getUser() {
        return chatAdapter.getUser();
    }

    @Override
    public Message getLastMessage() {
        return lastItemMessage;
    }

    @Override
    public void setLastMessage(Message lastMessage) {
        this.lastItemMessage = lastMessage;
    }


    @Override
    public void attach(final ChatActionListener actionListener) {
        this.actionListener = actionListener;
        messageView.addTextChangedListener(textWatcher);
        submitButton.setOnClickListener(onClickListener);
        toolbar.setNavigationOnClickListener(navigationClickListener);
        toolbarContent.setOnClickListener(onClickListener);
        toolbar.setOnMenuItemClickListener(onMenuItemClickListener);
        chatAdapter.attach(actionListener);
        mapAttachBtn.setOnClickListener(onClickListener);
    }

    @Override
    public void detach(ChatActionListener actionListener) {
        submitButton.setOnClickListener(null);
        messageView.removeTextChangedListener(null);
        toolbar.setOnMenuItemClickListener(null);
        toolbarContent.setOnClickListener(null);
        toolbar.setOnMenuItemClickListener(null);
        this.actionListener = actionListener;
        chatAdapter.attach(actionListener);
        mapAttachBtn.setOnClickListener(null);

    }

    @Override
    public void setTitle(String title) {
        toolbar.setTitle(title);
    }


    @Override
    public void display(Chat chat, User user, Need need) {
        lastItemMessage = new Message();
        lastItemMessage.setId("");
        chatAdapter.update(chat, user, need);
        isloading = false;
    }

    @Override
    public void displayMore(Chat chat, User user, Need need) {
        chatAdapter.updateMoreChat(chat, user, need);
        isloading = false;
    }

    @Override
    public void displayHeroes(boolean enable) {
        toolbar.getMenu().findItem(R.id.action_honor).setVisible(enable);
    }


    @Override
    public void enableInteraction() {
        submitButton.setEnabled(true);
        submitButton.setColorFilter(getResources().getColor(R.color.primary), PorterDuff.Mode.SRC_ATOP);
    }

    @Override
    public void disableInteraction() {
        submitButton.setEnabled(false);
        submitButton.setColorFilter(getResources().getColor(R.color.grey_shade_4), PorterDuff.Mode.SRC_ATOP);
    }

    private final TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }

        @Override
        public void afterTextChanged(Editable s) {
            actionListener.onMessageLengthChanged(s.toString().trim().length());
        }
    };

    private final OnClickListener navigationClickListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            actionListener.onUpPressed();
        }
    };


    private class ChatItemDecoration extends RecyclerView.ItemDecoration {

        private final int horizontalMargin = getResources().getDimensionPixelOffset(R.dimen.dimen_2);
        private final int verticalMargin = getResources().getDimensionPixelOffset(R.dimen.dimen_10);

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            outRect.left = horizontalMargin;
            outRect.right = horizontalMargin;
            outRect.top = verticalMargin;
            outRect.bottom = verticalMargin;
        }

    }

    OnClickListener onClickListener = new OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.toolbar_content:
                    actionListener.onToolbarClick();
                    break;

                case R.id.submit_button:
                    String msg = Utils.htmlToPlain(String.valueOf(Html.toHtml(new SpannableString(messageView.getText().toString().trim()))));
                    actionListener.onSubmitMessage(msg);
                    messageView.setText("");
                    break;

                case R.id.map_attach_button:
                    actionListener.onMapAttachClicked();
                    break;
            }
        }
    };


    Paginate.Callbacks callbacks = new Paginate.Callbacks() {
        @Override
        public void onLoadMore(int direction) {
            if (chatAdapter.getItemCount() != 0 && !isloading) {
                Message messageLast = chatAdapter.getLastItem();
                if (direction == Paginate.SCROLL_DOWN && chatAdapter.getItemCount() >= ChatDatabase.DEFAULT_LIMIT) {
                    actionListener.onLoadMore(messageLast);
                    isloading = true;
                }
                lastItemMessage = messageLast;
            }
        }

        @Override
        public boolean isLoading() {
            return isloading;
        }

        @Override
        public boolean hasLoadedAllItems() {
            if (chatAdapter.getItemCount() < ChatDatabase.DEFAULT_LIMIT) {
                return true;
            }

            if (lastItemMessage.getId().equals(chatAdapter.getLastItem().getId())) {
                return true;
            }

            return false;
        }
    };

    private final Toolbar.OnMenuItemClickListener onMenuItemClickListener = new Toolbar.OnMenuItemClickListener() {
        @Override
        public boolean onMenuItemClick(MenuItem item) {
            switch (item.getItemId()){
                case R.id.action_honor:
                    actionListener.onHonorClicked();
                    break;
            }
            return false;
        }
    };

    public View createView(final Need need, final User user) {
        View dialogView = LayoutInflater.from(getContext()).inflate(R.layout.dialog_need, null);
        FloatingActionButton callFabBtn = (FloatingActionButton) dialogView.findViewById(R.id.fab_button1);
        FloatingActionButton addressFabBtn = (FloatingActionButton) dialogView.findViewById(R.id.fab_button2);
        final CircleImageView profileImage = (CircleImageView) dialogView.findViewById(R.id.profile_image);
        TextView needMsg = (TextView) dialogView.findViewById(R.id.need_msg);
        final TextView addressTxt = (TextView) dialogView.findViewById(R.id.address_msg);
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(need.getAddress() + ", " + need.getCity() + ", " + need.getCountryName(getContext()));
        needMsg.setText(String.format(getResources().getString(R.string.str_blood_required_msg), need.getBloodGroup()));
        addressTxt.setText(stringBuilder.toString());
        Glide.with(getContext()).load(user.getPhotoUrl()).diskCacheStrategy(DiskCacheStrategy.ALL).thumbnail(0.8f)
                .crossFade().into(profileImage);
        callFabBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                actionListener.onCallClick(user.getMobileNum());
            }
        });

        addressFabBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                actionListener.onAddressClick(addressTxt.getText().toString());
            }
        });
        return dialogView;
    }

}
