package pl.sg.integrations.nbp.currencies;

import org.junit.Test;
import pl.sg.utils.PageFetcherImpl;

import java.util.Currency;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class GetCurrencyRateTest {

    @Test
    public void getEurRate() {
        Optional<GetCurrencyRateResponse> eur = new GetCurrencyRate(new PageFetcherImpl()).getCurrencyRate(Currency.getInstance("EUR"));
        assertTrue(eur.isPresent());
    }

    @Test
    public void getHrkRate() {
        Optional<GetCurrencyRateResponse> hrk = new GetCurrencyRate(new PageFetcherImpl()).getCurrencyRate(Currency.getInstance("HRK"));
        assertTrue(hrk.isPresent());
    }
}