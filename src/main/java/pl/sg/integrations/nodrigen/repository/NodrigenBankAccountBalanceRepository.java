package pl.sg.integrations.nodrigen.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.sg.integrations.nodrigen.model.balances.NodrigenBankAccountBalance;

public interface NodrigenBankAccountBalanceRepository extends JpaRepository<NodrigenBankAccountBalance, Integer> {
}