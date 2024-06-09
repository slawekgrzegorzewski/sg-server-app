package pl.sg.accountant.repository.billings;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.sg.accountant.model.billings.PiggyBank;
import pl.sg.application.model.Domain;

import java.util.List;

public interface PiggyBankRepository extends JpaRepository<PiggyBank, Integer> {

    List<PiggyBank> findByDomain(Domain domain);
}
