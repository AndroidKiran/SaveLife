package com.donate.savelife.chats.view;

import com.donate.savelife.core.chats.model.Message;

/**
 * Created by ravi on 12/02/17.
 */

public interface OnChatSelectionListener {

    void onChatSelected(Message message);

    void onProfilePicSelected(Message message);

}
