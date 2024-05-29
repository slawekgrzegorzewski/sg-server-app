package pl.sg.loans.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.sg.loans.model.Installment;
import pl.sg.loans.model.Loan;

import java.util.UUID;

@Repository
public interface InstallmentRepository extends JpaRepository<Installment, Long> {
    Installment findInstallmentByPublicId(UUID publicId);

    void deleteAllByLoan(Loan loan);
}
