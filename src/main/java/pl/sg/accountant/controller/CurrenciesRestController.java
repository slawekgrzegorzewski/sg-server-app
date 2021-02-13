package pl.sg.accountant.controller;

import org.springframework.web.bind.annotation.*;
import pl.sg.accountant.transport.CurrencyTO;
import pl.sg.application.security.annotations.TokenBearerAuth;

import java.util.Currency;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/currencies")
public class CurrenciesRestController implements CurrenciesController {

    @Override
    @GetMapping(value = "/{locale}")
    @TokenBearerAuth
    public List<CurrencyTO> locales(@PathVariable Locale locale) {
        return Currency.getAvailableCurrencies().stream()
                .map(c -> new CurrencyTO(c.getCurrencyCode(), c.getDisplayName(locale)))
                .collect(Collectors.toList());
    }
}