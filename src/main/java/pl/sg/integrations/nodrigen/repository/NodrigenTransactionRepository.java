package pl.sg.integrations.nodrigen.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.sg.integrations.nodrigen.model.transcations.NodrigenTransaction;

import java.util.Collection;
import java.util.List;

public interface NodrigenTransactionRepository extends JpaRepository<NodrigenTransaction, Integer> {
    List<NodrigenTransaction> findAllByTransactionIdIn(Collection<String> transactionIds);
}