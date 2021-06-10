package pl.sg.accountant.service;

import org.springframework.stereotype.Component;
import pl.sg.accountant.model.HolidayCurrencies;
import pl.sg.accountant.repository.HolidayCurrenciesRepository;
import pl.sg.application.model.Domain;

import java.util.Optional;

@Component
public class HolidayCurrenciesJPAService implements HolidayCurrenciesService {
    private final HolidayCurrenciesRepository holidayCurrenciesRepository;

    public HolidayCurrenciesJPAService(HolidayCurrenciesRepository holidayCurrenciesRepository) {
        this.holidayCurrenciesRepository = holidayCurrenciesRepository;
    }

    @Override
    public Optional<HolidayCurrencies> getForDomain(Domain domain) {
        return holidayCurrenciesRepository.findByDomain(domain);
    }

    @Override
    public HolidayCurrencies create(HolidayCurrencies holidayCurrencies) {
        holidayCurrencies.setId(null);
        return holidayCurrenciesRepository.save(holidayCurrencies);
    }

    @Override
    public void update(HolidayCurrencies holidayCurrencies) {
        holidayCurrenciesRepository.save(holidayCurrencies);
    }
}
