package pl.sg.accountant.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.sg.accountant.model.billings.PiggyBank;
import pl.sg.application.model.ApplicationUser;

import java.util.List;

public interface PiggyBankRepository extends JpaRepository<PiggyBank, Integer> {

    List<PiggyBank> findByApplicationUser(ApplicationUser user);
}
