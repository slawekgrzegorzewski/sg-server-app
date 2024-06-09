package pl.sg.accountant.repository.bussines;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.sg.accountant.model.bussines.DebitInvoice;
import pl.sg.accountant.model.bussines.FinancialDocument;
import pl.sg.application.model.Domain;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface FinancialDocumentRepository extends JpaRepository<FinancialDocument, Integer> {
    FinancialDocument findFinancialDocumentByPublicId(UUID publicId);

    List<FinancialDocument> findFinancialDocumentByDomain_IdAndCreatedAtBetween(int domainId, LocalDate start, LocalDate end);
}
