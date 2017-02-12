package com.donate.savelife.core.chats.model;


import android.os.Parcel;
import android.os.Parcelable;

import com.donate.savelife.core.user.data.model.User;
import com.donate.savelife.core.utils.CoreUtils;

public class Message implements Parcelable{

    private String id;
    private String userID;
    private User author;
    private String body;
    private long timestamp;
    private String needId;
    private String fileUrl;
    private int contentType;
    private Map map;

    public Message() {

    }

    public Message(String userId, String message){
        this.userID = userId;
        this.body = message;
        this.contentType = CoreUtils.ContentType.TXT;
        timestamp = System.currentTimeMillis();
    }

    public Message(String userId, Map map, int contentType){
        this.userID = userId;
        this.fileUrl = CoreUtils.local(map);
        this.contentType = contentType;
        this.map = map;
        timestamp = System.currentTimeMillis();
    }


    protected Message(Parcel in) {
        id = in.readString();
        userID = in.readString();
        author = in.readParcelable(User.class.getClassLoader());
        body = in.readString();
        timestamp = in.readLong();
        needId = in.readString();
        fileUrl = in.readString();
        contentType = in.readInt();
        map = in.readParcelable(Map.class.getClassLoader());
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(userID);
        dest.writeParcelable(author, flags);
        dest.writeString(body);
        dest.writeLong(timestamp);
        dest.writeString(needId);
        dest.writeString(fileUrl);
        dest.writeInt(contentType);
        dest.writeParcelable(map, flags);
    }

    @Override
    public int describeContents() {
        return 0;
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Message)) return false;

        Message message = (Message) o;

        return id != null ? id.equals(message.id) : message.id == null;

    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "Message{" +
                "id='" + id + '\'' +
                '}';
    }

    public Map getMap() {
        return map;
    }

    public void setMap(Map map) {
        this.map = map;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
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

    public String getFileUrl() {
        return fileUrl;
    }

    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
    }

    public int getContentType() {
        return contentType;
    }

    public void setContentType(int contentType) {
        this.contentType = contentType;
    }


}
