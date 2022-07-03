package pl.sg.integrations.nodrigen.model.rest;

import com.fasterxml.jackson.annotation.JsonProperty;

public class RefreshTokenResponse {
    private final String access;
    private final Long accessExpires;

    public RefreshTokenResponse(@JsonProperty("access") String access,
                                @JsonProperty("access_expires") Long accessExpires) {
        this.access = access;
        this.accessExpires = accessExpires;
    }

    @JsonProperty("access")
    public String getAccess() {
        return access;
    }

    @JsonProperty("access_expires")
    public Long getAccessExpires() {
        return accessExpires;
    }
}
