package com.donate.savelife.firebase;

import android.content.Context;

import com.donate.savelife.analytics.FirebaseAnalytics;
import com.donate.savelife.analytics.FirebaseErrorLogger;
import com.donate.savelife.apputils.AppPreferencesImpl;
import com.donate.savelife.apputils.GsonServiceImpl;
import com.donate.savelife.chats.database.FirebaseChatDatabase;
import com.donate.savelife.core.Config;
import com.donate.savelife.core.analytics.Analytics;
import com.donate.savelife.core.analytics.ErrorLogger;
import com.donate.savelife.core.chats.service.ChatService;
import com.donate.savelife.core.chats.service.PersistedChatService;
import com.donate.savelife.core.country.service.CountryService;
import com.donate.savelife.core.country.service.PersistedCountryService;
import com.donate.savelife.core.login.service.FirebaseLoginService;
import com.donate.savelife.core.login.service.LoginService;
import com.donate.savelife.core.requirement.database.NeedDatabase;
import com.donate.savelife.core.requirement.service.NeedService;
import com.donate.savelife.core.requirement.service.PersistedNeedService;
import com.donate.savelife.core.user.database.HeroDatabase;
import com.donate.savelife.core.user.service.HeroService;
import com.donate.savelife.core.user.service.PersistedHeroService;
import com.donate.savelife.core.user.service.PersistedUserService;
import com.donate.savelife.core.user.service.UserService;
import com.donate.savelife.core.utils.GsonService;
import com.donate.savelife.core.utils.SharedPreferenceService;
import com.donate.savelife.country.database.FirebaseCountryDatabase;
import com.donate.savelife.login.database.FirebaseAuthDatabase;
import com.donate.savelife.requirements.database.FirebaseNeedDatabase;
import com.donate.savelife.rx.FirebaseObservableListeners;
import com.donate.savelife.user.database.FirebaseHeroDatabase;
import com.donate.savelife.user.database.FirebaseUserDatabase;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public enum Dependencies {
    INSTANCE;

    private Analytics analytics;
    private ErrorLogger errorLogger;

    private Config config;
    private AppPreferencesImpl pref;
    private GsonServiceImpl gsonService;
    private FirebaseDatabase firebaseDatabase;
    private FirebaseLoginService loginService;
    private UserService userService;
    private PersistedCountryService countryService;
    private PersistedNeedService needService;
    private PersistedChatService chatService;
    private PersistedHeroService heroService;

    public void init(Context context) {
        if (needsInitialisation()) {
            Context appContext = context.getApplicationContext();

            FirebaseApp firebaseApp = FirebaseApp.initializeApp(appContext, FirebaseOptions.fromResource(appContext), "SaveLife");
            FirebaseAuth firebaseAuth = FirebaseAuth.getInstance(firebaseApp);

            firebaseDatabase = FirebaseDatabase.getInstance(firebaseApp);
            firebaseDatabase.setPersistenceEnabled(true);

            FirebaseObservableListeners firebaseObservableListeners = new FirebaseObservableListeners();

            analytics = new FirebaseAnalytics(com.google.firebase.analytics.FirebaseAnalytics.getInstance(context));

            errorLogger = new FirebaseErrorLogger();

            FirebaseAuthDatabase authDatabase = new FirebaseAuthDatabase(firebaseAuth);

            FirebaseUserDatabase userDatabase = new FirebaseUserDatabase(firebaseDatabase, firebaseObservableListeners);

            NeedDatabase needDatabase = new FirebaseNeedDatabase(firebaseDatabase, firebaseObservableListeners);

            HeroDatabase heroDatabase = new FirebaseHeroDatabase(firebaseDatabase, firebaseObservableListeners);

            FirebaseCountryDatabase countryDatabase = new FirebaseCountryDatabase(firebaseDatabase, firebaseObservableListeners);

            FirebaseChatDatabase chatDatabase = new FirebaseChatDatabase(firebaseDatabase, firebaseObservableListeners);

            loginService = new FirebaseLoginService(authDatabase, userDatabase);

            userService = new PersistedUserService(userDatabase);

            countryService = new PersistedCountryService(countryDatabase);

            needService = new PersistedNeedService(needDatabase, userDatabase);

            chatService = new PersistedChatService(chatDatabase, userDatabase);

            heroService = new PersistedHeroService(heroDatabase, userDatabase, needDatabase);

            config = FirebaseConfig.newInstance().init(errorLogger);
            pref = new AppPreferencesImpl(appContext);
            gsonService = new GsonServiceImpl();

        }
    }

    private boolean needsInitialisation() {
        return  analytics == null ||  errorLogger == null || loginService == null || userService == null;
    }

    public Analytics getAnalytics() {
        return analytics;
    }

    public ErrorLogger getErrorLogger() {
        return errorLogger;
    }

    public Config getConfig() {
        return config;
    }

    public SharedPreferenceService getPreference(){
        return pref;
    }

    public GsonService getGsonService(){
        return gsonService;
    }

    public LoginService getLoginService() {
        return loginService;
    }

    public UserService getUserService() {
        return userService;
    }

    public CountryService getCountryService(){
        return countryService;
    }

    public NeedService getNeedService() {
        return needService;
    }

    public ChatService getChatService() {
        return chatService;
    }

    public HeroService getHeroService(){
        return heroService;
    }



}
