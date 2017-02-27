package com.donate.savelife.core.user.data.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;
import java.util.ListIterator;

public class Users implements Parcelable{

    private final List<User> users;

    public Users(List<User> users) {
        this.users = users;
    }

    protected Users(Parcel in) {
        users = in.createTypedArrayList(User.CREATOR);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(users);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Users> CREATOR = new Creator<Users>() {
        @Override
        public Users createFromParcel(Parcel in) {
            return new Users(in);
        }

        @Override
        public Users[] newArray(int size) {
            return new Users[size];
        }
    };

    public List<User> getUsers() {
        return users;
    }

    public int size() {
        return users.size() ;
    }

    public User getUserAt(int position) {
        return users.get(position);
    }

    public boolean remove(User user){
        ListIterator<User> userListIterator = users.listIterator();
        while (userListIterator.hasNext()){
            User user1 = userListIterator.next();
            if (user.getId().equals(user1.getId())){
                remove(user);
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Users users1 = (Users) o;

        return users.equals(users1.users);

    }

    @Override
    public int hashCode() {
        return users.hashCode();
    }

    @Override
    public String toString() {
        return "Users{" +
                "users=" + users +
                '}';
    }
}
