package com.donate.savelife.core.chats.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by ravi on 13/02/17.
 */

public class Map implements Parcelable{

    private String latitude;
    private String longitude;

    public Map(){

    }

    public Map(String latitude, String longitude){
        this.longitude = longitude;
        this.latitude = latitude;
    }


    protected Map(Parcel in) {
        latitude = in.readString();
        longitude = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(latitude);
        dest.writeString(longitude);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Map> CREATOR = new Creator<Map>() {
        @Override
        public Map createFromParcel(Parcel in) {
            return new Map(in);
        }

        @Override
        public Map[] newArray(int size) {
            return new Map[size];
        }
    };

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }
}
