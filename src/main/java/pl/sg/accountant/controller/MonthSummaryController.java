package pl.sg.accountant.controller;

import org.springframework.http.ResponseEntity;
import pl.sg.accountant.model.billings.summary.MonthSummaryPiggyBank;
import pl.sg.application.model.ApplicationUser;

import java.math.BigDecimal;
import java.time.YearMonth;
import java.util.Currency;
import java.util.List;
import java.util.Map;

public interface MonthSummaryController {

    ResponseEntity<Map<YearMonth, Map<Currency, BigDecimal>>> getSavingsHistory(int forNMonths, ApplicationUser user);

    ResponseEntity<Map<YearMonth, List<MonthSummaryPiggyBank>>> getPiggyBanksHistory(int forNMonths, ApplicationUser user);
}
