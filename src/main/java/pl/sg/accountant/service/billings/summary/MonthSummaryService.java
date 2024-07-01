package pl.sg.accountant.service.billings.summary;

import pl.sg.accountant.model.billings.summary.MonthSummaryPiggyBank;
import pl.sg.application.model.Domain;

import java.math.BigDecimal;
import java.time.YearMonth;
import java.util.Currency;
import java.util.List;
import java.util.Map;

public interface MonthSummaryService {

    Map<YearMonth, Map<Currency, BigDecimal>> getSavingsHistory(Domain domain, int forNMonths);

    Map<YearMonth, List<MonthSummaryPiggyBank>> getPiggyBanksHistory(Domain domain, int forNMonths);
}