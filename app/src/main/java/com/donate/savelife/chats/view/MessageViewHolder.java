package com.donate.savelife.chats.view;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.donate.savelife.core.chats.model.Message;
import com.donate.savelife.core.requirement.model.Need;
import com.donate.savelife.core.user.data.model.User;

class MessageViewHolder extends RecyclerView.ViewHolder {

    private final MessageView messageView;

    public MessageViewHolder(MessageView messageView) {
        super(messageView);
        this.messageView = messageView;
    }

    public void bind(final Message message, User needOwner, Need need, final OnChatSelectionListener onChatSelectionListener) {
        messageView.display(message, needOwner, need);
        messageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onChatSelectionListener.onChatSelected(message);
            }
        });
    }

    public interface OnChatSelectionListener {
        void onChatSelected(Message message);
    }
}
