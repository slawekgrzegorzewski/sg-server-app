package pl.sg.mortgage.overpayment;

import java.math.BigDecimal;

public class YearlyMortgageOverpaymentStrategy implements MortgageOverpaymentStrategy {
    private final BigDecimal overpaymentYearlyBudget;
    private final BigDecimal overpaymentMonthlyBudget;

    public YearlyMortgageOverpaymentStrategy(BigDecimal overpaymentYearlyBudget, BigDecimal overpaymentMonthlyBudget) {

        this.overpaymentYearlyBudget = overpaymentYearlyBudget;
        this.overpaymentMonthlyBudget = overpaymentMonthlyBudget;
    }

    @Override
    public BigDecimal calculateOverpayment(int installmentNumber, BigDecimal installmentAmount) {
        BigDecimal result = overpaymentMonthlyBudget.subtract(installmentAmount);
        if (installmentNumber % 12 == 0) {
            result = result.add(overpaymentYearlyBudget);
        }
        return result.compareTo(BigDecimal.ZERO) > 0 ? result : BigDecimal.ZERO;
    }
}
