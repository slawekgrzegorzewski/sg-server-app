package pl.sg.accountant.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import pl.sg.accountant.model.accounts.ClientPayment;
import pl.sg.application.model.Domain;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;

public interface ClientPaymentRepository extends JpaRepository<ClientPayment, Integer> {

    @Query("SELECT cp FROM ClientPayment cp LEFT JOIN cp.services cps " +
            "WHERE cp.domain = ?1 " +
            "GROUP BY cp.id " +
            "HAVING (cp.date BETWEEN ?2 AND ?3) AND COALESCE(SUM(cps.price), 0) = cp.price")
    List<ClientPayment> findByDomainAndDateBetweenPaidOnly(Domain domain, LocalDate from, LocalDate to);

    @Query("SELECT cp FROM ClientPayment cp LEFT JOIN cp.services cps " +
            "WHERE cp.domain = ?1 " +
            "GROUP BY cp.id " +
            "HAVING (cp.date BETWEEN ?2 AND ?3) OR COALESCE(SUM(cps.price), 0) <> cp.price")
    List<ClientPayment> findByDomainInMonthAndNotPaid(Domain domain, LocalDate from, LocalDate to);

    default List<ClientPayment> findByDomainInMonthPaidOnly(Domain domain, YearMonth month) {
        return findByDomainAndDateBetweenPaidOnly(domain, month.atDay(1), month.atEndOfMonth());
    }

    default List<ClientPayment> findByDomainInMonthAndNotPaid(Domain domain, YearMonth month) {
        return findByDomainInMonthAndNotPaid(domain, month.atDay(1), month.atEndOfMonth());
    }
}
