package com.donate.savelife.core.login.service;

import com.donate.savelife.core.database.DatabaseResult;
import com.donate.savelife.core.login.data.model.Authentication;
import com.donate.savelife.core.login.database.AuthDatabase;
import com.donate.savelife.core.user.data.model.User;
import com.donate.savelife.core.user.database.UserDatabase;
import com.jakewharton.rxrelay.BehaviorRelay;

import rx.Observable;
import rx.functions.Action1;
import rx.functions.Func0;
import rx.functions.Func1;

public class FirebaseLoginService implements LoginService {

    private final AuthDatabase authDatabase;
    private final UserDatabase userDatabase;
    private final BehaviorRelay<Authentication> authRelay;

    public FirebaseLoginService(AuthDatabase authDatabase, UserDatabase userDatabase) {
        this.authDatabase = authDatabase;
        this.userDatabase = userDatabase;
        authRelay = BehaviorRelay.create();
    }

    @Override
    public Observable<Authentication> getAuthentication() {
        return authRelay
                .startWith(initRelay());
    }

    private Observable<Authentication> initRelay() {
        return Observable.defer(new Func0<Observable<Authentication>>() {
            @Override
            public Observable<Authentication> call() {
                if (authRelay.hasValue() && authRelay.getValue().isSuccess()) {
                    return Observable.empty();
                } else {
                    return fetchUser();
                }
            }
        });
    }

    private Observable<Authentication> fetchUser() {
        return authDatabase.readAuthentication()
                .doOnNext(authRelay)
                .ignoreElements();
    }

    @Override
    public void loginWithGoogle(String idToken) {
        authDatabase.loginWithGoogle(idToken)
                .subscribe(new Action1<Authentication>() {
                    @Override
                    public void call(final Authentication authentication) {
                        if (authentication.isSuccess()) {
                            userDatabase.readUserFrom(UserDatabase.VALUE_EVENT_TYPE, authentication.getUser().getId())
                                    .map(toUserDatabaseResult())
                                    .subscribe(new Action1<DatabaseResult<User>>() {
                                        @Override
                                        public void call(DatabaseResult<User> userDatabaseResult) {
                                            if (!userDatabaseResult.isSuccess()) {
                                                userDatabase.writeCurrentUser(authentication.getUser());
                                            } else {

                                            }
                                            authRelay.call(authentication);
                                        }
                                    });

                        }
                    }
                });
    }

    @Override
    public void signOut() {
        authDatabase.signOut();
    }

    private Func1<User, DatabaseResult<User>> toUserDatabaseResult(){
        return new Func1<User, DatabaseResult<User>>() {
            @Override
            public DatabaseResult<User> call(User user) {
                return new DatabaseResult<User>(user);
            }
        };
    }
}
