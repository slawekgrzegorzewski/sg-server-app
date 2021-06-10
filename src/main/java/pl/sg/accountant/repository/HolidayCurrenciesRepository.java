package pl.sg.accountant.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.sg.accountant.model.HolidayCurrencies;
import pl.sg.application.model.Domain;

import java.util.Optional;

public interface HolidayCurrenciesRepository extends JpaRepository<HolidayCurrencies, Integer> {
    Optional<HolidayCurrencies> findByDomain(Domain domain);
}
