package com.donate.savelife.apputils;


import com.donate.savelife.core.utils.GsonService;
import com.donate.savelife.core.user.data.model.User;
import com.google.gson.Gson;

/**
 * Created by ravi on 12/09/16.
 */
public class GsonServiceImpl implements GsonService {

    private final Gson gson;

    public GsonServiceImpl(){
        gson = new Gson();
    }

    @Override
    public String toString(User user) {
        return gson.toJson(user);
    }

    @Override
    public User toUser(String json) {
        return gson.fromJson(json, User.class);
    }
}
