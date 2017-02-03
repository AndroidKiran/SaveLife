package com.donate.savelife.core.user.data.model;

import android.os.Parcel;
import android.os.Parcelable;

public class User implements Parcelable{
    public static final String LIFE_COUNT = "lifeCount";
    public static final String CITY = "city";

    private String id;
    private String name;
    private String photoUrl;
    private String email;
    private String mobileNum;
    private String city;
    private String bloodGroup;
    private String country;
    private int lifeCount;

    @SuppressWarnings("unused") //Used by Firebase
    public User() {
    }

    public User(String id, String name, String photoUrl, String email) {
        this.id = id;
        this.name = name;
        this.photoUrl = photoUrl;
        this.email = email;
    }


    protected User(Parcel in) {
        id = in.readString();
        name = in.readString();
        photoUrl = in.readString();
        email = in.readString();
        mobileNum = in.readString();
        city = in.readString();
        bloodGroup = in.readString();
        country = in.readString();
        lifeCount = in.readInt();
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(id);
        parcel.writeString(name);
        parcel.writeString(photoUrl);
        parcel.writeString(email);
        parcel.writeString(mobileNum);
        parcel.writeString(city);
        parcel.writeString(bloodGroup);
        parcel.writeString(country);
        parcel.writeInt(lifeCount);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User)) return false;

        User user = (User) o;

        if (id != null ? !id.equals(user.id) : user.id != null) return false;
        if (name != null ? !name.equals(user.name) : user.name != null) return false;
        if (photoUrl != null ? !photoUrl.equals(user.photoUrl) : user.photoUrl != null)
            return false;
        if (email != null ? !email.equals(user.email) : user.email != null) return false;
        if (mobileNum != null ? !mobileNum.equals(user.mobileNum) : user.mobileNum != null)
            return false;
        if (city != null ? !city.equals(user.city) : user.city != null) return false;
        if (bloodGroup != null ? !bloodGroup.equals(user.bloodGroup) : user.bloodGroup != null)
            return false;
        return country != null ? country.equals(user.country) : user.country == null;

    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (photoUrl != null ? photoUrl.hashCode() : 0);
        result = 31 * result + (email != null ? email.hashCode() : 0);
        result = 31 * result + (mobileNum != null ? mobileNum.hashCode() : 0);
        result = 31 * result + (city != null ? city.hashCode() : 0);
        result = 31 * result + (bloodGroup != null ? bloodGroup.hashCode() : 0);
        result = 31 * result + (country != null ? country.hashCode() : 0);
        return result;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobileNum() {
        return mobileNum;
    }

    public void setMobileNum(String mobileNum) {
        this.mobileNum = mobileNum;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getBloodGroup() {
        return bloodGroup;
    }

    public void setBloodGroup(String bloodGroup) {
        this.bloodGroup = bloodGroup;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public int getLifeCount() {
        return lifeCount;
    }

    public void setLifeCount(int lifeCount) {
        this.lifeCount = lifeCount;
    }
}
