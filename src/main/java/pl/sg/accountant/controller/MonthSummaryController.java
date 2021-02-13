package pl.sg.accountant.controller;

import pl.sg.accountant.model.billings.summary.MonthSummaryPiggyBank;
import pl.sg.application.model.ApplicationUser;

import java.math.BigDecimal;
import java.time.YearMonth;
import java.util.Currency;
import java.util.List;
import java.util.Map;

public interface MonthSummaryController {

    Map<YearMonth, Map<Currency, BigDecimal>> getSavingsHistory(ApplicationUser user, int domainId, int forNMonths);

    Map<YearMonth, List<MonthSummaryPiggyBank>> getPiggyBanksHistory(ApplicationUser user, int domainId, int forNMonths);
}
