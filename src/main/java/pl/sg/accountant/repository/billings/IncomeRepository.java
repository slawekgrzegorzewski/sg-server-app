package pl.sg.accountant.repository.billings;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.sg.accountant.model.billings.Income;

public interface IncomeRepository extends JpaRepository<Income, Integer> {
}
