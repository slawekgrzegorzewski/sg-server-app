package pl.sg.accountant.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import pl.sg.accountant.transport.CurrencyTO;
import pl.sg.application.security.annotations.TokenBearerAuth;

import java.util.Currency;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/currency")
public class CurrenciesRestController {

    @RequestMapping(value = "/all/{locale}", method = RequestMethod.GET)
    @TokenBearerAuth
    public ResponseEntity<List<CurrencyTO>> accounts(@PathVariable Locale locale) {
        List<CurrencyTO> currencies = Currency.getAvailableCurrencies().stream()
                .map(c -> new CurrencyTO(c.getCurrencyCode(), c.getDisplayName(locale)))
                .collect(Collectors.toList());
        return ResponseEntity.ok(currencies);
    }
}