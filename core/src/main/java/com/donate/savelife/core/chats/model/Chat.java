package com.donate.savelife.core.chats.model;


import android.os.Parcel;
import android.os.Parcelable;

import com.donate.savelife.core.UniqueList;

import java.util.ArrayList;

public class Chat implements Parcelable{

    private ArrayList<Message> messages;

    public Chat(UniqueList<Message> messages) {
        this.messages = messages;
    }

    protected Chat(Parcel in) {
        messages = in.createTypedArrayList(Message.CREATOR);
    }

    public static final Creator<Chat> CREATOR = new Creator<Chat>() {
        @Override
        public Chat createFromParcel(Parcel in) {
            return new Chat(in);
        }

        @Override
        public Chat[] newArray(int size) {
            return new Chat[size];
        }
    };

    public int size() {
        return messages.size();
    }

    public Message get(int position) {
        return messages.get(position);
    }

    public ArrayList<Message> getMessages(){
        return this.messages;
    }

    public void addAll(ArrayList<Message> messages){
        this.messages.addAll(messages);
    }

    public void addAllAt(int index, ArrayList<Message> messages){
        this.messages.addAll(index, messages);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Chat chat = (Chat) o;

        return messages != null ? messages.equals(chat.messages) : chat.messages == null;

    }

    @Override
    public int hashCode() {
        return messages != null ? messages.hashCode() : 0;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeTypedList(messages);
    }
}
