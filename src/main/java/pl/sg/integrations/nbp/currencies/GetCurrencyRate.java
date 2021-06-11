package pl.sg.integrations.nbp.currencies;

import com.google.gson.Gson;
import pl.sg.utils.PageFetcher;

import java.util.Currency;
import java.util.Optional;

public class GetCurrencyRate {

    private final PageFetcher pageFetcher;

    public GetCurrencyRate(PageFetcher pageFetcher) {
        this.pageFetcher = pageFetcher;
    }

    public Optional<GetCurrencyRateResponse> getCurrencyRate(Currency currency) {
        String url = String.format("http://api.nbp.pl/api/exchangerates/rates/a/%s?format=json", currency.getCurrencyCode());
        return pageFetcher
                .getBody(url)
                .map(r -> new Gson().fromJson(r, GetCurrencyRateResponse.class));
    }
}
