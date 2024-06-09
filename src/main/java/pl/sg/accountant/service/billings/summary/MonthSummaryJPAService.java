package pl.sg.accountant.service.billings.summary;

import org.springframework.stereotype.Component;
import pl.sg.accountant.model.billings.summary.MonthSummary;
import pl.sg.accountant.model.billings.summary.MonthSummaryPiggyBank;
import pl.sg.accountant.repository.billings.summary.MonthlySummaryRepository;
import pl.sg.application.model.Domain;
import pl.sg.application.service.DomainService;

import java.math.BigDecimal;
import java.time.YearMonth;
import java.util.Currency;
import java.util.List;
import java.util.Map;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
public class MonthSummaryJPAService implements MonthSummaryService {

    private static final Collector<MonthSummary, ?, Map<YearMonth, Map<Currency, BigDecimal>>> SAVINGS_COLLECTOR = Collectors.toMap(
            monthSummary -> monthSummary.getBillingPeriod().getPeriod(),
            monthSummary -> monthSummary.getData().getSavings(),
            (a, b) -> b);
    private static final Collector<MonthSummary, ?, Map<YearMonth, List<MonthSummaryPiggyBank>>> PIGGY_BANKS_COLLECTOR = Collectors.toMap(
            monthSummary -> monthSummary.getBillingPeriod().getPeriod(),
            monthSummary -> monthSummary.getData().getPiggyBanks(),
            (a, b) -> b);
    private final MonthlySummaryRepository monthlySummaryRepository;

    private final DomainService domainService;

    public MonthSummaryJPAService(MonthlySummaryRepository monthlySummaryRepository, DomainService domainService) {
        this.monthlySummaryRepository = monthlySummaryRepository;
        this.domainService = domainService;
    }

    @Override
    public Map<YearMonth, Map<Currency, BigDecimal>> getSavingsHistory(Domain domain, int forNMonths) {
        return monthSummaryStream(domain, forNMonths).collect(SAVINGS_COLLECTOR);
    }

    @Override
    public Map<YearMonth, List<MonthSummaryPiggyBank>> getPiggyBanksHistory(Domain domain, int forNMonths) {
        return monthSummaryStream(domain, forNMonths).collect(PIGGY_BANKS_COLLECTOR);
    }

    private Stream<MonthSummary> monthSummaryStream(Domain domain, int forNMonths) {
        YearMonth from = YearMonth.now().minusMonths(forNMonths);
        return this.monthlySummaryRepository.findAllByBillingPeriod_Domain(domain).stream()
                .filter(ms -> ms.getBillingPeriod().getPeriod().isAfter(from.minusMonths(1)));
    }
}
