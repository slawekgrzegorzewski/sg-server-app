package pl.sg.mortgage;

import org.springframework.stereotype.Component;
import pl.sg.graphql.schema.types.MortgageCalculationInstallment;
import pl.sg.graphql.schema.types.MortgageCalculationParams;
import pl.sg.mortgage.overpayment.MortgageOverpaymentStrategyFactory;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.Year;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;

import static java.math.BigDecimal.ZERO;
import static java.time.temporal.ChronoUnit.DAYS;

@Component
public class MortgageSimulator {

    private static final BigDecimal TWELVE = new BigDecimal("12");
    private static final BigDecimal HUNDRED = new BigDecimal("100");
    private final int SCALE_OF_FORTY = 40;
    private final int SCALE_OF_TWO = 2;

    private final MortgageOverpaymentStrategyFactory mortgageOverpaymentStrategyFactory;

    public MortgageSimulator(MortgageOverpaymentStrategyFactory mortgageOverpaymentStrategyFactory) {
        this.mortgageOverpaymentStrategyFactory = mortgageOverpaymentStrategyFactory;
    }

    public List<MortgageCalculationInstallment> simulate(MortgageCalculationParams mortgageCalculationParams) {
        List<MortgageCalculationInstallment> installments = new ArrayList<>();
        BigDecimal leftToRepay = mortgageCalculationParams.getMortgageAmount();
        BigDecimal annualRate = mortgageCalculationParams.getWibor().add(mortgageCalculationParams.getRate()).divide(HUNDRED, SCALE_OF_FORTY, RoundingMode.HALF_DOWN);
        BigDecimal monthlyRate = annualRate.divide(TWELVE, SCALE_OF_FORTY, RoundingMode.HALF_DOWN);
        BigDecimal installmentAmount = null;
        int installmentsFromLastHolidays = 0;
        while (installments.size() < mortgageCalculationParams.getNumberOfInstallments()) {

            LocalDate installmentStartDate = installments.isEmpty() ? mortgageCalculationParams.getRepaymentStart() : installments.get(installments.size() - 1).getPaymentTo().plusDays(1);
            LocalDate installmentEndDate = installmentStartDate.plusMonths(1).minusDays(1);

            boolean holidays = mortgageCalculationParams.getHolidaysMonthAfterNumberOfInstallments() != null
                               && mortgageCalculationParams.getHolidaysMonthAfterNumberOfInstallments() == installmentsFromLastHolidays;

            if (holidays) {
                installmentsFromLastHolidays = -1;
                installmentAmount = ZERO;
            } else if (installmentAmount == null) {
                installmentAmount = calculateInstallmentAmount(leftToRepay, monthlyRate, mortgageCalculationParams.getNumberOfInstallments() - installments.size());
            }

            BigDecimal interestToPay = holidays ? BigDecimal.ZERO : calculateInterest(leftToRepay, annualRate, installmentStartDate, installmentEndDate);

            BigDecimal repaidCapital = installmentAmount.subtract(interestToPay);
            BigDecimal overpayment = mortgageOverpaymentStrategyFactory.create(mortgageCalculationParams).calculateOverpayment(installments.size() + 1, installmentAmount);

            if (leftToRepay.subtract(repaidCapital).compareTo(ZERO) < 0) {
                installmentAmount = leftToRepay.add(interestToPay);
                repaidCapital = leftToRepay;
                overpayment = ZERO;
            } else if (leftToRepay.subtract(repaidCapital).subtract(overpayment).compareTo(ZERO) < 0) {
                overpayment = leftToRepay.subtract(repaidCapital);
            }

            installments.add(MortgageCalculationInstallment.newBuilder().paymentFrom(installmentStartDate).paymentTo(installmentEndDate).remainingCapitalAtTheBeginning(leftToRepay.setScale(SCALE_OF_TWO, RoundingMode.HALF_UP)).installment(installmentAmount.setScale(SCALE_OF_TWO, RoundingMode.HALF_UP)).paidInterest(interestToPay.setScale(SCALE_OF_TWO, RoundingMode.HALF_UP)).repaidCapital(repaidCapital.setScale(SCALE_OF_TWO, RoundingMode.HALF_UP)).overpayment(overpayment).build());

            installmentsFromLastHolidays++;

            leftToRepay = leftToRepay.subtract(repaidCapital).subtract(overpayment);
            if (overpayment.compareTo(ZERO) > 0 || holidays) {
                installmentAmount = null;
            }
            if (leftToRepay.compareTo(ZERO) <= 0) {
                break;
            }
        }
        if (leftToRepay.compareTo(ZERO) > 0) {

            LocalDate installmentStartDate = installments.isEmpty() ? mortgageCalculationParams.getRepaymentStart() : installments.get(installments.size() - 1).getPaymentTo().plusDays(1);
            LocalDate installmentEndDate = installmentStartDate.plusMonths(1).minusDays(1);

            BigDecimal interestToPay = calculateInterest(leftToRepay, annualRate, installmentStartDate, installmentEndDate);

            installments.add(MortgageCalculationInstallment.newBuilder().paymentFrom(installmentStartDate).paymentTo(installmentEndDate).remainingCapitalAtTheBeginning(leftToRepay.setScale(SCALE_OF_TWO, RoundingMode.HALF_UP)).installment(interestToPay.add(leftToRepay)).paidInterest(interestToPay.setScale(SCALE_OF_TWO, RoundingMode.HALF_UP)).repaidCapital(leftToRepay.setScale(SCALE_OF_TWO, RoundingMode.HALF_UP)).overpayment(ZERO).build());
        }
        return installments;
    }

    private BigDecimal calculateInterest(BigDecimal leftToRepay, BigDecimal annualRate, LocalDate installmentStartDate, LocalDate installmentEndDate) {
        if (YearMonth.from(installmentStartDate).equals(YearMonth.from(installmentEndDate))) {
            BigDecimal dailyRate = annualRate.divide(BigDecimal.valueOf(Year.from(installmentStartDate).length()), SCALE_OF_FORTY, RoundingMode.HALF_DOWN);
            BigDecimal numberOfDaysBetween = BigDecimal.valueOf(DAYS.between(installmentStartDate, installmentEndDate.plusDays(1)));
            return leftToRepay.multiply(dailyRate).multiply(numberOfDaysBetween);
        }
        BigDecimal left = calculateInterest(leftToRepay, annualRate, installmentStartDate, YearMonth.from(installmentStartDate).atEndOfMonth());
        BigDecimal right = calculateInterest(leftToRepay, annualRate, YearMonth.from(installmentStartDate).atEndOfMonth().plusDays(1), installmentEndDate);
        return left.add(right).setScale(SCALE_OF_TWO, RoundingMode.HALF_UP);
    }

    private BigDecimal calculateInstallmentAmount(BigDecimal mortgageAmount, BigDecimal monthlyRate, int numberOfInstallments) {
        BigDecimal factor = monthlyRate.add(BigDecimal.ONE).pow(numberOfInstallments);
        BigDecimal rate = monthlyRate.multiply(factor).divide(factor.subtract(BigDecimal.ONE), SCALE_OF_FORTY, RoundingMode.HALF_DOWN);
        return mortgageAmount.multiply(rate);
    }

}
