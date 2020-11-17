package pl.sg.accountant.controller;

import pl.sg.accountant.transport.CurrencyTO;

import java.util.List;
import java.util.Locale;

public interface CurrenciesController {
    List<CurrencyTO> accounts(Locale locale);
}
