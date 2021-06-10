package pl.sg.accountant.controller;

import pl.sg.accountant.model.HolidayCurrencies;
import pl.sg.accountant.transport.HolidayCurrenciesTO;
import pl.sg.application.model.Domain;

import javax.validation.Valid;

public interface HolidayCurrenciesController {

    HolidayCurrenciesTO get(Domain domain);

    HolidayCurrenciesTO create(@Valid HolidayCurrencies holidayCurrencies);

    String update(@Valid HolidayCurrencies holidayCurrencies);
}
