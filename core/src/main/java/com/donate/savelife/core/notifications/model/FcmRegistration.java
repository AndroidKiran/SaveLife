package com.donate.savelife.core.notifications.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by ravi on 17/01/17.
 */

public class FcmRegistration implements Parcelable {

    private String userId;
    private String regId;

    public FcmRegistration(){

    }

    protected FcmRegistration(Parcel in) {
        userId = in.readString();
        regId = in.readString();
    }

    public static final Creator<FcmRegistration> CREATOR = new Creator<FcmRegistration>() {
        @Override
        public FcmRegistration createFromParcel(Parcel in) {
            return new FcmRegistration(in);
        }

        @Override
        public FcmRegistration[] newArray(int size) {
            return new FcmRegistration[size];
        }
    };

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getRegId() {
        return regId;
    }

    public void setRegId(String regId) {
        this.regId = regId;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(userId);
        parcel.writeString(regId);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof FcmRegistration)) return false;

        FcmRegistration that = (FcmRegistration) o;

        if (userId != null ? !userId.equals(that.userId) : that.userId != null) return false;
        return regId != null ? regId.equals(that.regId) : that.regId == null;

    }

    @Override
    public int hashCode() {
        int result = userId != null ? userId.hashCode() : 0;
        result = 31 * result + (regId != null ? regId.hashCode() : 0);
        return result;
    }
}
