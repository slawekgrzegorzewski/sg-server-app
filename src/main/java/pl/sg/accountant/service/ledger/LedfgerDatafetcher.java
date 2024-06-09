package pl.sg.accountant.service.ledger;

import com.netflix.graphql.dgs.DgsComponent;
import com.netflix.graphql.dgs.DgsMutation;
import com.netflix.graphql.dgs.DgsQuery;
import com.netflix.graphql.dgs.InputArgument;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.NameTokenizers;
import org.modelmapper.jooq.RecordValueReader;
import org.springframework.web.bind.annotation.RequestHeader;
import pl.sg.application.model.Domain;
import pl.sg.application.security.annotations.TokenBearerAuth;
import pl.sg.graphql.schema.types.LedgerQuery;
import pl.sg.graphql.schema.types.RevenueAndExpenseEntry;
import pl.sg.graphql.schema.types.RevenueAndExpenseEntryInput;

import java.time.YearMonth;
import java.util.List;

@DgsComponent
public class LedfgerDatafetcher {

    private final LedgerService ledgerService;
    private final ModelMapper modelMapper;

    public LedfgerDatafetcher(LedgerService ledgerService, ModelMapper modelMapper) {
        this.ledgerService = ledgerService;
        this.modelMapper = modelMapper;
        modelMapper.getConfiguration().addValueReader(new RecordValueReader());
        modelMapper.getConfiguration().setSourceNameTokenizer(NameTokenizers.UNDERSCORE);
    }

    @DgsQuery
    @TokenBearerAuth(any = {"ACCOUNTANT_ADMIN", "ACCOUNTANT_USER"})
    public List<RevenueAndExpenseEntry> monthLedger(
            @InputArgument("ledgerQuery") LedgerQuery ledgerQuery,
            @RequestHeader("domainId") int domainId
    ) {
        YearMonth yearMonth = YearMonth.of(ledgerQuery.getYearMonth().getYear(), ledgerQuery.getYearMonth().getMonth());
        return ledgerService.monthLedger(domainId, yearMonth)
                .stream()
                .map(record -> modelMapper.map(record, RevenueAndExpenseEntry.class))
                .toList();
    }

    @DgsMutation
    @TokenBearerAuth(any = {"ACCOUNTANT_ADMIN", "ACCOUNTANT_USER"})
    public RevenueAndExpenseEntry addRevenueAndExpenseEntry(
            @InputArgument("input") RevenueAndExpenseEntryInput input,
            @RequestHeader("domainId") Domain domain
    ) {
        return modelMapper.map(
                ledgerService.addRevenueAndExpenseEntry(
                        input.getPublicId(),
                        domain.getId(),
                        input.getEntryOrder(),
                        input.getEntryDate(),
                        input.getAccountingDocumentNumber(),
                        input.getCounterparty(),
                        input.getCounterpartyAddress(),
                        input.getDescription(),
                        input.getProvidedGoodsAndServicesValue(),
                        input.getOtherIncome(),
                        input.getTotalIncome(),
                        input.getPurchasedGoodsAndMaterialsValue(),
                        input.getAdditionalCostOfPurchase(),
                        input.getRemunerationInCashOrInKind(),
                        input.getOtherExpense(),
                        input.getTotalExpense(),
                        input.getComments()
                ),
                RevenueAndExpenseEntry.class);
    }
}