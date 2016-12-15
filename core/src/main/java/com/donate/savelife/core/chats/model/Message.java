package com.donate.savelife.core.chats.model;


import android.os.Parcel;
import android.os.Parcelable;

import com.donate.savelife.core.user.data.model.User;

public class Message implements Parcelable{

    private String id;
    private String userId;
    private User author;
    private String body;
    private long timestamp;
    private String needId;

    @SuppressWarnings("unused") //Used by Firebase
    public Message() {
    }

    public Message(User author, String body) {
        this.author = author;
        this.body = body;
        this.timestamp = System.currentTimeMillis(); //TODO move timestamp db side ?
    }

    public Message(String userId, String body) {
        this.userId = userId;
        this.body = body;
        this.timestamp = System.currentTimeMillis(); //TODO move timestamp db side ?
    }


    protected Message(Parcel in) {
        id = in.readString();
        userId = in.readString();
        author = in.readParcelable(User.class.getClassLoader());
        body = in.readString();
        timestamp = in.readLong();
        needId = in.readString();
    }

    public static final Creator<Message> CREATOR = new Creator<Message>() {
        @Override
        public Message createFromParcel(Parcel in) {
            return new Message(in);
        }

        @Override
        public Message[] newArray(int size) {
            return new Message[size];
        }
    };


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(id);
        parcel.writeString(userId);
        parcel.writeParcelable(author, i);
        parcel.writeString(body);
        parcel.writeLong(timestamp);
        parcel.writeString(needId);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Message)) return false;

        Message message = (Message) o;

        if (timestamp != message.timestamp) return false;
        if (id != null ? !id.equals(message.id) : message.id != null) return false;
        if (userId != null ? !userId.equals(message.userId) : message.userId != null) return false;
        if (author != null ? !author.equals(message.author) : message.author != null) return false;
        return body != null ? body.equals(message.body) : message.body == null;

    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (userId != null ? userId.hashCode() : 0);
        result = 31 * result + (author != null ? author.hashCode() : 0);
        result = 31 * result + (body != null ? body.hashCode() : 0);
        result = 31 * result + (int) (timestamp ^ (timestamp >>> 32));
        return result;
    }

    @Override
    public String toString() {
        return "Message{" +
                "id='" + id + '\'' +
                ", userId='" + userId + '\'' +
                ", author=" + author +
                ", body='" + body + '\'' +
                ", timestamp=" + timestamp +
                '}';
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public String getNeedId() {
        return needId;
    }

    public void setNeedId(String needId) {
        this.needId = needId;
    }

}
