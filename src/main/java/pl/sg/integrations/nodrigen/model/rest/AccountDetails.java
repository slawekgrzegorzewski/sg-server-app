package pl.sg.integrations.nodrigen.model.rest;

import com.fasterxml.jackson.annotation.JsonProperty;

public class AccountDetails {
    private final Account account;

    public AccountDetails(@JsonProperty("account") Account account) {
        this.account = account;
    }

    @JsonProperty("account")
    public Account getAccount() {
        return account;
    }
}
