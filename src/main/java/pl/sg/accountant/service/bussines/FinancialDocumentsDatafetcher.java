package pl.sg.accountant.service.bussines;

import com.netflix.graphql.dgs.DgsComponent;
import com.netflix.graphql.dgs.DgsMutation;
import com.netflix.graphql.dgs.DgsQuery;
import com.netflix.graphql.dgs.InputArgument;
import org.javamoney.moneta.Money;
import org.springframework.web.bind.annotation.RequestHeader;
import pl.sg.accountant.model.AccountsException;
import pl.sg.application.model.Domain;
import pl.sg.application.security.annotations.TokenBearerAuth;
import pl.sg.graphql.schema.types.*;

import javax.money.MonetaryAmount;
import java.math.BigDecimal;
import java.time.YearMonth;
import java.util.Currency;
import java.util.List;
import java.util.UUID;

@DgsComponent
public class FinancialDocumentsDatafetcher {

    private final FinancialDocumentsService financialDocumentsService;

    public FinancialDocumentsDatafetcher(FinancialDocumentsService financialDocumentsService) {
        this.financialDocumentsService = financialDocumentsService;
    }

    @DgsQuery
    @TokenBearerAuth(any = {"ACCOUNTANT_ADMIN", "ACCOUNTANT_USER"})
    public List<FinancialDocument> monthFinancialDocuments(
            @RequestHeader("domainId") int domainId,
            @InputArgument("yearMonthInput") YearMonthInput yearMonthInput
    ) {
        return financialDocumentsService.findAllByYearMonth(domainId, YearMonth.of(yearMonthInput.getYear(), yearMonthInput.getMonth()))
                .stream()
                .map(document -> {
                    if (document instanceof pl.sg.accountant.model.bussines.CreditInvoice creditInvoice)
                        return mapCreditInvoice(creditInvoice);
                    if (document instanceof pl.sg.accountant.model.bussines.DebitInvoice debitInvoice)
                        return mapDebitInvoice(debitInvoice);
                    if (document instanceof pl.sg.accountant.model.bussines.BillOfPurchase billOfPurchase)
                        return mapBillOfPurchase(billOfPurchase);
                    if (document instanceof pl.sg.accountant.model.bussines.BillOfSale billOfSale)
                        return mapBillOfSale(billOfSale);
                    else throw new AccountsException("Not known type of financial document: " + document.getClass());
                })
                .toList();
    }

    @DgsMutation
    @TokenBearerAuth(any = {"ACCOUNTANT_ADMIN", "ACCOUNTANT_USER"})
    public FinancialDocument createBill(
            @RequestHeader("domainId") int domainId,
            @InputArgument("billCreationInput") BillCreationInput billCreationInput
    ) {
        if (billCreationInput.getType() == BillType.BILL_OF_PURCHASE) {
            return mapBillOfPurchase(financialDocumentsService.createBillOfPurchase(
                    domainId,
                    billCreationInput.getOtherPartyId(),
                    billCreationInput.getCreatedAt(),
                    billCreationInput.getDescription(),
                    map(billCreationInput.getAmount()),
                    billCreationInput.getName()
            ));
        } else {
            pl.sg.accountant.model.bussines.BillOfSale billOfSale = financialDocumentsService.createBillOfSale(
                    domainId,
                    billCreationInput.getOtherPartyId(),
                    billCreationInput.getCreatedAt(),
                    billCreationInput.getDescription(),
                    map(billCreationInput.getAmount()),
                    billCreationInput.getName()
            );
            return mapBillOfSale(billOfSale);
        }
    }

    @DgsMutation
    @TokenBearerAuth(any = {"ACCOUNTANT_ADMIN", "ACCOUNTANT_USER"})
    public FinancialDocument updateBill(
            @RequestHeader("domainId") int domainId,
            @InputArgument("billUpdateInput") BillUpdateInput billUpdateInput
    ) {
        if (billUpdateInput.getType() == BillType.BILL_OF_PURCHASE) {
            return mapBillOfPurchase(financialDocumentsService.updateBillOfPurchase(
                    billUpdateInput.getBillPublicId(),
                    domainId,
                    billUpdateInput.getOtherPartyId(),
                    billUpdateInput.getCreatedAt(),
                    billUpdateInput.getDescription(),
                    map(billUpdateInput.getAmount()),
                    billUpdateInput.getName()
            ));
        } else {
            return mapBillOfSale(financialDocumentsService.updateBillOfSale(
                    billUpdateInput.getBillPublicId(),
                    domainId,
                    billUpdateInput.getOtherPartyId(),
                    billUpdateInput.getCreatedAt(),
                    billUpdateInput.getDescription(),
                    map(billUpdateInput.getAmount()),
                    billUpdateInput.getName()
            ));
        }
    }

    private BillOfPurchase mapBillOfPurchase(pl.sg.accountant.model.bussines.BillOfPurchase billOfPurchase) {
        return BillOfPurchase.newBuilder()
                .publicId(billOfPurchase.getPublicId())
                .name(billOfPurchase.getName())
                .otherParty(mapSupplier(billOfPurchase.getOtherParty()))
                .amount(map(billOfPurchase.getAmount()))
                .createdAt(billOfPurchase.getCreatedAt())
                .description(billOfPurchase.getDescription())
                .domain(map(billOfPurchase.getDomain()))
                .build();
    }

    private BillOfSale mapBillOfSale(pl.sg.accountant.model.bussines.BillOfSale billOfSale) {
        return BillOfSale.newBuilder()
                .publicId(billOfSale.getPublicId())
                .name(billOfSale.getName())
                .otherParty(mapClient(billOfSale.getOtherParty()))
                .amount(map(billOfSale.getAmount()))
                .createdAt(billOfSale.getCreatedAt())
                .description(billOfSale.getDescription())
                .domain(map(billOfSale.getDomain()))
                .build();
    }

    @DgsMutation
    @TokenBearerAuth(any = {"ACCOUNTANT_ADMIN", "ACCOUNTANT_USER"})
    public Invoice createInvoice(
            @RequestHeader("domainId") int domainId,
            @InputArgument("invoiceCreationInput") InvoiceCreationInput invoiceCreationInput
    ) {
        if (invoiceCreationInput.getType() == InvoiceType.CREDIT) {
            return mapCreditInvoice(financialDocumentsService.createCreditInvoice(
                    domainId,
                    invoiceCreationInput.getOtherPartyId(),
                    invoiceCreationInput.getCreatedAt(),
                    invoiceCreationInput.getDueTo(),
                    invoiceCreationInput.getDescription(),
                    map(invoiceCreationInput.getAmount()),
                    map(invoiceCreationInput.getVat()),
                    invoiceCreationInput.getName()
            ));
        } else {
            return mapDebitInvoice(financialDocumentsService.createDebitInvoice(
                    domainId,
                    invoiceCreationInput.getOtherPartyId(),
                    invoiceCreationInput.getCreatedAt(),
                    invoiceCreationInput.getDueTo(),
                    invoiceCreationInput.getDescription(),
                    map(invoiceCreationInput.getAmount()),
                    map(invoiceCreationInput.getVat()),
                    invoiceCreationInput.getName()
            ));
        }
    }

    @DgsMutation
    @TokenBearerAuth(any = {"ACCOUNTANT_ADMIN", "ACCOUNTANT_USER"})
    public Invoice updateInvoice(
            @RequestHeader("domainId") int domainId,
            @InputArgument("invoiceUpdateInput") InvoiceUpdateInput invoiceUpdateInput
    ) {
        if (invoiceUpdateInput.getType() == InvoiceType.CREDIT) {
            return mapCreditInvoice(financialDocumentsService.updateCreditInvoice(
                    invoiceUpdateInput.getInvoicePublicId(),
                    domainId,
                    invoiceUpdateInput.getOtherPartyId(),
                    invoiceUpdateInput.getCreatedAt(),
                    invoiceUpdateInput.getDueTo(),
                    invoiceUpdateInput.getDescription(),
                    map(invoiceUpdateInput.getAmount()),
                    map(invoiceUpdateInput.getVat()),
                    invoiceUpdateInput.getName()
            ));
        } else {
            return mapDebitInvoice(financialDocumentsService.updateDebitInvoice(
                    invoiceUpdateInput.getInvoicePublicId(),
                    domainId,
                    invoiceUpdateInput.getOtherPartyId(),
                    invoiceUpdateInput.getCreatedAt(),
                    invoiceUpdateInput.getDueTo(),
                    invoiceUpdateInput.getDescription(),
                    map(invoiceUpdateInput.getAmount()),
                    map(invoiceUpdateInput.getVat()),
                    invoiceUpdateInput.getName()
            ));
        }
    }

    private DebitInvoice mapDebitInvoice(pl.sg.accountant.model.bussines.DebitInvoice debitInvoice) {
        return DebitInvoice.newBuilder()
                .publicId(debitInvoice.getPublicId())
                .name(debitInvoice.getName())
                .otherParty(mapSupplier(debitInvoice.getOtherParty()))
                .amount(map(debitInvoice.getAmount()))
                .vat(map(debitInvoice.getVat()))
                .createdAt(debitInvoice.getCreatedAt())
                .dueTo(debitInvoice.getDueTo())
                .description(debitInvoice.getDescription())
                .domain(map(debitInvoice.getDomain()))
                .build();
    }

    private CreditInvoice mapCreditInvoice(pl.sg.accountant.model.bussines.CreditInvoice creditInvoice) {
        return CreditInvoice.newBuilder()
                .publicId(creditInvoice.getPublicId())
                .name(creditInvoice.getName())
                .otherParty(mapClient(creditInvoice.getOtherParty()))
                .amount(map(creditInvoice.getAmount()))
                .vat(map(creditInvoice.getVat()))
                .createdAt(creditInvoice.getCreatedAt())
                .dueTo(creditInvoice.getDueTo())
                .description(creditInvoice.getDescription())
                .domain(map(creditInvoice.getDomain()))
                .build();
    }

    @DgsMutation
    @TokenBearerAuth(any = {"ACCOUNTANT_ADMIN", "ACCOUNTANT_USER"})
    public String deleteFinancialDocument(
            @RequestHeader("domainId") int domainId,
            @InputArgument("financialDocumentPublicId") UUID financialDocumentPublicId
    ) {
        financialDocumentsService.delete(domainId, financialDocumentPublicId);
        return "OK";
    }

    private pl.sg.graphql.schema.types.MonetaryAmount map(MonetaryAmount amount) {
        return pl.sg.graphql.schema.types.MonetaryAmount.newBuilder()
                .amount(amount.getNumber().numberValue(BigDecimal.class))
                .currency(Currency.getInstance(amount.getCurrency().getCurrencyCode()))
                .build();
    }

    private static Supplier mapSupplier(pl.sg.accountant.model.bussines.OtherParty supplier) {
        if (!(supplier instanceof pl.sg.accountant.model.bussines.Supplier)) {
            throw new AccountsException("Other party was not a supplier");
        }
        return Supplier.newBuilder()
                .publicId(supplier.getPublicId())
                .name(supplier.getName())
                .domain(map(supplier.getDomain()))
                .build();
    }

    private static Client mapClient(pl.sg.accountant.model.bussines.OtherParty client) {
        if (!(client instanceof pl.sg.accountant.model.bussines.Client)) {
            throw new AccountsException("Other party was not a supplier");
        }
        return Client.newBuilder()
                .publicId(client.getPublicId())
                .name(client.getName())
                .domain(map(client.getDomain()))
                .build();
    }

    private static DomainSimple map(Domain domain) {
        return DomainSimple.newBuilder()
                .id(domain.getId())
                .name(domain.getName())
                .build();
    }

    private MonetaryAmount map(MonetaryAmountInput amount) {
        return Money.of(
                amount.getAmount(),
                amount.getCurrency().getCurrencyCode());
    }
}