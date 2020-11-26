package pl.sg.accountant.model.billings.summary;

import java.math.BigDecimal;
import java.util.Currency;

public class MonthSummaryPiggyBank {
    public final int id;
    public final String name;
    public final String description;
    public final BigDecimal balance;
    public final Currency currency;
    public final boolean savings;
    public final BigDecimal monthlyTopUp;

    public MonthSummaryPiggyBank(int id, String name, String description, BigDecimal balance, Currency currency, boolean savings, BigDecimal monthlyTopUp) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.balance = balance;
        this.currency = currency;
        this.savings = savings;
        this.monthlyTopUp = monthlyTopUp;
    }
}
