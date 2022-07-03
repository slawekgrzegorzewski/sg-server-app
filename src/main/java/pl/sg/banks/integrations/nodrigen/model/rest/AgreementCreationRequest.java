package pl.sg.banks.integrations.nodrigen.model.rest;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Arrays;

public class AgreementCreationRequest {

    private final int maxHistoricalDays;
    private final int accessValidForDays;
    private final String[] accessScope;
    private final String institutionId;

    public AgreementCreationRequest(
            @JsonProperty("max_historical_days") int maxHistoricalDays,
            @JsonProperty("access_valid_for_days") int accessValidForDays,
            @JsonProperty("access_scope") String[] accessScope,
            @JsonProperty("institution_id") String institutionId) {
        this.maxHistoricalDays = maxHistoricalDays;
        this.accessValidForDays = accessValidForDays;
        this.accessScope = accessScope;
        this.institutionId = institutionId;
    }

    @JsonProperty("max_historical_days")
    public int getMaxHistoricalDays() {
        return maxHistoricalDays;
    }

    @JsonProperty("access_valid_for_days")
    public int getAccessValidForDays() {
        return accessValidForDays;
    }

    @JsonProperty("access_scope")
    public String[] getAccessScope() {
        return accessScope;
    }

    @JsonProperty("institution_id")
    public String getInstitutionId() {
        return institutionId;
    }

    @Override
    public String toString() {
        return "AgreementCreationRequest{" +
                "maxHistoricalDays=" + maxHistoricalDays +
                ", accessValidForDays=" + accessValidForDays +
                ", accessScope=" + Arrays.toString(accessScope) +
                ", institutionId='" + institutionId + '\'' +
                '}';
    }
}
