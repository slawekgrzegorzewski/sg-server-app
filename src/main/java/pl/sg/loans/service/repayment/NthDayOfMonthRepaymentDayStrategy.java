package pl.sg.loans.service.repayment;

import pl.sg.loans.utils.RepaymentDayStrategy;

import java.time.LocalDate;

public class NthDayOfMonthRepaymentDayStrategy implements RepaymentDayStrategy {
    private final int dayNumber;

    public NthDayOfMonthRepaymentDayStrategy(int dayNumber) {
        this.dayNumber = dayNumber;
    }

    public LocalDate getNextPaymentDay(LocalDate lastPayment) {
        return lastPayment.plusMonths(1).withDayOfMonth(dayNumber);
    }
}
