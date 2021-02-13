package pl.sg.accountant.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.sg.accountant.model.billings.summary.MonthSummaryPiggyBank;
import pl.sg.accountant.service.MonthSummaryService;
import pl.sg.application.model.ApplicationUser;
import pl.sg.application.security.annotations.RequestUser;
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
    @GetMapping("/savings/{domainId}/{forNMonths}")
    @TokenBearerAuth(any = {"ACCOUNTANT_ADMIN", "ACCOUNTANT_USER"})
    public Map<YearMonth, Map<Currency, BigDecimal>> getSavingsHistory(@RequestUser ApplicationUser user,
                                                                       @PathVariable int domainId,
                                                                       @PathVariable int forNMonths) {
        return this.monthSummaryService.getSavingsHistory(user, domainId, forNMonths);
    }

    @Override
    @GetMapping("/piggy-banks/{domainId}/{forNMonths}")
    @TokenBearerAuth(any = {"ACCOUNTANT_ADMIN", "ACCOUNTANT_USER"})
    public Map<YearMonth, List<MonthSummaryPiggyBank>> getPiggyBanksHistory(@RequestUser ApplicationUser user,
                                                                            @PathVariable int domainId,
                                                                            @PathVariable int forNMonths) {
        return this.monthSummaryService.getPiggyBanksHistory(user, domainId, forNMonths);
    }
}
