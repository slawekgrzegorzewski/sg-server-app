package pl.sg.loans;

import com.netflix.graphql.dgs.DgsComponent;
import com.netflix.graphql.dgs.DgsQuery;
import com.netflix.graphql.dgs.InputArgument;
import org.jetbrains.annotations.NotNull;
import pl.sg.application.security.annotations.TokenBearerAuth;
import pl.sg.loans.calculator.LoanSimulator;
import pl.sg.loans.model.LoanCalculationInstallment;
import pl.sg.loans.model.LoanCalculationParams;

import java.util.List;

@DgsComponent
public class LoanCalculatorDatafetcher {
    private final LoanSimulator loanSimulator;

    public LoanCalculatorDatafetcher(LoanSimulator loanSimulator) {
        this.loanSimulator = loanSimulator;
    }

    @DgsQuery
    @TokenBearerAuth(any = {"ACCOUNTANT_ADMIN", "ACCOUNTANT_USER"})
    public List<pl.sg.graphql.schema.types.LoanCalculationInstallment> simulateLoan(
            @InputArgument("loanCalculationParams") pl.sg.graphql.schema.types.LoanCalculationParams loanCalculationParams
    ) {
        return loanSimulator.simulate(map(loanCalculationParams))
                .stream()
                .map(LoanCalculatorDatafetcher::map)
                .toList();
    }

    @NotNull
    private static LoanCalculationParams map(pl.sg.graphql.schema.types.LoanCalculationParams loanCalculationParams) {
        return new LoanCalculationParams(
                loanCalculationParams.getLoanAmount(),
                loanCalculationParams.getRepaymentStart(),
                loanCalculationParams.getRate(),
                loanCalculationParams.getWibor(),
                loanCalculationParams.getNumberOfInstallments(),
                loanCalculationParams.getOverpaymentMonthlyBudget(),
                loanCalculationParams.getOverpaymentYearlyBudget());
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