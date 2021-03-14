package pl.sg.accountant.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import pl.sg.accountant.model.accounts.ClientPayment;
import pl.sg.application.model.Domain;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;

public interface ClientPaymentRepository extends JpaRepository<ClientPayment, Integer> {
    List<ClientPayment> findByDomainAndDateBetween(Domain domain, LocalDate from, LocalDate to);

    @Query("SELECT cp FROM ClientPayment cp join cp.services cps " +
            "WHERE cp.domain = ?1 AND SUM(cps.price) <> cp.price " +
            "AND cp.date BETWEEN ?2 AND ?3 GROUP BY cp.id")
    List<ClientPayment> findByDomainAndNotPaid(Domain domain, LocalDate from, LocalDate to);

    default List<ClientPayment> findByDomainAndInMonth(Domain domain, YearMonth month) {
        return findByDomainAndDateBetween(domain, month.atDay(1), month.atEndOfMonth());
    }

    default List<ClientPayment> findByDomainAndNotPaidInMonth(Domain domain, YearMonth month) {
        return findByDomainAndNotPaid(domain, month.atDay(1), month.atEndOfMonth());
    }
}
