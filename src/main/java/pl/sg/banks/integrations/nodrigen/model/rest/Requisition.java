package pl.sg.banks.integrations.nodrigen.model.rest;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.net.URL;
import java.time.LocalDateTime;
import java.util.UUID;

public class Requisition {

    private final UUID id;
    private final LocalDateTime created;
    private final String redirect;
    private final String status;
    private final String institutionId;
    private final UUID agreement;
    private final String reference;
    private final UUID[] accounts;
    private final String userLanguage;
    private final URL link;
    private final String ssn;
    private final boolean accountSelection;
    private final boolean redirectImmediate;

    public Requisition(
            @JsonProperty("id") UUID id,
            @JsonProperty("created") LocalDateTime created,
            @JsonProperty("redirect") String redirect,
            @JsonProperty("status") String status,
            @JsonProperty("institution_id") String institutionId,
            @JsonProperty("agreement") UUID agreement,
            @JsonProperty("reference") String reference,
            @JsonProperty("accounts") UUID[] accounts,
            @JsonProperty("user_language") String userLanguage,
            @JsonProperty("link") URL link,
            @JsonProperty("ssn") String ssn,
            @JsonProperty("account_selection") boolean accountSelection,
            @JsonProperty("redirect_immediate") boolean redirectImmediate) {
        this.id = id;
        this.created = created;
        this.redirect = redirect;
        this.status = status;
        this.institutionId = institutionId;
        this.agreement = agreement;
        this.reference = reference;
        this.accounts = accounts;
        this.userLanguage = userLanguage;
        this.link = link;
        this.ssn = ssn;
        this.accountSelection = accountSelection;
        this.redirectImmediate = redirectImmediate;
    }

    @JsonProperty("id")
    public UUID getId() {
        return id;
    }

    @JsonProperty("created")
    public LocalDateTime getCreated() {
        return created;
    }

    @JsonProperty("redirect")
    public String getRedirect() {
        return redirect;
    }

    @JsonProperty("status")
    public String getStatus() {
        return status;
    }

    @JsonProperty("institution_id")
    public String getInstitutionId() {
        return institutionId;
    }

    @JsonProperty("agreement")
    public UUID getAgreement() {
        return agreement;
    }

    @JsonProperty("reference")
    public String getReference() {
        return reference;
    }

    @JsonProperty("accounts")
    public UUID[] getAccounts() {
        return accounts;
    }

    @JsonProperty("user_language")
    public String getUserLanguage() {
        return userLanguage;
    }

    @JsonProperty("link")
    public URL getLink() {
        return link;
    }

    @JsonProperty("ssn")
    public String getSsn() {
        return ssn;
    }

    @JsonProperty("account_selection")
    public boolean isAccountSelection() {
        return accountSelection;
    }

    @JsonProperty("redirect_immediate")
    public boolean isRedirectImmediate() {
        return redirectImmediate;
    }
}
