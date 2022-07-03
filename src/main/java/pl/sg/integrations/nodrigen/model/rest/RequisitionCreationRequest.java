package pl.sg.integrations.nodrigen.model.rest;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.net.URL;
import java.util.UUID;

public class RequisitionCreationRequest {

    private final URL redirect;
    private final String institutionId;
    private final UUID agreement;
    private final String reference;
    private final String userLanguage;
    private final boolean accountSelection;
    private final boolean redirectImmediate;

    public RequisitionCreationRequest(
            @JsonProperty("redirect") URL redirect,
            @JsonProperty("institution_id") String institutionId,
            @JsonProperty("agreement") UUID agreement,
            @JsonProperty("reference") String reference,
            @JsonProperty("user_language") String userLanguage,
            @JsonProperty("account_selection") boolean accountSelection,
            @JsonProperty("redirect_immediate") boolean redirectImmediate) {
        this.redirect = redirect;
        this.institutionId = institutionId;
        this.agreement = agreement;
        this.reference = reference;
        this.userLanguage = userLanguage;
        this.accountSelection = accountSelection;
        this.redirectImmediate = redirectImmediate;
    }

    @JsonProperty("redirect")
    public URL getRedirect() {
        return redirect;
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

    @JsonProperty("user_language")
    public String getUserLanguage() {
        return userLanguage;
    }

    @JsonProperty("account_selection")
    public boolean isAccountSelection() {
        return accountSelection;
    }

    @JsonProperty("redirect_immediate")
    public boolean isRedirectImmediate() {
        return redirectImmediate;
    }

    @Override
    public String toString() {
        return "RequisitionCreationRequest{" +
                "redirect=" + redirect +
                ", institutionId='" + institutionId + '\'' +
                ", agreement=" + agreement +
                ", reference='" + reference + '\'' +
                ", userLanguage='" + userLanguage + '\'' +
                ", accountSelection=" + accountSelection +
                ", redirectImmediate=" + redirectImmediate +
                '}';
    }
}
