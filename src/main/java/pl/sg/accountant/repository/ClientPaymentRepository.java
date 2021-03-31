package pl.sg.accountant.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import pl.sg.accountant.model.accounts.ClientPayment;
import pl.sg.application.model.Domain;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;

public interface ClientPaymentRepository extends JpaRepository<ClientPayment, Integer> {

    @Query("SELECT cp FROM ClientPayment cp " +
            "WHERE cp.domain = ?1 AND (cp.date BETWEEN ?2 AND ?3) ")
    List<ClientPayment> findByDomainAndDateBetween(Domain domain, LocalDate from, LocalDate to);

    default List<ClientPayment> forMonth(Domain domain, YearMonth forMonth) {
        return findByDomainAndDateBetween(domain, forMonth.atDay(1), forMonth.atEndOfMonth());
    }
}
