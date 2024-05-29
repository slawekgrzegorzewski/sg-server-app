package pl.sg.loans.service.rate;

import pl.sg.loans.model.Loan;
import pl.sg.loans.utils.LoansException;
import pl.sg.loans.utils.RateStrategy;

import java.math.BigDecimal;

public class ConstantForNFirstInstallmentRateStrategy implements RateStrategy {
    private final Loan loan;
    private final BigDecimal constantRate;
    private final BigDecimal variableRateMargin;
    private final int becomesVariableRateAfterNInstallments;

    public ConstantForNFirstInstallmentRateStrategy(Loan loan, BigDecimal constantRate, BigDecimal variableRateMargin, int becomesVariableRateAfterNInstallments) {
        this.loan = loan;
        this.constantRate = constantRate;
        this.variableRateMargin = variableRateMargin;
        this.becomesVariableRateAfterNInstallments = becomesVariableRateAfterNInstallments;
    }

    public BigDecimal getNextInstallmentRate() {
        if (loan.getInstallments().size() > becomesVariableRateAfterNInstallments) {
            throw new LoansException("No wibor source implemented yet");
        }
        return constantRate;
    }

    @Override
    public BigDecimal getNextInstallmentPercent() {
        return getNextInstallmentRate().multiply(new BigDecimal("100"));
    }
}
