package pl.sg.mortgage.overpayment;

import java.math.BigDecimal;

public class NoneMortgageOverpaymentStrategy implements MortgageOverpaymentStrategy {
    @Override
    public BigDecimal calculateOverpayment(int installmentNumber, BigDecimal installmentAmount) {
        return BigDecimal.ZERO;
    }
}
