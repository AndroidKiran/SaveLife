package com.donate.savelife.core.notifications.model;

import java.util.ArrayList;

/**
 * Created by ravi on 09/01/17.
 */

public class Registrations {

    private ArrayList<String> registrationList;

    public Registrations(ArrayList<String> registrationList){
        this.registrationList = registrationList;
    }

    public int size(){
        return registrationList.size();
    }

    public String getRegistrationId(int position){
        return registrationList.get(position);
    }
}
