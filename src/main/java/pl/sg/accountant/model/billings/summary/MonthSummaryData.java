package pl.sg.accountant.model.billings.summary;

import org.apache.xmlbeans.impl.store.Cur;
import pl.sg.accountant.model.accounts.Account;
import pl.sg.accountant.model.billings.PiggyBank;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class MonthSummaryData {
    private Map<Currency, BigDecimal> savings;
    private List<MonthSummaryAccount> accounts;
    private List<MonthSummaryPiggyBank> piggyBanks;

    public static MonthSummaryData of(List<Account> accounts, List<PiggyBank> piggyBanks) {
        MonthSummaryData result = new MonthSummaryData();
        result.accounts = accounts.stream()
                .map(a ->
                        new MonthSummaryAccount((int) (long) a.getId(), a.getName(), a.getCurrency(), a.getCurrentBalance().getNumber().numberValue(BigDecimal.class),
                                a.getLastTransactionIncludedInBalance() == null ? null : a.getLastTransactionIncludedInBalance().getId())
                )
                .collect(Collectors.toList());
        result.piggyBanks = piggyBanks.stream()
                .map(pg ->
                        new MonthSummaryPiggyBank(pg.getId(), pg.getName(), pg.getDescription(), pg.getBalance(),
                                pg.getCurrency(), pg.isSavings(), pg.getMonthlyTopUp()))
                .collect(Collectors.toList());
        result.savings = new HashMap<>();
        result.piggyBanks.stream()
                .filter(mspg -> mspg.savings)
                .forEach(mspg -> result.savings.compute(mspg.currency, (currency, total) -> total == null ?
                        mspg.balance
                        : total.add(mspg.balance))
                );
        return result;
    }

    public Map<Currency, BigDecimal> getSavings() {
        return savings;
    }

    public List<MonthSummaryAccount> getAccounts() {
        return accounts;
    }

    public List<MonthSummaryPiggyBank> getPiggyBanks() {
        return piggyBanks;
    }
}

