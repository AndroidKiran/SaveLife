package com.donate.savelife.core.chats.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.donate.savelife.core.UniqueList;

/**
 * Created by ravi on 27/11/16.
 */

public class Test implements Parcelable{

    private UniqueList<Message> messages;

    protected Test(Parcel in) {
        messages = (UniqueList<Message>) in.createTypedArrayList(Message.CREATOR);
    }

    public static final Creator<Test> CREATOR = new Creator<Test>() {
        @Override
        public Test createFromParcel(Parcel in) {
            return new Test(in);
        }

        @Override
        public Test[] newArray(int size) {
            return new Test[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeTypedList(messages);
    }
}
