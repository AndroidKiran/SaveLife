package com.donate.savelife.chats.view;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.donate.savelife.R;
import com.donate.savelife.core.UniqueList;
import com.donate.savelife.core.chats.displayer.ChatDisplayer;
import com.donate.savelife.core.chats.model.Chat;
import com.donate.savelife.core.chats.model.Message;
import com.donate.savelife.core.requirement.model.Need;
import com.donate.savelife.core.user.data.model.User;

class ChatAdapter extends RecyclerView.Adapter<MessageViewHolder> {

    private static final int VIEW_TYPE_MESSAGE_THIS_USER = 0;
    private static final int VIEW_TYPE_MESSAGE_OTHER_USERS = 1;
    private Chat chat;
    private User user;
    private final LayoutInflater inflater;
    private MessageView messageView;
    private ChatDisplayer.ChatActionListener chatActionListener;
    private Need need;

    ChatAdapter(LayoutInflater inflater) {
        this.inflater = inflater;
        user = new User();
        chat = new Chat(new UniqueList<Message>());
        setHasStableIds(true);
    }

    public void update(Chat chat, User user, Need need) {
        if (chat.size() > 0){
            this.chat = chat;
            this.user = user;
            this.need = need;
            notifyDataSetChanged();
            chatActionListener.onContentLoaded();
        } else {
            chatActionListener.onEmpty();
        }
    }

    public void updateMoreChat(Chat chat, User user,Need need){
        this.chat.addAll(chat.getMessages());
        this.user = user;
        this.need = need;
        notifyItemRangeInserted(getItemCount(), chat.size());
    }

    public Message getLastItem(){
        int count = getItemCount() == 0 ? 0 : getItemCount() - 1;
        return chat.get(count);
    }


    @Override
    public MessageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        if (viewType == VIEW_TYPE_MESSAGE_THIS_USER) {
            messageView = (MessageView) inflater.inflate(R.layout.self_message_item_layout, parent, false);
        } else if (viewType == VIEW_TYPE_MESSAGE_OTHER_USERS) {
            messageView = (MessageView) inflater.inflate(R.layout.message_item_layout, parent, false);
        }
        return new MessageViewHolder(messageView);
    }

    @Override
    public void onBindViewHolder(MessageViewHolder holder, int position) {
        holder.bind(chat.get(position), user, need, onChatSelectionListener);
    }

    @Override
    public int getItemCount() {
        return chat.size();
    }

    @Override
    public long getItemId(int position) {
        return chat.get(position).getTimestamp();
    }

    public Chat getChat() {
        return this.chat;
    }

    public User getUser(){
        return this.user;
    }

    @Override
    public int getItemViewType(int position) {
        return chat.get(position).getUserId().equals(user.getId()) ? VIEW_TYPE_MESSAGE_THIS_USER : VIEW_TYPE_MESSAGE_OTHER_USERS;
    }

    public void attach(ChatDisplayer.ChatActionListener chatActionListener){
        this.chatActionListener = chatActionListener;
    }

    public void detach(ChatDisplayer.ChatActionListener chatActionListener){
        this.chatActionListener = chatActionListener;
    }

    MessageViewHolder.OnChatSelectionListener onChatSelectionListener = new MessageViewHolder.OnChatSelectionListener() {

        @Override
        public void onChatSelected(Message message) {
            chatActionListener.onChatClicked(message);
        }
    };
}
