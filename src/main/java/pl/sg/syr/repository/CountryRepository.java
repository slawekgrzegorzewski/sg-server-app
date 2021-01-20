package pl.sg.syr.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import pl.sg.accountant.model.billings.BillingPeriod;
import pl.sg.application.model.ApplicationUser;
import pl.sg.syr.model.Country;

import java.time.YearMonth;
import java.util.List;
import java.util.Optional;

public interface CountryRepository extends JpaRepository<Country, Integer> {
}
