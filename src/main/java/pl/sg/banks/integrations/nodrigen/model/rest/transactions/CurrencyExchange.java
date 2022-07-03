package pl.sg.banks.integrations.nodrigen.model.rest.transactions;

import pl.sg.banks.integrations.nodrigen.model.rest.balances.Amount;

import java.math.BigDecimal;
import java.util.Currency;

public class CurrencyExchange {
    public BigDecimal exchangeRate;
    public Amount instructedAmount;
    public Currency sourceCurrency;
    public Currency targetCurrency;
    public Currency unitCurrency;
}
