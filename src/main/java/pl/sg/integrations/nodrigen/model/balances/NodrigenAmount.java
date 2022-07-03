package pl.sg.integrations.nodrigen.model.balances;

import javax.persistence.Embeddable;
import java.math.BigDecimal;
import java.util.Currency;

@Embeddable
public class NodrigenAmount {
    public BigDecimal amount;
    public Currency currency;

    public BigDecimal getAmount() {
        return amount;
    }

    public NodrigenAmount setAmount(BigDecimal amount) {
        this.amount = amount;
        return this;
    }

    public Currency getCurrency() {
        return currency;
    }

    public NodrigenAmount setCurrency(Currency currency) {
        this.currency = currency;
        return this;
    }
}
