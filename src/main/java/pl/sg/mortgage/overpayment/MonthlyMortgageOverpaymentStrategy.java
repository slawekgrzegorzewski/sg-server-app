package pl.sg.mortgage.overpayment;

import java.math.BigDecimal;

public class MonthlyMortgageOverpaymentStrategy implements MortgageOverpaymentStrategy {
    private final BigDecimal overpaymentMonthlyBudget;

    public MonthlyMortgageOverpaymentStrategy(BigDecimal overpaymentMonthlyBudget) {

        this.overpaymentMonthlyBudget = overpaymentMonthlyBudget;
    }

    @Override
    public BigDecimal calculateOverpayment(int installmentNumber, BigDecimal installmentAmount) {
        BigDecimal result = overpaymentMonthlyBudget.subtract(installmentAmount);
        return result.compareTo(BigDecimal.ZERO) > 0 ? result : BigDecimal.ZERO;
    }
}
