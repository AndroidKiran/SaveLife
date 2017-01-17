package com.donate.savelife.core.notifications.database;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by ravi on 17/01/17.
 */

public class FCMRemoteMsg implements Parcelable{

    private String to;
    private String priority;
    private String collapse_key;
    private Data data;
    private Notification notification;
    private boolean content_available = false;
    private String registration_ids;

    public FCMRemoteMsg(){

    }


    protected FCMRemoteMsg(Parcel in) {
        to = in.readString();
        priority = in.readString();
        collapse_key = in.readString();
        data = in.readParcelable(Data.class.getClassLoader());
        notification = in.readParcelable(Notification.class.getClassLoader());
        content_available = in.readByte() != 0;
        registration_ids = in.readString();

    }

    public static final Creator<FCMRemoteMsg> CREATOR = new Creator<FCMRemoteMsg>() {
        @Override
        public FCMRemoteMsg createFromParcel(Parcel in) {
            return new FCMRemoteMsg(in);
        }

        @Override
        public FCMRemoteMsg[] newArray(int size) {
            return new FCMRemoteMsg[size];
        }
    };

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public String getCollapse_key() {
        return collapse_key;
    }

    public void setCollapse_key(String collapse_key) {
        this.collapse_key = collapse_key;
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public Notification getNotification() {
        return notification;
    }

    public void setNotification(Notification notification) {
        this.notification = notification;
    }

    public boolean isContent_available() {
        return content_available;
    }

    public void setContent_available(boolean content_available) {
        this.content_available = content_available;
    }

    public String getRegistration_ids() {
        return registration_ids;
    }

    public void setRegistration_ids(String registration_ids) {
        this.registration_ids = registration_ids;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(to);
        parcel.writeString(priority);
        parcel.writeString(collapse_key);
        parcel.writeParcelable(data, i);
        parcel.writeParcelable(notification, i);
        parcel.writeByte((byte) (content_available ? 1 : 0));
        parcel.writeString(registration_ids);
    }

    public static class Data implements Parcelable{
        private String ownerId;
        private String imageUrl;

        protected Data(Parcel in) {
            ownerId = in.readString();
            imageUrl = in.readString();
        }

        public static final Creator<Data> CREATOR = new Creator<Data>() {
            @Override
            public Data createFromParcel(Parcel in) {
                return new Data(in);
            }

            @Override
            public Data[] newArray(int size) {
                return new Data[size];
            }
        };

        public String getOwnerId() {
            return ownerId;
        }

        public void setOwnerId(String ownerId) {
            this.ownerId = ownerId;
        }

        public String getImageUrl() {
            return imageUrl;
        }

        public void setImageUrl(String imageUrl) {
            this.imageUrl = imageUrl;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel parcel, int i) {
            parcel.writeString(ownerId);
            parcel.writeString(imageUrl);
        }
    }

    public static class Notification implements Parcelable{
        private String title;
        private String body;
        private String sound = "default";
        private String icon;
        private String badge = "1";

        public Notification() {

        }

        protected Notification(Parcel in) {
            title = in.readString();
            body = in.readString();
            sound = in.readString();
            icon = in.readString();
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(title);
            dest.writeString(body);
            dest.writeString(sound);
            dest.writeString(icon);
        }

        @Override
        public int describeContents() {
            return 0;
        }

        public static final Creator<Notification> CREATOR = new Creator<Notification>() {
            @Override
            public Notification createFromParcel(Parcel in) {
                return new Notification(in);
            }

            @Override
            public Notification[] newArray(int size) {
                return new Notification[size];
            }
        };

        public String getBody() {
            return body;
        }

        public void setBody(String body) {
            this.body = body;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getSound() {
            return sound;
        }

        public void setSound(String sound) {
            this.sound = sound;
        }

        public String getIcon() {
            return icon;
        }

        public void setIcon(String icon) {
            this.icon = icon;
        }
    }
}
