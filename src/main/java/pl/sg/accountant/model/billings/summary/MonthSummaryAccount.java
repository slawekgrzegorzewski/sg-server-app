package pl.sg.accountant.model.billings.summary;

import java.math.BigDecimal;
import java.util.Currency;

public class MonthSummaryAccount {
    public final int id;
    public final String name;
    public final Currency currency;
    public final BigDecimal currentBalance;
    public final int lastTransactionIdIncludedInBalance;


    public MonthSummaryAccount(int id, String name, Currency currency, BigDecimal currentBalance, int lastTransactionIdIncludedInBalance) {
        this.id = id;
        this.name = name;
        this.currency = currency;
        this.currentBalance = currentBalance;
        this.lastTransactionIdIncludedInBalance = lastTransactionIdIncludedInBalance;
    }
}
