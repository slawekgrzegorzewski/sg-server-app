package pl.sg.accountant.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.sg.accountant.model.billings.summary.MonthSummary;
import pl.sg.accountant.model.billings.summary.MonthSummaryPiggyBank;
import pl.sg.accountant.repository.MonthlySummaryRepository;
import pl.sg.application.model.ApplicationUser;
import pl.sg.application.security.annotations.RequestUser;
import pl.sg.application.security.annotations.TokenBearerAuth;

import java.math.BigDecimal;
import java.time.YearMonth;
import java.util.Currency;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/month-summaries")
public class MonthRestSummaryController implements MonthSummaryController {

    private final MonthlySummaryRepository monthlySummaryRepository;

    public MonthRestSummaryController(MonthlySummaryRepository monthlySummaryRepository) {
        this.monthlySummaryRepository = monthlySummaryRepository;
    }

    @Override
    @GetMapping("/savings/{forNMonths}")
    @TokenBearerAuth(any = {"ADMIN", "USER"})
    public ResponseEntity<Map<YearMonth, Map<Currency, BigDecimal>>> getSavingsHistory(
            @PathVariable int forNMonths, @RequestUser ApplicationUser user) {
        Map<YearMonth, Map<Currency, BigDecimal>> result;
        YearMonth from = YearMonth.now().minusMonths(forNMonths);
        List<MonthSummary> allBySinceYear = this.monthlySummaryRepository.findAll();
        result = allBySinceYear.stream()
                .filter(ms -> ms.getBillingPeriod().getPeriod().isAfter(from.minusMonths(1)))
                .collect(Collectors.toMap(
                        monthSummary -> monthSummary.getBillingPeriod().getPeriod(),
                        monthSummary -> monthSummary.getData().getSavings(),
                        (a, b) -> b)
                );
        return ResponseEntity.ok(result);
    }

    @Override
    @GetMapping("/piggy-banks/{forNMonths}")
    @TokenBearerAuth(any = {"ADMIN", "USER"})
    public ResponseEntity<Map<YearMonth, List<MonthSummaryPiggyBank>>> getPiggyBanksHistory(
            @PathVariable int forNMonths, @RequestUser ApplicationUser user) {
        Map<YearMonth, List<MonthSummaryPiggyBank>> result = new HashMap<>();
        YearMonth from = YearMonth.now().minusMonths(forNMonths);
        List<MonthSummary> allBySinceYear = this.monthlySummaryRepository.findAll();
        result = allBySinceYear.stream()
                .filter(ms -> ms.getBillingPeriod().getPeriod().isAfter(from.minusMonths(1)))
                .collect(Collectors.toMap(
                        monthSummary -> monthSummary.getBillingPeriod().getPeriod(),
                        monthSummary -> monthSummary.getData().getPiggyBanks(),
                        (a, b) -> b)
                );
        return ResponseEntity.ok(result);
    }
}
