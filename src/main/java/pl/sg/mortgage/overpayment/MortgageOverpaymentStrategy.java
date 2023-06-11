package pl.sg.mortgage.overpayment;

import java.math.BigDecimal;

public interface MortgageOverpaymentStrategy {
    BigDecimal calculateOverpayment(int installmentNumber, BigDecimal installmentAmount);
}
