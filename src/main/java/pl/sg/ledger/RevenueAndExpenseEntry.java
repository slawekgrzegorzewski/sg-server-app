package pl.sg.ledger;

import jakarta.persistence.*;
import pl.sg.application.model.Domain;
import pl.sg.application.model.WithDomain;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Entity(name = "revenue_and_expense_entries")
public class RevenueAndExpenseEntry implements WithDomain<RevenueAndExpenseEntry> {

    @Id
    @SequenceGenerator(
            name = "revenueAndExpenseEntryIdGenerator",
            sequenceName = "revenue_and_expense_entries_id_seq",
            allocationSize = 1)
    @GeneratedValue(generator = "revenueAndExpenseEntryIdGenerator")
    private long id;
    private UUID publicId;
    @ManyToOne
    private Domain domain;
    private LocalDate entryDate;
    private int entryOrder;
    private String accountingDocumentNumber;
    private String counterparty;
    private String counterpartyAddress;
    private String description;
    private BigDecimal providedGoodsAndServicesValue;
    private BigDecimal otherIncome;
    private BigDecimal totalIncome;
    private BigDecimal purchasedGoodsAndMaterialsValue;
    private BigDecimal additionalCostOfPurchase;
    private BigDecimal remunerationInCashOrInKind;
    private BigDecimal otherExpense;
    private BigDecimal totalExpense;
    private String comments;

    public long id() {
        return id;
    }

    public RevenueAndExpenseEntry setId(long id) {
        this.id = id;
        return this;
    }

    public UUID publicId() {
        return publicId;
    }

    public RevenueAndExpenseEntry setPublicId(UUID publicId) {
        this.publicId = publicId;
        return this;
    }

    @Override
    public Domain getDomain() {
        return domain;
    }

    @Override
    public RevenueAndExpenseEntry setDomain(Domain domain) {
        this.domain = domain;
        return this;
    }

    public LocalDate getEntryDate() {
        return entryDate;
    }

    public void setEntryDate(LocalDate entryDate) {
        this.entryDate = entryDate;
    }

    public int getEntryOrder() {
        return entryOrder;
    }

    public void setEntryOrder(int entryOrder) {
        this.entryOrder = entryOrder;
    }

    public String getAccountingDocumentNumber() {
        return accountingDocumentNumber;
    }

    public void setAccountingDocumentNumber(String accountingDocumentNumber) {
        this.accountingDocumentNumber = accountingDocumentNumber;
    }

    public String getCounterparty() {
        return counterparty;
    }

    public void setCounterparty(String counterparty) {
        this.counterparty = counterparty;
    }

    public String getCounterpartyAddress() {
        return counterpartyAddress;
    }

    public void setCounterpartyAddress(String counterpartyAddress) {
        this.counterpartyAddress = counterpartyAddress;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getProvidedGoodsAndServicesValue() {
        return providedGoodsAndServicesValue;
    }

    public void setProvidedGoodsAndServicesValue(BigDecimal providedGoodsAndServicesValue) {
        this.providedGoodsAndServicesValue = providedGoodsAndServicesValue;
    }

    public BigDecimal getOtherIncome() {
        return otherIncome;
    }

    public void setOtherIncome(BigDecimal otherIncome) {
        this.otherIncome = otherIncome;
    }

    public BigDecimal getTotalIncome() {
        return totalIncome;
    }

    public void setTotalIncome(BigDecimal totalIncome) {
        this.totalIncome = totalIncome;
    }

    public BigDecimal getPurchasedGoodsAndMaterialsValue() {
        return purchasedGoodsAndMaterialsValue;
    }

    public void setPurchasedGoodsAndMaterialsValue(BigDecimal purchasedGoodsAndMaterialsValue) {
        this.purchasedGoodsAndMaterialsValue = purchasedGoodsAndMaterialsValue;
    }

    public BigDecimal getAdditionalCostOfPurchase() {
        return additionalCostOfPurchase;
    }

    public void setAdditionalCostOfPurchase(BigDecimal additionalCostOfPurchase) {
        this.additionalCostOfPurchase = additionalCostOfPurchase;
    }

    public BigDecimal getRemunerationInCashOrInKind() {
        return remunerationInCashOrInKind;
    }

    public void setRemunerationInCashOrInKind(BigDecimal remunerationInCashOrInKind) {
        this.remunerationInCashOrInKind = remunerationInCashOrInKind;
    }

    public BigDecimal getOtherExpense() {
        return otherExpense;
    }

    public void setOtherExpense(BigDecimal otherExpense) {
        this.otherExpense = otherExpense;
    }

    public BigDecimal getTotalExpense() {
        return totalExpense;
    }

    public void setTotalExpense(BigDecimal totalExpense) {
        this.totalExpense = totalExpense;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }
}

