package pl.sg.kpir;

import com.netflix.graphql.dgs.DgsComponent;
import com.netflix.graphql.dgs.DgsQuery;
import com.netflix.graphql.dgs.InputArgument;
import org.jooq.DSLContext;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.NameTokenizers;
import org.modelmapper.jooq.RecordValueReader;
import org.springframework.web.bind.annotation.RequestHeader;
import pl.sg.application.security.annotations.TokenBearerAuth;
import pl.sg.graphql.schema.types.KPIREntry;

import java.time.YearMonth;
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
    public List<KPIREntry> monthKPIR(
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
                .map(record -> modelMapper.map(record, KPIREntry.class));
    }
}