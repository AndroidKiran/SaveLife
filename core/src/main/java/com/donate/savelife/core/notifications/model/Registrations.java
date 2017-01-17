package com.donate.savelife.core.notifications.model;

import java.util.ArrayList;

/**
 * Created by ravi on 09/01/17.
 */

public class Registrations {

    private ArrayList<FcmRegistration> registrationList;

    public Registrations(ArrayList<FcmRegistration> registrationList){
        this.registrationList = registrationList;
    }

    public int size(){
        return registrationList.size();
    }

    public FcmRegistration getRegistrationId(int position){
        return registrationList.get(position);
    }

    public ArrayList<FcmRegistration> getRegistrationList(){
        return this.registrationList;
    }
}
