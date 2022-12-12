package pl.sg.integrations.nodrigen.model.transcations;

import pl.sg.integrations.nodrigen.model.balances.NodrigenAmount;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Currency;

@Embeddable
public class NodrigenCurrencyExchange {
    public BigDecimal exchangeRate;
    @Embedded
    public NodrigenAmount instructedAmount;
    public Currency sourceCurrency;
    public Currency targetCurrency;
    public Currency unitCurrency;

    public BigDecimal getExchangeRate() {
        return exchangeRate;
    }

    public NodrigenCurrencyExchange setExchangeRate(BigDecimal exchangeRate) {
        this.exchangeRate = exchangeRate;
        return this;
    }

    public NodrigenAmount getInstructedAmount() {
        return instructedAmount;
    }

    public NodrigenCurrencyExchange setInstructedAmount(NodrigenAmount instructedAmount) {
        this.instructedAmount = instructedAmount;
        return this;
    }

    public Currency getSourceCurrency() {
        return sourceCurrency;
    }

    public NodrigenCurrencyExchange setSourceCurrency(Currency sourceCurrency) {
        this.sourceCurrency = sourceCurrency;
        return this;
    }

    public Currency getTargetCurrency() {
        return targetCurrency;
    }

    public NodrigenCurrencyExchange setTargetCurrency(Currency targetCurrency) {
        this.targetCurrency = targetCurrency;
        return this;
    }

    public Currency getUnitCurrency() {
        return unitCurrency;
    }

    public NodrigenCurrencyExchange setUnitCurrency(Currency unitCurrency) {
        this.unitCurrency = unitCurrency;
        return this;
    }
}
