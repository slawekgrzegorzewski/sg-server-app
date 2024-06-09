package pl.sg.accountant.service.ledger;

import jakarta.annotation.Nullable;
import org.springframework.stereotype.Service;
import pl.sg.accountant.model.AccountsException;
import pl.sg.accountant.model.ledger.RevenueAndExpenseEntry;
import pl.sg.accountant.repository.ledger.RevenueAndExpenseEntryRepository;
import pl.sg.application.model.Domain;
import pl.sg.application.repository.DomainRepository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import static pl.sg.application.DomainValidator.validateDomain;

@Service
public class LedgerService {

    private final DomainRepository domainRepository;
    private final RevenueAndExpenseEntryRepository revenueAndExpenseEntryRepository;

    public LedgerService(DomainRepository domainRepository, RevenueAndExpenseEntryRepository revenueAndExpenseEntryRepository) {
        this.domainRepository = domainRepository;
        this.revenueAndExpenseEntryRepository = revenueAndExpenseEntryRepository;
    }

    public List<RevenueAndExpenseEntry> monthLedger(int domainId, YearMonth yearMonth) {
        return revenueAndExpenseEntryRepository.findRevenueAndExpenseEntriesInMonth(domainId, yearMonth);
    }

    public RevenueAndExpenseEntry addRevenueAndExpenseEntry(
            @Nullable UUID revenueAndExpenseEntryId,
            int domainId,
            int entryOrder,
            LocalDate entryDate,
            String accountingDocumentNumber,
            String counterparty,
            String counterpartyAddress,
            String description,
            BigDecimal providedGoodsAndServicesValue,
            BigDecimal otherIncome,
            BigDecimal totalIncome,
            BigDecimal purchasedGoodsAndMaterialsValue,
            BigDecimal additionalCostOfPurchase,
            BigDecimal remunerationInCashOrInKind,
            BigDecimal otherExpense,
            BigDecimal totalExpense,
            String comments
    ) {
        Domain domain = domainRepository.getReferenceById(domainId);
        RevenueAndExpenseEntry revenueAndExpenseEntry;
        if (revenueAndExpenseEntryId == null) {
            revenueAndExpenseEntry = new RevenueAndExpenseEntry();
            revenueAndExpenseEntry.setDomain(domain);
        } else {
            revenueAndExpenseEntry = Objects.requireNonNull(revenueAndExpenseEntryRepository.getRevenueAndExpenseEntriesByPublicId(revenueAndExpenseEntryId));
            validateDomain(domain, revenueAndExpenseEntry.getDomain());
        }
        revenueAndExpenseEntry.setEntryOrder(entryOrder);
        revenueAndExpenseEntry.setEntryDate(entryDate);
        revenueAndExpenseEntry.setAccountingDocumentNumber(accountingDocumentNumber);
        revenueAndExpenseEntry.setCounterparty(counterparty);
        revenueAndExpenseEntry.setCounterpartyAddress(counterpartyAddress);
        revenueAndExpenseEntry.setDescription(description);
        revenueAndExpenseEntry.setProvidedGoodsAndServicesValue(providedGoodsAndServicesValue);
        revenueAndExpenseEntry.setOtherIncome(otherIncome);
        revenueAndExpenseEntry.setTotalIncome(totalIncome);
        revenueAndExpenseEntry.setPurchasedGoodsAndMaterialsValue(purchasedGoodsAndMaterialsValue);
        revenueAndExpenseEntry.setAdditionalCostOfPurchase(additionalCostOfPurchase);
        revenueAndExpenseEntry.setRemunerationInCashOrInKind(remunerationInCashOrInKind);
        revenueAndExpenseEntry.setOtherExpense(otherExpense);
        revenueAndExpenseEntry.setTotalExpense(totalExpense);
        revenueAndExpenseEntry.setComments(comments);
        return revenueAndExpenseEntryRepository.save(revenueAndExpenseEntry);
    }
}