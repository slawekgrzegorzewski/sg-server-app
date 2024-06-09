package pl.sg.loans;

import com.netflix.graphql.dgs.DgsComponent;
import com.netflix.graphql.dgs.DgsQuery;
import com.netflix.graphql.dgs.InputArgument;
import org.jetbrains.annotations.NotNull;
import org.springframework.web.bind.annotation.RequestHeader;
import pl.sg.application.security.annotations.TokenBearerAuth;
import pl.sg.graphql.schema.types.LoanCalculationParams;
import pl.sg.loans.model.LoanCalculationInstallment;
import pl.sg.loans.service.LoanService;

import java.util.List;

@DgsComponent
public class LoanCalculatorDatafetcher {

    private final LoanService loanService;

    public LoanCalculatorDatafetcher(LoanService loanService) {
        this.loanService = loanService;
    }

    @DgsQuery
    @TokenBearerAuth(any = {"ACCOUNTANT_ADMIN", "ACCOUNTANT_USER"})
    public List<pl.sg.graphql.schema.types.LoanCalculationInstallment> simulateLoan(
            @InputArgument("loanCalculationParams") LoanCalculationParams loanCalculationParams
    ) {
        return loanService.simulate(loanCalculationParams)
                .stream()
                .map(LoanCalculatorDatafetcher::map)
                .toList();
    }

    @DgsQuery
    @TokenBearerAuth(any = {"ACCOUNTANT_ADMIN", "ACCOUNTANT_USER"})
    public List<pl.sg.graphql.schema.types.LoanCalculationInstallment> simulateExistingLoan(
            @RequestHeader("domainId") int domainId,
            @InputArgument("loanSimulationParams") pl.sg.graphql.schema.types.LoanSimulationParams loanSimulationParams
    ) {
        return loanService.simulateExistingLoan(loanSimulationParams, domainId)
                .stream()
                .map(LoanCalculatorDatafetcher::map)
                .toList();
    }

    @NotNull

    private static pl.sg.graphql.schema.types.LoanCalculationInstallment map(LoanCalculationInstallment installment) {
        return pl.sg.graphql.schema.types.LoanCalculationInstallment.newBuilder()
                .paymentFrom(installment.paymentFrom())
                .paymentTo(installment.paymentTo())
                .remainingCapitalAtTheBeginning(installment.remainingCapitalAtTheBeginning())
                .installment(installment.installment())
                .repaidCapital(installment.repaidCapital())
                .paidInterest(installment.paidInterest())
                .overpayment(installment.overpayment())
                .build();
    }
}