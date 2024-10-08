package pl.sg.accountant.repository.ledger;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import pl.sg.accountant.model.ledger.PerformedService;
import pl.sg.application.model.Domain;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;
import java.util.Set;

public interface PerformedServiceRepository extends JpaRepository<PerformedService, Integer> {

    List<PerformedService> findByDomain(Domain domain);

    @Query("SELECT ps FROM PerformedService ps LEFT JOIN ps.payments psp " +
            "WHERE ps.domain = ?1 AND (psp.clientPayment.date BETWEEN ?2 AND ?3)" +
            "GROUP BY ps.id " +
            "HAVING COALESCE(SUM(psp.price), 0) > 0")
    List<PerformedService> findByDomainAndDateBetweenPaidOnly(Domain domain, LocalDate from, LocalDate to);

    @Query("SELECT ps FROM PerformedService ps LEFT JOIN ps.payments psp " +
            "WHERE ps.domain = ?1 " +
            "GROUP BY ps.id " +
            "HAVING COALESCE(SUM(psp.price), 0) < ps.price")
    List<PerformedService> findByDomainAndNotPaid(Domain domain);

    default List<PerformedService> forMonth(Domain domain, YearMonth forMonth) {
        Set<PerformedService> result = new java.util.HashSet<>(Set.copyOf(
                findByDomainAndDateBetweenPaidOnly(domain, forMonth.atDay(1), forMonth.atEndOfMonth())
        ));
        if (forMonth.equals(YearMonth.now())) {
            result.addAll(findByDomainAndNotPaid(domain));
        }
        return List.copyOf(result);
    }

}
