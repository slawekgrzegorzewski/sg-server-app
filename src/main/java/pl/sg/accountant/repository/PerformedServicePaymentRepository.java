package pl.sg.accountant.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import pl.sg.accountant.model.ledger.ClientPayment;
import pl.sg.accountant.model.ledger.PerformedService;
import pl.sg.accountant.model.ledger.PerformedServicePayment;
import pl.sg.application.model.Domain;

import java.util.List;

public interface PerformedServicePaymentRepository extends JpaRepository<PerformedServicePayment, Integer> {

    @Query("select psp " +
            "from PerformedServicePayment psp " +
            "left join psp.performedService ps " +
            "left join psp.clientPayment sp " +
            "where ps.domain = ?1 or sp.domain = ?1")
    List<PerformedServicePayment> findByDomain(Domain domain);

    List<PerformedServicePayment> findByClientPaymentAndPerformedService(
            ClientPayment clientPayment,
            PerformedService performedService);

}
