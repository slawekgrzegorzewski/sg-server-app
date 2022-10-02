package pl.sg.integrations.nodrigen.model.rest;

import com.fasterxml.jackson.annotation.JsonProperty;

public class NodrigenInstitution {
    private final String id;
    private final String name;
    private final String bic;
    private final int transactionTotalDays;
    private final String[] countries;
    private final String logo;

    public NodrigenInstitution(
            @JsonProperty("id") String id,
            @JsonProperty("name") String name,
            @JsonProperty("bic") String bic,
            @JsonProperty("transaction_total_days") int transactionTotalDays,
            @JsonProperty("countries") String[] countries,
            @JsonProperty("logo") String logo) {
        this.id = id;
        this.name = name;
        this.bic = bic;
        this.transactionTotalDays = transactionTotalDays;
        this.countries = countries;
        this.logo = logo;
    }

    @JsonProperty("id")
    public String getId() {
        return id;
    }

    @JsonProperty("name")
    public String getName() {
        return name;
    }

    @JsonProperty("bic")
    public String getBic() {
        return bic;
    }

    @JsonProperty("transaction_total_days")
    public int getTransactionTotalDays() {
        return transactionTotalDays;
    }

    @JsonProperty("countries")
    public String[] getCountries() {
        return countries;
    }

    @JsonProperty("logo")
    public String getLogo() {
        return logo;
    }
}
