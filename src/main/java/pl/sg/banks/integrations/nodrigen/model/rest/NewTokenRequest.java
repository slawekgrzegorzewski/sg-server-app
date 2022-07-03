package pl.sg.banks.integrations.nodrigen.model.rest;

import com.fasterxml.jackson.annotation.JsonProperty;

public class NewTokenRequest {
    private final String secretId;
    private final String secretKey;

    public NewTokenRequest(@JsonProperty("secret_id") String secretId, @JsonProperty("secret_key") String secretKey) {
        this.secretId = secretId;
        this.secretKey = secretKey;
    }

    @JsonProperty("secret_id")
    public String getSecretId() {
        return secretId;
    }

    @JsonProperty("secret_key")
    public String getSecretKey() {
        return secretKey;
    }
}
