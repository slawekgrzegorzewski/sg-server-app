package pl.sg.integrations.nodrigen.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.sg.application.model.Domain;
import pl.sg.integrations.nodrigen.model.NodrigenTransactionsToImport;
import pl.sg.integrations.nodrigen.model.transcations.NodrigenTransaction;

import java.util.List;

public interface NodrigenTransactionsToImportRepository extends JpaRepository<NodrigenTransactionsToImport, Integer> {
    List<NodrigenTransactionsToImport> findNodrigenTransactionsToImportByDomainId(int domainId);

}