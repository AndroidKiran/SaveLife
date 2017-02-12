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
import com.donate.savelife.core.utils.CoreUtils;

class ChatAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int VIEW_TYPE_MESSAGE_THIS_USER = 0;
    private static final int VIEW_TYPE_MESSAGE_OTHER_USERS = 1;
    private static final int VIEW_TYPE_MAP_THIS_USER = 2;
    private static final int VIEW_TYPE_MAP_OTHER_USERS = 3;

    private Chat chat;
    private User user;
    private final LayoutInflater inflater;
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
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder = null;
        switch (viewType){
            case VIEW_TYPE_MESSAGE_THIS_USER:
                MessageView messageView = (MessageView) inflater.inflate(R.layout.self_message_item_layout, parent, false);
                viewHolder = new MessageViewHolder(messageView);
                break;
            case VIEW_TYPE_MESSAGE_OTHER_USERS:
                MessageView messageView1 = (MessageView) inflater.inflate(R.layout.message_item_layout, parent, false);
                viewHolder = new MessageViewHolder(messageView1);
                break;
            case VIEW_TYPE_MAP_THIS_USER:
                MapView mapView = (MapView) inflater.inflate(R.layout.self_map_item_layout, parent, false);
                viewHolder = new MapViewHolder(mapView);
                break;
            case VIEW_TYPE_MAP_OTHER_USERS:
                MapView mapView1 = (MapView) inflater.inflate(R.layout.map_item_layout, parent, false);
                viewHolder = new MapViewHolder(mapView1);
                break;
        }

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof MessageViewHolder){
            ((MessageViewHolder) holder).bind(chat.get(position), user, need, onChatSelectionListener);
        } else {
            ((MapViewHolder) holder).bind(chat.get(position), user, need, onChatSelectionListener);
        }
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
        Message message = chat.get(position);
        int viewType = -1;
        switch (message.getContentType()){
            case CoreUtils.ContentType.TXT:
                if (isMyMsg(message)){
                    viewType = VIEW_TYPE_MESSAGE_THIS_USER;
                } else {
                    viewType = VIEW_TYPE_MESSAGE_OTHER_USERS;
                }
                break;

            case CoreUtils.ContentType.MAP:
                if (isMyMsg(message)){
                    viewType = VIEW_TYPE_MAP_THIS_USER;
                } else {
                    viewType = VIEW_TYPE_MAP_OTHER_USERS;
                }
                break;
        }

        return viewType;
    }

    private boolean isMyMsg(Message message){
        if (message.getUserID().equals(user.getId())){
            return true;
        }
        return false;
    }

    public void attach(ChatDisplayer.ChatActionListener chatActionListener){
        this.chatActionListener = chatActionListener;
    }

    public void detach(ChatDisplayer.ChatActionListener chatActionListener){
        this.chatActionListener = chatActionListener;
    }

    OnChatSelectionListener onChatSelectionListener = new OnChatSelectionListener() {

        @Override
        public void onChatSelected(Message message) {
            chatActionListener.onChatClicked(message);
        }

        @Override
        public void onProfilePicSelected(Message message) {
            chatActionListener.onProfileClicked(message);
        }
    };
}
