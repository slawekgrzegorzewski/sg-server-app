package pl.sg.integrations.nodrigen.transport;

import java.net.URL;

public class NodrigenPermissionRequest {
    private String institutionId;
    private int maxHistoricalDays;
    private URL redirect;
    private String userLanguage;

    public NodrigenPermissionRequest() {
    }

    public NodrigenPermissionRequest(String institutionId, int maxHistoricalDays, URL redirect, String userLanguage) {
        this.institutionId = institutionId;
        this.maxHistoricalDays = maxHistoricalDays;
        this.redirect = redirect;
        this.userLanguage = userLanguage;
    }

    public String getInstitutionId() {
        return institutionId;
    }

    public NodrigenPermissionRequest setInstitutionId(String institutionId) {
        this.institutionId = institutionId;
        return this;
    }

    public int getMaxHistoricalDays() {
        return maxHistoricalDays;
    }

    public NodrigenPermissionRequest setMaxHistoricalDays(int maxHistoricalDays) {
        this.maxHistoricalDays = maxHistoricalDays;
        return this;
    }

    public URL getRedirect() {
        return redirect;
    }

    public NodrigenPermissionRequest setRedirect(URL redirect) {
        this.redirect = redirect;
        return this;
    }

    public String getUserLanguage() {
        return userLanguage;
    }

    public NodrigenPermissionRequest setUserLanguage(String userLanguage) {
        this.userLanguage = userLanguage;
        return this;
    }
}
