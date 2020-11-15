package pl.sg.accountant.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.sg.accountant.model.billings.Expense;

public interface ExpenseRepository extends JpaRepository<Expense, Integer> {
}
