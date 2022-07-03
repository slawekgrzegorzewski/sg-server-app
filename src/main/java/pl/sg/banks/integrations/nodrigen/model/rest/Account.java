package pl.sg.banks.integrations.nodrigen.model.rest;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Currency;

public class Account {
    private final String resourceId;
    private final String iban;
    private final Currency currency;
    private final String ownerName;
    private final String product;
    private final String bic;

    public Account(@JsonProperty("resourceId") String resourceId,
                   @JsonProperty("iban") String iban,
                   @JsonProperty("currency") Currency currency,
                   @JsonProperty("ownerName") String ownerName,
                   @JsonProperty("product") String product,
                   @JsonProperty("bic") String bic) {
        this.resourceId = resourceId;
        this.iban = iban;
        this.currency = currency;
        this.ownerName = ownerName;
        this.product = product;
        this.bic = bic;
    }

    @JsonProperty("resourceId")
    public String getResourceId() {
        return resourceId;
    }

    @JsonProperty("iban")
    public String getIban() {
        return iban;
    }

    @JsonProperty("currency")
    public Currency getCurrency() {
        return currency;
    }

    @JsonProperty("ownerName")
    public String getOwnerName() {
        return ownerName;
    }

    @JsonProperty("product")
    public String getProduct() {
        return product;
    }

    @JsonProperty("bic")
    public String getBic() {
        return bic;
    }
}
