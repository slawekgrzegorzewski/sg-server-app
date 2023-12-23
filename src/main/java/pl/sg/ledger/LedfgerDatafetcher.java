package pl.sg.ledger;

import com.netflix.graphql.dgs.DgsComponent;
import com.netflix.graphql.dgs.DgsMutation;
import com.netflix.graphql.dgs.DgsQuery;
import com.netflix.graphql.dgs.InputArgument;
import jakarta.validation.constraints.NotNull;
import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.impl.DSL;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.NameTokenizers;
import org.modelmapper.jooq.RecordValueReader;
import org.springframework.web.bind.annotation.RequestHeader;
import pl.sg.application.model.Domain;
import pl.sg.application.security.annotations.TokenBearerAuth;
import pl.sg.graphql.schema.types.DomainSimple;
import pl.sg.graphql.schema.types.RevenueAndExpenseEntry;
import pl.sg.graphql.schema.types.RevenueAndExpenseEntryInput;

import java.time.YearMonth;
import java.util.HashMap;
import java.util.List;

import static pl.sg.jooq.tables.Domain.DOMAIN;
import static pl.sg.jooq.tables.RevenueAndExpenseEntries.REVENUE_AND_EXPENSE_ENTRIES;

@DgsComponent
public class LedfgerDatafetcher {

    private final DSLContext dslContext;
    private final ModelMapper modelMapper;

    public LedfgerDatafetcher(DSLContext dslContext, ModelMapper modelMapper) {
        this.dslContext = dslContext;
        this.modelMapper = modelMapper;
        modelMapper.getConfiguration().addValueReader(new RecordValueReader());
        modelMapper.getConfiguration().setSourceNameTokenizer(NameTokenizers.UNDERSCORE);
    }

    @DgsQuery
    @TokenBearerAuth(any = {"ACCOUNTANT_ADMIN", "ACCOUNTANT_USER"})
    public List<RevenueAndExpenseEntry> monthRevenueAndExpenseEntry(
            @InputArgument("year") int year,
            @InputArgument("month") int month,
            @RequestHeader("domainId") int domainId
    ) {
        YearMonth yearMonth = YearMonth.of(year, month);
        return dslContext.select(
                        REVENUE_AND_EXPENSE_ENTRIES.asterisk(),
                        DOMAIN.ID.as("domain_id"),
                        DOMAIN.NAME.as("domain_name")
                )
                .from(REVENUE_AND_EXPENSE_ENTRIES)
                .join(DOMAIN).on(DOMAIN.ID.eq(REVENUE_AND_EXPENSE_ENTRIES.DOMAIN_ID))
                .where(REVENUE_AND_EXPENSE_ENTRIES.DOMAIN_ID.eq(domainId))
                .and(REVENUE_AND_EXPENSE_ENTRIES.ENTRY_DATE.between(yearMonth.atDay(1), yearMonth.atEndOfMonth()))
                .fetch()
                .map(record -> modelMapper.map(record, RevenueAndExpenseEntry.class));
    }

    @DgsMutation
    @TokenBearerAuth(any = {"ACCOUNTANT_ADMIN", "ACCOUNTANT_USER"})
    public RevenueAndExpenseEntry addRevenueAndExpenseEntry(
            @InputArgument("input") RevenueAndExpenseEntryInput input,
            @RequestHeader("domainId") Domain domain
    ) {
        if (input.getPublicId() == null) {
            return dslContext.insertInto(REVENUE_AND_EXPENSE_ENTRIES)
                    .set(setFields(input, domain))
                    .returning(DSL.asterisk())
                    .fetchOne()
                    .map(record -> mapRevenueAndExpenseEntry(domain, record));
        } else {
            return dslContext.update(REVENUE_AND_EXPENSE_ENTRIES)
                    .set(setFields(input, domain))
                    .where(REVENUE_AND_EXPENSE_ENTRIES.PUBLIC_ID.eq(input.getPublicId()).and(REVENUE_AND_EXPENSE_ENTRIES.DOMAIN_ID.eq(domain.getId())))
                    .returning(DSL.asterisk())
                    .fetchOne()
                    .map(record -> mapRevenueAndExpenseEntry(domain, record));
        }
    }

    @NotNull
    private RevenueAndExpenseEntry mapRevenueAndExpenseEntry(Domain domain, Record record) {
        RevenueAndExpenseEntry newValue = modelMapper.map(record, RevenueAndExpenseEntry.class);
        newValue.setDomain(modelMapper.map(domain, DomainSimple.class));
        return newValue;
    }

    @NotNull
    private static HashMap<Object, Object> setFields(RevenueAndExpenseEntryInput input, Domain domain) {
        var setFields = new HashMap<>();
        if (input.getPublicId() != null) {
            setFields.put(REVENUE_AND_EXPENSE_ENTRIES.PUBLIC_ID, input.getPublicId());
        }
        setFields.put(REVENUE_AND_EXPENSE_ENTRIES.ENTRY_DATE, input.getEntryDate());
        setFields.put(REVENUE_AND_EXPENSE_ENTRIES.ENTRY_ORDER, input.getEntryOrder());
        setFields.put(REVENUE_AND_EXPENSE_ENTRIES.ACCOUNTING_DOCUMENT_NUMBER, input.getAccountingDocumentNumber());
        setFields.put(REVENUE_AND_EXPENSE_ENTRIES.COUNTERPARTY, input.getCounterparty());
        setFields.put(REVENUE_AND_EXPENSE_ENTRIES.COUNTERPARTY_ADDRESS, input.getCounterpartyAddress());
        setFields.put(REVENUE_AND_EXPENSE_ENTRIES.DESCRIPTION, input.getDescription());
        setFields.put(REVENUE_AND_EXPENSE_ENTRIES.PROVIDED_GOODS_AND_SERVICES_VALUE, input.getProvidedGoodsAndServicesValue());
        setFields.put(REVENUE_AND_EXPENSE_ENTRIES.OTHER_INCOME, input.getOtherIncome());
        setFields.put(REVENUE_AND_EXPENSE_ENTRIES.TOTAL_INCOME, input.getTotalIncome());
        setFields.put(REVENUE_AND_EXPENSE_ENTRIES.PURCHASED_GOODS_AND_MATERIALS_VALUE, input.getPurchasedGoodsAndMaterialsValue());
        setFields.put(REVENUE_AND_EXPENSE_ENTRIES.ADDITIONAL_COST_OF_PURCHASE, input.getAdditionalCostOfPurchase());
        setFields.put(REVENUE_AND_EXPENSE_ENTRIES.REMUNERATION_IN_CASH_OR_IN_KIND, input.getRemunerationInCashOrInKind());
        setFields.put(REVENUE_AND_EXPENSE_ENTRIES.OTHER_EXPENSE, input.getOtherExpense());
        setFields.put(REVENUE_AND_EXPENSE_ENTRIES.TOTAL_EXPENSE, input.getTotalExpense());
        setFields.put(REVENUE_AND_EXPENSE_ENTRIES.COMMENTS, input.getComments());
        setFields.put(REVENUE_AND_EXPENSE_ENTRIES.DOMAIN_ID, domain.getId());
        return setFields;
    }
}