package com.donate.savelife.core.login.service;


import com.donate.savelife.core.login.data.model.Authentication;

import rx.Observable;

public interface LoginService {

    Observable<Authentication> getAuthentication();

    void loginWithGoogle(String idToken);

}
