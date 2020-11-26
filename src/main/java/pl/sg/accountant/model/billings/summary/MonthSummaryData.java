package pl.sg.accountant.model.billings.summary;

import pl.sg.accountant.model.accounts.Account;
import pl.sg.accountant.model.billings.PiggyBank;

import java.util.List;
import java.util.stream.Collectors;

public class MonthSummaryData {
    private List<MonthSummaryAccount> accounts;
    private List<MonthSummaryPiggyBank> piggyBanks;

    public static MonthSummaryData of(List<Account> accounts, List<PiggyBank> piggyBanks) {
        MonthSummaryData result = new MonthSummaryData();
        result.accounts = accounts.stream()
                .map(a ->
                        new MonthSummaryAccount(a.getId(), a.getName(), a.getCurrency(), a.getCurrentBalance(),
                                a.getLastTransactionIncludedInBalance().getId())
                )
                .collect(Collectors.toList());
        result.piggyBanks = piggyBanks.stream()
                .map(pg ->
                        new MonthSummaryPiggyBank(pg.getId(), pg.getName(), pg.getDescription(), pg.getBalance(),
                                pg.getCurrency(), pg.isSavings(), pg.getMonthlyTopUp()))
                .collect(Collectors.toList());
        return result;
    }

    public List<MonthSummaryAccount> getAccounts() {
        return accounts;
    }

    public List<MonthSummaryPiggyBank> getPiggyBanks() {
        return piggyBanks;
    }
}

