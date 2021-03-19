package pl.sg.accountant.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import pl.sg.accountant.model.accounts.ClientPayment;
import pl.sg.application.model.Domain;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;

public interface ClientPaymentRepository extends JpaRepository<ClientPayment, Integer> {

    @Query("SELECT DISTINCT cp FROM PerformedService ps LEFT JOIN ps.payments psp LEFT JOIN psp.clientPayment cp " +
            "WHERE cp.domain = ?1 AND (psp.clientPayment.date BETWEEN ?2 AND ?3) " +
            "GROUP BY ps.id, cp.id " +
            "HAVING COALESCE(SUM(psp.price), 0) > 0")
    List<ClientPayment> findByDomainAndDateBetweenPaidOnly(Domain domain, LocalDate from, LocalDate to);

    @Query("SELECT DISTINCT cp FROM PerformedService ps LEFT JOIN ps.payments psp LEFT JOIN psp.clientPayment cp " +
            "WHERE cp.domain = ?1 " +
            "GROUP BY ps.id, cp.id " +
            "HAVING COALESCE(SUM(psp.price), 0) < ps.price")
    List<ClientPayment> findByDomainAndNotPaid(Domain domain);


    default List<ClientPayment> forMonth(Domain domain, YearMonth forMonth) {
        List<ClientPayment> result = findByDomainAndDateBetweenPaidOnly(
                domain,
                forMonth.atDay(1),
                forMonth.atEndOfMonth());
        if (YearMonth.now().equals(forMonth)) {
            result.addAll(findByDomainAndNotPaid(domain));
        }
        return result;
    }
}
