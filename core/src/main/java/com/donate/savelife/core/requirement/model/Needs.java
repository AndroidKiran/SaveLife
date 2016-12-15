package com.donate.savelife.core.requirement.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.donate.savelife.core.UniqueList;

import java.util.ArrayList;

public class Needs implements Parcelable{

    private final ArrayList<Need> needs;

    public Needs(UniqueList<Need> needs) {
        this.needs = needs;
    }

    protected Needs(Parcel in) {
        needs = in.createTypedArrayList(Need.CREATOR);
    }

    public static final Creator<Needs> CREATOR = new Creator<Needs>() {
        @Override
        public Needs createFromParcel(Parcel in) {
            return new Needs(in);
        }

        @Override
        public Needs[] newArray(int size) {
            return new Needs[size];
        }
    };

    public ArrayList<Need> getNeeds() {
        return this.needs;
    }

    public int size() {
        return needs.size() ;
    }

    public Need getNeed(int position) {
        return needs.get(position);
    }

    public void addAll(ArrayList<Need> needs){
        this.needs.addAll(needs);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Needs needs1 = (Needs) o;

        return needs.equals(needs1.needs);

    }

    @Override
    public int hashCode() {
        return needs.hashCode();
    }

    @Override
    public String toString() {
        return "Users{" +
                "users=" + needs +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeTypedList(needs);
    }
}
