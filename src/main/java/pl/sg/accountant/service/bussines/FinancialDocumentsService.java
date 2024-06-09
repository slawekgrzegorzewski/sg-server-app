package pl.sg.accountant.service.bussines;

import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;
import pl.sg.accountant.model.bussines.*;
import pl.sg.accountant.repository.bussines.*;
import pl.sg.application.repository.DomainRepository;

import javax.money.MonetaryAmount;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import static pl.sg.application.DomainValidator.validateDomain;

@Service
public class FinancialDocumentsService {

    private final BillOfPurchaseRepository billOfPurchaseRepository;
    private final BillOfSaleRepository billOfSaleRepository;
    private final ClientRepository clientRepository;
    private final CreditInvoiceRepository creditInvoiceRepository;
    private final DebitInvoiceRepository debitInvoiceRepository;
    private final DomainRepository domainRepository;
    private final FinancialDocumentRepository financialDocumentRepository;
    private final SupplierRepository supplierRepository;

    public FinancialDocumentsService(BillOfPurchaseRepository billOfPurchaseRepository,
                                     BillOfSaleRepository billOfSaleRepository,
                                     ClientRepository clientRepository, CreditInvoiceRepository creditInvoiceRepository, DebitInvoiceRepository debitInvoiceRepository,
                                     DomainRepository domainRepository, FinancialDocumentRepository financialDocumentRepository,
                                     SupplierRepository supplierRepository) {
        this.billOfPurchaseRepository = billOfPurchaseRepository;
        this.billOfSaleRepository = billOfSaleRepository;
        this.clientRepository = clientRepository;
        this.creditInvoiceRepository = creditInvoiceRepository;
        this.debitInvoiceRepository = debitInvoiceRepository;
        this.domainRepository = domainRepository;
        this.financialDocumentRepository = financialDocumentRepository;
        this.supplierRepository = supplierRepository;
    }

    public List<FinancialDocument> findAllByYearMonth(int domainId, YearMonth yearMonth) {
        return financialDocumentRepository.findFinancialDocumentByDomain_IdAndCreatedAtBetween(domainId, yearMonth.atDay(1), yearMonth.atEndOfMonth());
    }

    public BillOfPurchase createBillOfPurchase(
            int domainId,
            UUID supplierPublicId,
            LocalDate createdAt,
            String description,
            MonetaryAmount amount,
            String name) {
        BillOfPurchase billOfPurchase = new BillOfPurchase();
        return updateAndSaveBillOfPurchase(domainId, supplierPublicId, createdAt, description, amount, name, billOfPurchase);
    }

    public BillOfPurchase updateBillOfPurchase(
            UUID billOfPurchasePublicID,
            int domainId,
            UUID supplierPublicId,
            LocalDate createdAt,
            String description,
            MonetaryAmount amount,
            String name) {
        BillOfPurchase billOfPurchase = Objects.requireNonNull(billOfPurchaseRepository.findBillOfPurchaseByPublicId(billOfPurchasePublicID));
        validateDomain(domainId, billOfPurchase.getDomain());
        return updateAndSaveBillOfPurchase(domainId, supplierPublicId, createdAt, description, amount, name, billOfPurchase);
    }

    private @NotNull BillOfPurchase updateAndSaveBillOfPurchase(int domainId, UUID supplierPublicId, LocalDate createdAt, String description, MonetaryAmount amount, String name, BillOfPurchase billOfPurchase) {
        Supplier supplier = Objects.requireNonNull(supplierRepository.findByPublicId(supplierPublicId));
        validateDomain(domainId, supplier.getDomain());
        billOfPurchase.setCreatedAt(createdAt);
        billOfPurchase.setDescription(description);
        billOfPurchase.setAmount(amount);
        billOfPurchase.setName(name);
        billOfPurchase.setOtherParty(supplier);
        billOfPurchase.setDomain(domainRepository.getReferenceById(domainId));
        return billOfPurchaseRepository.save(billOfPurchase);
    }

    public BillOfSale createBillOfSale(
            int domainId,
            UUID clientPublicId,
            LocalDate createdAt,
            String description,
            MonetaryAmount amount,
            String name) {
        BillOfSale billOfSale = new BillOfSale();
        return updateAndSaveBillOfSale(domainId, clientPublicId, createdAt, description, amount, name, billOfSale);
    }

    private @NotNull BillOfSale updateAndSaveBillOfSale(int domainId, UUID clientPublicId, LocalDate createdAt, String description, MonetaryAmount amount, String name, BillOfSale billOfSale) {
        Client client = Objects.requireNonNull(clientRepository.findByPublicId(clientPublicId));
        validateDomain(domainId, client.getDomain());
        billOfSale.setCreatedAt(createdAt);
        billOfSale.setDescription(description);
        billOfSale.setAmount(amount);
        billOfSale.setName(name);
        billOfSale.setOtherParty(client);
        billOfSale.setDomain(domainRepository.getReferenceById(domainId));
        return billOfSaleRepository.save(billOfSale);
    }

    public BillOfSale updateBillOfSale(
            UUID billOfSalePublicID,
            int domainId,
            UUID clientPublicId,
            LocalDate createdAt,
            String description,
            MonetaryAmount amount,
            String name) {
        BillOfSale billOfSale = Objects.requireNonNull(billOfSaleRepository.findBillOfSaleByPublicId(billOfSalePublicID));
        validateDomain(domainId, billOfSale.getDomain());
        return updateAndSaveBillOfSale(domainId, clientPublicId, createdAt, description, amount, name, billOfSale);
    }

    public CreditInvoice createCreditInvoice(
            int domainId,
            UUID clientPublicId,
            LocalDate createdAt,
            LocalDate dueTo,
            String description,
            MonetaryAmount amount,
            MonetaryAmount vat,
            String name) {
        CreditInvoice creditInvoice = new CreditInvoice();
        return updateAndSaveCreditInvoice(domainId, clientPublicId, createdAt, dueTo, description, amount, vat, name, creditInvoice);
    }

    public CreditInvoice updateCreditInvoice(
            UUID creditInvoicePublicID,
            int domainId,
            UUID clientPublicId,
            LocalDate createdAt,
            LocalDate dueTo,
            String description,
            MonetaryAmount amount,
            MonetaryAmount vat,
            String name) {
        CreditInvoice creditInvoice = Objects.requireNonNull(creditInvoiceRepository.findBillOfSaleByPublicId(creditInvoicePublicID));
        validateDomain(domainId, creditInvoice.getDomain());
        return updateAndSaveCreditInvoice(domainId, clientPublicId, createdAt, dueTo, description, amount, vat, name, creditInvoice);
    }

    private @NotNull CreditInvoice updateAndSaveCreditInvoice(int domainId, UUID clientPublicId, LocalDate createdAt, LocalDate dueTo, String description, MonetaryAmount amount, MonetaryAmount vat, String name, CreditInvoice creditInvoice) {
        Client client = Objects.requireNonNull(clientRepository.findByPublicId(clientPublicId));
        validateDomain(domainId, client.getDomain());
        creditInvoice.setCreatedAt(createdAt);
        creditInvoice.setDueTo(dueTo);
        creditInvoice.setDescription(description);
        creditInvoice.setAmount(amount);
        creditInvoice.setVat(vat);
        creditInvoice.setName(name);
        creditInvoice.setOtherParty(client);
        creditInvoice.setDomain(domainRepository.getReferenceById(domainId));
        return creditInvoiceRepository.save(creditInvoice);
    }

    public DebitInvoice createDebitInvoice(
            int domainId,
            UUID supplierPublicId,
            LocalDate createdAt,
            LocalDate dueTo,
            String description,
            MonetaryAmount amount,
            MonetaryAmount vat,
            String name) {
        DebitInvoice debitInvoice = new DebitInvoice();
        return updateAndSaveDebitInvoice(domainId, supplierPublicId, createdAt, dueTo, description, amount, vat, name, debitInvoice);
    }

    public DebitInvoice updateDebitInvoice(
            UUID updateInvoicePublicID,
            int domainId,
            UUID supplierPublicId,
            LocalDate createdAt,
            LocalDate dueTo,
            String description,
            MonetaryAmount amount,
            MonetaryAmount vat,
            String name) {
        DebitInvoice debitInvoice = Objects.requireNonNull(debitInvoiceRepository.findBillOfSaleByPublicId(updateInvoicePublicID));
        validateDomain(domainId, debitInvoice.getDomain());
        return updateAndSaveDebitInvoice(domainId, supplierPublicId, createdAt, dueTo, description, amount, vat, name, debitInvoice);
    }

    private @NotNull DebitInvoice updateAndSaveDebitInvoice(int domainId, UUID supplierPublicId, LocalDate createdAt, LocalDate dueTo, String description, MonetaryAmount amount, MonetaryAmount vat, String name, DebitInvoice debitInvoice) {
        Supplier supplier = Objects.requireNonNull(supplierRepository.findByPublicId(supplierPublicId));
        validateDomain(domainId, supplier.getDomain());
        debitInvoice.setCreatedAt(createdAt);
        debitInvoice.setDueTo(dueTo);
        debitInvoice.setDescription(description);
        debitInvoice.setAmount(amount);
        debitInvoice.setVat(vat);
        debitInvoice.setName(name);
        debitInvoice.setOtherParty(supplier);
        debitInvoice.setDomain(domainRepository.getReferenceById(domainId));
        return debitInvoiceRepository.save(debitInvoice);
    }

    public FinancialDocument getByPublicId(int domainId, UUID financialDocumentPublicId) {
        FinancialDocument financialDocument = financialDocumentRepository.findFinancialDocumentByPublicId(financialDocumentPublicId);
        validateDomain(domainId, financialDocument.getDomain());
        return financialDocument;
    }

    public void delete(int domainId, UUID financialDocumentPublicId) {
        FinancialDocument financialDocument = Objects.requireNonNull(financialDocumentRepository.findFinancialDocumentByPublicId(financialDocumentPublicId));
        validateDomain(domainId, financialDocument.getDomain());
    }
}