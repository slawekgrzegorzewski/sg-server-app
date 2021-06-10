package pl.sg.accountant.service;

import pl.sg.accountant.model.HolidayCurrencies;
import pl.sg.application.model.Domain;

import java.util.Optional;

public interface HolidayCurrenciesService {

    Optional<HolidayCurrencies> getForDomain(Domain domain);

    HolidayCurrencies create(HolidayCurrencies holidayCurrencies);

    void update(HolidayCurrencies holidayCurrencies);
}
