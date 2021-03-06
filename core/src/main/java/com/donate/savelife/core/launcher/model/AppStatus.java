package com.donate.savelife.core.launcher.model;

/**
 * Created by ravi on 02/02/17.
 */

public class AppStatus
{
    private String id;
    private boolean versionDeprecated;
    private boolean updateAvailable;
    private boolean isError;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public boolean isVersionDeprecated() {
        return versionDeprecated;
    }

    public void setVersionDeprecated(boolean versionDeprecated) {
        this.versionDeprecated = versionDeprecated;
    }

    public boolean isUpdateAvailable() {
        return updateAvailable;
    }

    public void setUpdateAvailable(boolean updateAvailable) {
        this.updateAvailable = updateAvailable;
    }

    public boolean isError() {
        return isError;
    }

    public void setError(boolean error) {
        isError = error;
    }
}
