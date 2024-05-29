package pl.sg.loans.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.sg.application.model.Domain;
import pl.sg.loans.model.Loan;

import java.util.List;
import java.util.UUID;

@Repository
public interface LoanRepository extends JpaRepository<Loan, Long> {
    List<Loan> findAllByDomain(Domain domain);

    Loan findByPublicId(UUID publicId);
}
