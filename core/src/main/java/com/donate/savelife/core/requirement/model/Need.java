package com.donate.savelife.core.requirement.model;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;

import com.donate.savelife.core.user.data.model.User;

import java.util.Locale;

public class Need implements Parcelable{

    public static final String CITY = "city";
    public static final String USER_ID = "userID";
    public static final String RESPONSE_COUNT = "responseCount";



    private String id;
    private String city;
    private String bloodGroup;
    private String country;
    private String address;
    private String userID;
    private long timeStamp;
    private User user;
    private int responseCount = 0;

    public Need() {
    }


    protected Need(Parcel in) {
        id = in.readString();
        city = in.readString();
        bloodGroup = in.readString();
        country = in.readString();
        address = in.readString();
        userID = in.readString();
        timeStamp = in.readLong();
        user = in.readParcelable(User.class.getClassLoader());
        responseCount = in.readInt();
    }

    public static final Creator<Need> CREATOR = new Creator<Need>() {
        @Override
        public Need createFromParcel(Parcel in) {
            return new Need(in);
        }

        @Override
        public Need[] newArray(int size) {
            return new Need[size];
        }
    };

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Need)) return false;

        Need need = (Need) o;

        if (timeStamp != need.timeStamp) return false;
        if (id != null ? !id.equals(need.id) : need.id != null) return false;
        if (city != null ? !city.equals(need.city) : need.city != null) return false;
        if (bloodGroup != null ? !bloodGroup.equals(need.bloodGroup) : need.bloodGroup != null)
            return false;
        if (country != null ? !country.equals(need.country) : need.country != null) return false;
        if (address != null ? !address.equals(need.address) : need.address != null) return false;
        if (userID != null ? !userID.equals(need.userID) : need.userID != null) return false;
        return user != null ? user.equals(need.user) : need.user == null;

    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (city != null ? city.hashCode() : 0);
        result = 31 * result + (bloodGroup != null ? bloodGroup.hashCode() : 0);
        result = 31 * result + (country != null ? country.hashCode() : 0);
        result = 31 * result + (address != null ? address.hashCode() : 0);
        result = 31 * result + (userID != null ? userID.hashCode() : 0);
        result = 31 * result + (int) (timeStamp ^ (timeStamp >>> 32));
        result = 31 * result + (user != null ? user.hashCode() : 0);
        return result;
    }

    public String getId() {
        return id;
    }

    public String getCity() {
        return city;
    }

    public String getBloodGroup() {
        return bloodGroup;
    }

    public String getCountry() {
        return country;
    }

    public String getAddress(){
        return address;
    }

    public String getUserID(){
        return userID;
    }

    public void setId(String id){
        this.id = id;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setBloodGroup(String bloodGroup) {
        this.bloodGroup = bloodGroup;
    }

    public void setCountry(String country){
        this.country = country;
    }

    public void setAddress(String address){
        this.address = address;
    }

    public void setUserID(String uid){
        this.userID = uid;
    }

    public String getCountryName(Context con) {
        return new Locale(con.getResources().getConfiguration().locale.getLanguage(), country).getDisplayCountry();
    }

    public long getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(long timeStamp) {
        this.timeStamp = timeStamp;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public int getResponseCount() {
        return responseCount;
    }

    public void setResponseCount(int responseCount) {
        this.responseCount = responseCount;
    }

    @Override
    public int describeContents() {
        return 0;
    }


    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(id);
        parcel.writeString(city);
        parcel.writeString(bloodGroup);
        parcel.writeString(country);
        parcel.writeString(address);
        parcel.writeString(userID);
        parcel.writeLong(timeStamp);
        parcel.writeParcelable(user, i);
        parcel.writeInt(responseCount);

    }
}
