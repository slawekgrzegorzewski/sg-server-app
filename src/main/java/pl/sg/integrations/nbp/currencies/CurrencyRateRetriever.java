package pl.sg.integrations.nbp.currencies;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import pl.sg.accountant.service.HolidayCurrenciesService;
import pl.sg.utils.PageFetcher;

import java.sql.Date;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Currency;

@Component
public class CurrencyRateRetriever {

    private static final Currency EUR = Currency.getInstance("EUR");
    private static final Currency HRK = Currency.getInstance("HRK");
    private final PageFetcher pageFetcher;
    private final HolidayCurrenciesService holidayCurrenciesService;

    public CurrencyRateRetriever(PageFetcher pageFetcher, HolidayCurrenciesService holidayCurrenciesService) {
        this.pageFetcher = pageFetcher;
        this.holidayCurrenciesService = holidayCurrenciesService;
    }

    @Scheduled(cron = "0 00 13 * * *", zone = "Europe/Warsaw")
    public void getCurrencyRate() {
        GetCurrencyRate getCurrencyRate = new GetCurrencyRate(pageFetcher);
        refreshRate(getCurrencyRate, EUR);
        refreshRate(getCurrencyRate, HRK);

    }

    private void refreshRate(GetCurrencyRate getCurrencyRate, Currency currency) {
        getCurrencyRate.getCurrencyRate(currency)
                .map(GetCurrencyRateResponse::getRates)
                .stream()
                .flatMap(Arrays::stream)
                .peek(r -> {})
                .sorted(Comparator.<GetCurrencyRateResponse.Rate, Date>comparing(r -> Date.valueOf(r.getEffectiveDate())).reversed())
                .map(GetCurrencyRateResponse.Rate::getMid)
                .findFirst()
                .ifPresent(r -> this.holidayCurrenciesService.updateCurrencyRateForAll(currency, r));
    }
}
