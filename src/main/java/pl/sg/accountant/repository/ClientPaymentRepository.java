package pl.sg.accountant.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.sg.accountant.model.accounts.ClientPayment;
import pl.sg.application.model.Domain;

import java.util.List;

public interface ClientPaymentRepository extends JpaRepository<ClientPayment, Integer> {
    List<ClientPayment> findByDomain(Domain domain);
}
