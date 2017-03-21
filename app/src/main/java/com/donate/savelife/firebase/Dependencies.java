package com.donate.savelife.firebase;

import android.content.Context;

import com.donate.savelife.analytics.FirebaseAnalytics;
import com.donate.savelife.analytics.FirebaseErrorLogger;
import com.donate.savelife.apputils.AppPreferencesImpl;
import com.donate.savelife.apputils.GsonServiceImpl;
import com.donate.savelife.chats.database.FirebaseChatDatabase;
import com.donate.savelife.core.analytics.Analytics;
import com.donate.savelife.core.analytics.ErrorLogger;
import com.donate.savelife.core.chats.database.ChatDatabase;
import com.donate.savelife.core.chats.service.ChatService;
import com.donate.savelife.core.chats.service.PersistedChatService;
import com.donate.savelife.core.country.database.CountryDatabase;
import com.donate.savelife.core.country.service.CountryService;
import com.donate.savelife.core.country.service.PersistedCountryService;
import com.donate.savelife.core.launcher.database.AppStatusDatabase;
import com.donate.savelife.core.launcher.service.PersistedAppStatusService;
import com.donate.savelife.core.login.database.AuthDatabase;
import com.donate.savelife.core.login.service.FirebaseLoginService;
import com.donate.savelife.core.login.service.LoginService;
import com.donate.savelife.core.notifications.database.NotificationQueueDatabase;
import com.donate.savelife.core.notifications.database.NotificationRegistrationDatabase;
import com.donate.savelife.core.notifications.service.NotificationQueueService;
import com.donate.savelife.core.notifications.service.NotificationRegistrationService;
import com.donate.savelife.core.notifications.service.PersistedNotificationQueueService;
import com.donate.savelife.core.notifications.service.PersistedNotificationRegistrationService;
import com.donate.savelife.core.requirement.database.NeedDatabase;
import com.donate.savelife.core.requirement.service.NeedService;
import com.donate.savelife.core.requirement.service.PersistedNeedService;
import com.donate.savelife.core.user.database.HeroDatabase;
import com.donate.savelife.core.user.database.UserDatabase;
import com.donate.savelife.core.user.service.HeroService;
import com.donate.savelife.core.user.service.PersistedHeroService;
import com.donate.savelife.core.user.service.PersistedUserService;
import com.donate.savelife.core.user.service.UserService;
import com.donate.savelife.core.utils.GsonService;
import com.donate.savelife.core.utils.SharedPreferenceService;
import com.donate.savelife.country.database.FirebaseCountryDatabase;
import com.donate.savelife.launcher.database.FirebaseAppStatusDatabase;
import com.donate.savelife.login.database.FirebaseAuthDatabase;
import com.donate.savelife.notifications.database.FirebaseNotificationRegistrationDatabase;
import com.donate.savelife.notifications.database.FirebaseNotificatonQueueDatabase;
import com.donate.savelife.requirements.database.FirebaseNeedDatabase;
import com.donate.savelife.rx.FirebaseObservableListeners;
import com.donate.savelife.user.database.FirebaseHeroDatabase;
import com.donate.savelife.user.database.FirebaseUserDatabase;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.FirebaseMessaging;

public enum Dependencies {
    INSTANCE;

    private Analytics analytics;
    private ErrorLogger errorLogger;

    private AppPreferencesImpl pref;
    private GsonServiceImpl gsonService;
    private FirebaseDatabase firebaseDatabase;
    private LoginService loginService;
    private UserService userService;
    private CountryService countryService;
    private NeedService needService;
    private ChatService chatService;
    private HeroService heroService;
    private NotificationRegistrationService notificationRegistrationsService;
    private FirebaseMessaging firebaseMessageInstance;
    private NotificationQueueService notificationQueueService;
    private PersistedAppStatusService appStatusService;

    public void init(Context context) {
        if (needsInitialisation()) {
            Context appContext = context.getApplicationContext();

            FirebaseApp firebaseApp = FirebaseApp.initializeApp(appContext, FirebaseOptions.fromResource(appContext), "SaveLife");
            FirebaseAuth firebaseAuth = FirebaseAuth.getInstance(firebaseApp);

            firebaseDatabase = FirebaseDatabase.getInstance(firebaseApp);
            firebaseDatabase.setPersistenceEnabled(true);

            firebaseMessageInstance = FirebaseMessaging.getInstance();

            FirebaseObservableListeners firebaseObservableListeners = new FirebaseObservableListeners();

            analytics = new FirebaseAnalytics(com.google.firebase.analytics.FirebaseAnalytics.getInstance(context));

            errorLogger = new FirebaseErrorLogger();

            AuthDatabase authDatabase = new FirebaseAuthDatabase(firebaseAuth);

            UserDatabase userDatabase = new FirebaseUserDatabase(firebaseDatabase, firebaseObservableListeners);

            NeedDatabase needDatabase = new FirebaseNeedDatabase(firebaseDatabase, firebaseObservableListeners);

            HeroDatabase heroDatabase = new FirebaseHeroDatabase(firebaseDatabase, firebaseObservableListeners);

            CountryDatabase countryDatabase = new FirebaseCountryDatabase(firebaseDatabase, firebaseObservableListeners);

            ChatDatabase chatDatabase = new FirebaseChatDatabase(firebaseDatabase, firebaseObservableListeners);

            NotificationRegistrationDatabase firebaseNotificationRegistrationDatabase = new FirebaseNotificationRegistrationDatabase(firebaseDatabase, firebaseObservableListeners);

            NotificationQueueDatabase firebaseNotificatonQueueDatabase = new FirebaseNotificatonQueueDatabase(firebaseDatabase, firebaseObservableListeners);

            AppStatusDatabase appStatusDatabase = new FirebaseAppStatusDatabase(firebaseDatabase, firebaseObservableListeners);

            loginService = new FirebaseLoginService(authDatabase, userDatabase);

            userService = new PersistedUserService(userDatabase);

            countryService = new PersistedCountryService(countryDatabase);

            needService = new PersistedNeedService(needDatabase, userDatabase);

            chatService = new PersistedChatService(chatDatabase, userDatabase, heroDatabase);

            heroService = new PersistedHeroService(heroDatabase, userDatabase);

            notificationRegistrationsService = new PersistedNotificationRegistrationService(firebaseNotificationRegistrationDatabase, chatDatabase, userDatabase);

            notificationQueueService = new PersistedNotificationQueueService(firebaseNotificatonQueueDatabase);

            appStatusService = new PersistedAppStatusService(appStatusDatabase);

            pref = new AppPreferencesImpl(appContext);

            gsonService = new GsonServiceImpl();

        }
    }

    private boolean needsInitialisation() {
        return  analytics == null ||  errorLogger == null || loginService == null || userService == null
                || loginService == null || countryService == null || needService == null || chatService == null
                || heroService == null || notificationQueueService == null || notificationRegistrationsService == null
                || appStatusService == null;
    }

    public Analytics getAnalytics() {
        return analytics;
    }

    public ErrorLogger getErrorLogger() {
        return errorLogger;
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

    public NotificationRegistrationService  getNotificationRegistrationService(){
        return notificationRegistrationsService;
    }

    public FirebaseMessaging getFirebaseMessageInstance(){
        return firebaseMessageInstance;
    }

    public NotificationQueueService getNotificationQueueService(){
        return notificationQueueService;
    }

    public PersistedAppStatusService getAppStatusService() {
        return appStatusService;
    }

    public FirebaseDatabase getFirebaseDatabase(){
        return firebaseDatabase;
    }
}
