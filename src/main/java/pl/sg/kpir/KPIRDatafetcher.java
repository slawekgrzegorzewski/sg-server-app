package pl.sg.kpir;

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
import pl.sg.graphql.schema.types.KPiREntry;
import pl.sg.graphql.schema.types.KPiREntryInput;

import java.time.YearMonth;
import java.util.HashMap;
import java.util.List;

import static pl.sg.jooq.tables.Domain.DOMAIN;
import static pl.sg.jooq.tables.KpirEntry.KPIR_ENTRY;

@DgsComponent
public class KPIRDatafetcher {

    private final DSLContext dslContext;
    private final ModelMapper modelMapper;

    public KPIRDatafetcher(DSLContext dslContext, ModelMapper modelMapper) {
        this.dslContext = dslContext;
        this.modelMapper = modelMapper;
        modelMapper.getConfiguration().addValueReader(new RecordValueReader());
        modelMapper.getConfiguration().setSourceNameTokenizer(NameTokenizers.UNDERSCORE);
    }

    @DgsQuery
    @TokenBearerAuth(any = {"ACCOUNTANT_ADMIN", "ACCOUNTANT_USER"})
    public List<KPiREntry> monthKPIR(
            @InputArgument int year,
            @InputArgument int month,
            @RequestHeader("domainId") int domainId
    ) {
        YearMonth yearMonth = YearMonth.of(year, month);
        return dslContext.select(
                        KPIR_ENTRY.asterisk(),
                        DOMAIN.ID.as("domain_id"),
                        DOMAIN.NAME.as("domain_name")
                )
                .from(KPIR_ENTRY)
                .join(DOMAIN).on(DOMAIN.ID.eq(KPIR_ENTRY.DOMAIN_ID))
                .where(KPIR_ENTRY.DOMAIN_ID.eq(domainId))
                .and(KPIR_ENTRY.ENTRY_DATE.between(yearMonth.atDay(1), yearMonth.atEndOfMonth()))
                .fetch()
                .map(record -> modelMapper.map(record, KPiREntry.class));
    }

    @DgsMutation
    @TokenBearerAuth(any = {"ACCOUNTANT_ADMIN", "ACCOUNTANT_USER"})
    public KPiREntry addKPiREntry(
            @InputArgument KPiREntryInput input,
            @RequestHeader("domainId") Domain domain
    ) {
        if (input.getPublicId() == null) {
            return dslContext.insertInto(KPIR_ENTRY)
                    .set(setFields(input, domain))
                    .returning(DSL.asterisk())
                    .fetchOne()
                    .map(record -> mapKPiREntry(domain, record));
        } else {
            return dslContext.update(KPIR_ENTRY)
                    .set(setFields(input, domain))
                    .where(KPIR_ENTRY.PUBLIC_ID.eq(input.getPublicId()).and(KPIR_ENTRY.DOMAIN_ID.eq(domain.getId())))
                    .returning(DSL.asterisk())
                    .fetchOne()
                    .map(record -> mapKPiREntry(domain, record));
        }
    }

    @NotNull
    private KPiREntry mapKPiREntry(Domain domain, Record record) {
        KPiREntry newValue = modelMapper.map(record, KPiREntry.class);
        newValue.setDomain(modelMapper.map(domain, DomainSimple.class));
        return newValue;
    }

    @NotNull
    private static HashMap<Object, Object> setFields(KPiREntryInput input, Domain domain) {
        var setFields = new HashMap<>();
        if (input.getPublicId() != null) {
            setFields.put(KPIR_ENTRY.PUBLIC_ID, input.getPublicId());
        }
        setFields.put(KPIR_ENTRY.ENTRY_DATE, input.getEntryDate());
        setFields.put(KPIR_ENTRY.ENTRY_ORDER, input.getEntryOrder());
        setFields.put(KPIR_ENTRY.BOOKING_NUMBER, input.getBookingNumber());
        setFields.put(KPIR_ENTRY.COUNTERPARTY, input.getCounterparty());
        setFields.put(KPIR_ENTRY.COUNTERPARTY_ADDRESS, input.getCounterpartyAddress());
        setFields.put(KPIR_ENTRY.DESCRIPTION, input.getDescription());
        setFields.put(KPIR_ENTRY.PROVIDED_GOODS_AND_SERVICES_VALUE, input.getProvidedGoodsAndServicesValue());
        setFields.put(KPIR_ENTRY.OTHER_INCOMES, input.getOtherIncomes());
        setFields.put(KPIR_ENTRY.TOTAL_INCOMES, input.getTotalIncomes());
        setFields.put(KPIR_ENTRY.PURCHASED_GOODS_AND_MATERIALS_VALUE, input.getPurchasedGoodsAndMaterialsValue());
        setFields.put(KPIR_ENTRY.ADDITIONAL_COST_OF_PURCHASE, input.getAdditionalCostOfPurchase());
        setFields.put(KPIR_ENTRY.REMUNERATION_IN_CASH_OR_IN_KIND, input.getRemunerationInCashOrInKind());
        setFields.put(KPIR_ENTRY.OTHER_EXPENSES, input.getOtherExpenses());
        setFields.put(KPIR_ENTRY.TOTAL_EXPENSES, input.getTotalExpenses());
        setFields.put(KPIR_ENTRY.COMMENTS, input.getComments());
        setFields.put(KPIR_ENTRY.DOMAIN_ID, domain.getId());
        return setFields;
    }
}