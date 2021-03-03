package pl.sg.accountant.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.sg.accountant.model.AccountantSettings;
import pl.sg.application.model.Domain;

import java.util.Optional;

public interface AccountantSettingsRepository extends JpaRepository<AccountantSettings, Integer> {

    Optional<AccountantSettings> findByDomain(Domain domain);

    AccountantSettings getByDomain(Domain domain);

}
