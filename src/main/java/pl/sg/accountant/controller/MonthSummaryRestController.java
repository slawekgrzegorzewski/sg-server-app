package pl.sg.accountant.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.sg.accountant.model.billings.summary.MonthSummaryPiggyBank;
import pl.sg.accountant.service.billings.summary.MonthSummaryService;
import pl.sg.application.model.Domain;
import pl.sg.application.security.annotations.RequestDomain;
import pl.sg.application.security.annotations.TokenBearerAuth;

import java.math.BigDecimal;
import java.time.YearMonth;
import java.util.Currency;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/month-summaries")
public class MonthSummaryRestController implements MonthSummaryController {

    private final MonthSummaryService monthSummaryService;

    public MonthSummaryRestController(MonthSummaryService monthSummaryService) {
        this.monthSummaryService = monthSummaryService;
    }

    @Override
    @GetMapping("/savings/{forNMonths}")
    @TokenBearerAuth(any = {"ACCOUNTANT_ADMIN", "ACCOUNTANT_USER"})
    public Map<YearMonth, Map<Currency, BigDecimal>> getSavingsHistory(@RequestDomain Domain domain,
                                                                       @PathVariable("forNMonths") int forNMonths) {
        return this.monthSummaryService.getSavingsHistory(domain, forNMonths);
    }

    @Override
    @GetMapping("/piggy-banks/{forNMonths}")
    @TokenBearerAuth(any = {"ACCOUNTANT_ADMIN", "ACCOUNTANT_USER"})
    public Map<YearMonth, List<MonthSummaryPiggyBank>> getPiggyBanksHistory(@RequestDomain Domain domain,
                                                                            @PathVariable("forNMonths") int forNMonths) {
        return this.monthSummaryService.getPiggyBanksHistory(domain, forNMonths);
    }
}
