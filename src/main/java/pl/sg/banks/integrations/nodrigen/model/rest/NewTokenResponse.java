package pl.sg.banks.integrations.nodrigen.model.rest;

import com.fasterxml.jackson.annotation.JsonProperty;

public class NewTokenResponse {

    private final String access;

    private final Long accessExpires;

    private final String refresh;

    private final Long refreshExpires;

    public NewTokenResponse(@JsonProperty("access") String access,
                            @JsonProperty("access_expires") Long accessExpires,
                            @JsonProperty("refresh") String refresh,
                            @JsonProperty("refresh_expires") Long refreshExpires) {
        this.access = access;
        this.accessExpires = accessExpires;
        this.refresh = refresh;
        this.refreshExpires = refreshExpires;
    }

    @JsonProperty("access")
    public String getAccess() {
        return access;
    }

    @JsonProperty("access_expires")
    public Long getAccessExpires() {
        return accessExpires;
    }

    @JsonProperty("refresh")
    public String getRefresh() {
        return refresh;
    }

    @JsonProperty("refresh_expires")
    public Long getRefreshExpires() {
        return refreshExpires;
    }
}
