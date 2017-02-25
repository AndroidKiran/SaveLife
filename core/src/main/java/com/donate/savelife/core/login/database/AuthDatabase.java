package com.donate.savelife.core.login.database;


import com.donate.savelife.core.login.data.model.Authentication;

import rx.Observable;

public interface AuthDatabase {

    Observable<Authentication> readAuthentication();

    Observable<Authentication> loginWithGoogle(String idToken);

    void signOut();

}
