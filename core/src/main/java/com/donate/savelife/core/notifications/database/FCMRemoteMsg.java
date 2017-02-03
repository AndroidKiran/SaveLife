package com.donate.savelife.core.notifications.database;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

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
    private List<String> registration_ids;

    public FCMRemoteMsg(){

    }

    protected FCMRemoteMsg(Parcel in) {
        to = in.readString();
        priority = in.readString();
        collapse_key = in.readString();
        data = in.readParcelable(Data.class.getClassLoader());
        notification = in.readParcelable(Notification.class.getClassLoader());
        content_available = in.readByte() != 0;
        registration_ids = in.createStringArrayList();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(to);
        dest.writeString(priority);
        dest.writeString(collapse_key);
        dest.writeParcelable(data, flags);
        dest.writeParcelable(notification, flags);
        dest.writeByte((byte) (content_available ? 1 : 0));
        dest.writeStringList(registration_ids);
    }

    @Override
    public int describeContents() {
        return 0;
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

    public List<String> getRegistration_ids() {
        return registration_ids;
    }

    public void setRegistration_ids(List<String> registration_ids) {
        this.registration_ids = registration_ids;
    }

    public static class Data implements Parcelable{
        private String click_action;
        private String need_id;

        public Data(){

        }

        protected Data(Parcel in) {
            click_action = in.readString();
            need_id = in.readString();
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


        public String getClick_action() {
            return click_action;
        }

        public String getNeed_id() {
            return need_id;
        }

        public void setNeed_id(String need_id) {
            this.need_id = need_id;
        }

        public void setClick_action(String click_action) {
            this.click_action = click_action;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel parcel, int i) {
            parcel.writeString(click_action);
            parcel.writeString(need_id);
        }
    }

    public static class Notification implements Parcelable{
        private String title;
        private String body;
        private String sound = "default";
        private String icon;
        private String badge = "1";
        private String click_action;

        public Notification() {

        }

        protected Notification(Parcel in) {
            title = in.readString();
            body = in.readString();
            sound = in.readString();
            icon = in.readString();
            click_action = in.readString();
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(title);
            dest.writeString(body);
            dest.writeString(sound);
            dest.writeString(icon);
            dest.writeString(click_action);
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

        public String getBadge() {
            return badge;
        }

        public void setBadge(String badge) {
            this.badge = badge;
        }

        public String getClick_action() {
            return click_action;
        }

        public void setClick_action(String click_action) {
            this.click_action = click_action;
        }
    }
}
