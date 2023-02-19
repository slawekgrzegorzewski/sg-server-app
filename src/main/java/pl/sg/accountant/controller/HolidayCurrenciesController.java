package pl.sg.accountant.controller;

import pl.sg.accountant.transport.HolidayCurrencies;
import pl.sg.application.model.Domain;

import jakarta.validation.Valid;

public interface HolidayCurrenciesController {

    HolidayCurrencies get(Domain domain);

    HolidayCurrencies create(@Valid pl.sg.accountant.model.HolidayCurrencies holidayCurrencies);

    String update(@Valid pl.sg.accountant.model.HolidayCurrencies holidayCurrencies);
}
