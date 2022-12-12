package pl.sg.kpir;

import pl.sg.application.model.Domain;
import pl.sg.application.model.WithDomain;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Entity(name = "kpir_entry")
public class KPiREntry implements WithDomain<KPiREntry> {
    @Id
    @SequenceGenerator(
            name = "kpirEntryIdGenerator",
            sequenceName = "kpir_entry_id_seq",
            allocationSize = 1)
    @GeneratedValue(generator = "kpirEntryIdGenerator")
    private long id;
    private UUID publicId;
    @ManyToOne
    private Domain domain;
    private LocalDate entryDate;
    private int entryOrder;
    private String bookingNumber;
    private String counterparty;
    private String counterpartyAddress;
    private String description;
    private BigDecimal providedGoodsAndServicesValue;
    private BigDecimal otherIncomes;
    private BigDecimal totalIncomes;
    private BigDecimal purchasedGoodsAndMaterialsValue;
    private BigDecimal additionalCostOfPurchase;
    private BigDecimal remunerationInCashOrInKind;
    private BigDecimal otherExpenses;
    private BigDecimal totalExpenses;
    private String comments;

    public long id() {
        return id;
    }

    public KPiREntry setId(long id) {
        this.id = id;
        return this;
    }

    public UUID publicId() {
        return publicId;
    }

    public KPiREntry setPublicId(UUID publicId) {
        this.publicId = publicId;
        return this;
    }

    @Override
    public Domain getDomain() {
        return domain;
    }

    @Override
    public KPiREntry setDomain(Domain domain) {
        this.domain = domain;
        return this;
    }

    public LocalDate locaentryDatelDate() {
        return entryDate;
    }

    public KPiREntry setEntryDate(LocalDate entryDate) {
        this.entryDate = entryDate;
        return this;
    }

    public int entryOrder() {
        return entryOrder;
    }

    public KPiREntry setEntryOrder(int entryOrder) {
        this.entryOrder = entryOrder;
        return this;
    }

    public String bookingNumber() {
        return bookingNumber;
    }

    public KPiREntry setBookingNumber(String bookingNumber) {
        this.bookingNumber = bookingNumber;
        return this;
    }

    public String counterparty() {
        return counterparty;
    }

    public KPiREntry setCounterparty(String counterparty) {
        this.counterparty = counterparty;
        return this;
    }

    public String counterpartyAddress() {
        return counterpartyAddress;
    }

    public KPiREntry setCounterpartyAddress(String counterpartyAddress) {
        this.counterpartyAddress = counterpartyAddress;
        return this;
    }

    public String description() {
        return description;
    }

    public KPiREntry setDescription(String description) {
        this.description = description;
        return this;
    }

    public BigDecimal providedGoodsAndServicesValue() {
        return providedGoodsAndServicesValue;
    }

    public KPiREntry setProvidedGoodsAndServicesValue(BigDecimal providedGoodsAndServicesValue) {
        this.providedGoodsAndServicesValue = providedGoodsAndServicesValue;
        return this;
    }

    public BigDecimal otherIncomes() {
        return otherIncomes;
    }

    public KPiREntry setOtherIncomes(BigDecimal otherIncomes) {
        this.otherIncomes = otherIncomes;
        return this;
    }

    public BigDecimal totalIncomes() {
        return totalIncomes;
    }

    public KPiREntry setTotalIncomes(BigDecimal totalIncomes) {
        this.totalIncomes = totalIncomes;
        return this;
    }

    public BigDecimal purchasedGoodsAndMaterialsValue() {
        return purchasedGoodsAndMaterialsValue;
    }

    public KPiREntry setPurchasedGoodsAndMaterialsValue(BigDecimal purchasedGoodsAndMaterialsValue) {
        this.purchasedGoodsAndMaterialsValue = purchasedGoodsAndMaterialsValue;
        return this;
    }

    public BigDecimal additionalCostOfPurchase() {
        return additionalCostOfPurchase;
    }

    public KPiREntry setAdditionalCostOfPurchase(BigDecimal additionalCostOfPurchase) {
        this.additionalCostOfPurchase = additionalCostOfPurchase;
        return this;
    }

    public BigDecimal remunerationInCashOrInKind() {
        return remunerationInCashOrInKind;
    }

    public KPiREntry setRemunerationInCashOrInKind(BigDecimal remunerationInCashOrInKind) {
        this.remunerationInCashOrInKind = remunerationInCashOrInKind;
        return this;
    }

    public BigDecimal otherExpenses() {
        return otherExpenses;
    }

    public KPiREntry setOtherExpenses(BigDecimal otherExpenses) {
        this.otherExpenses = otherExpenses;
        return this;
    }

    public BigDecimal totalExpenses() {
        return totalExpenses;
    }

    public KPiREntry setTotalExpenses(BigDecimal totalExpenses) {
        this.totalExpenses = totalExpenses;
        return this;
    }

    public String comments() {
        return comments;
    }

    public KPiREntry setComments(String comments) {
        this.comments = comments;
        return this;
    }
}

