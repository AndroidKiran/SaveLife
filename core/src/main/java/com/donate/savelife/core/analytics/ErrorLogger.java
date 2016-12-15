package com.donate.savelife.core.analytics;

public interface ErrorLogger {

    void reportError(Throwable throwable, Object... args);

}
