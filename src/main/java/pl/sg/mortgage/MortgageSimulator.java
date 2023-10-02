package pl.sg.mortgage;

import org.springframework.stereotype.Component;
import pl.sg.graphql.schema.types.MortgageCalculationInstallment;
import pl.sg.graphql.schema.types.MortgageCalculationParams;
import pl.sg.mortgage.overpayment.MortgageOverpaymentStrategyFactory;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Year;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;

import static java.math.BigDecimal.*;
import static java.math.RoundingMode.HALF_DOWN;
import static java.time.temporal.ChronoUnit.DAYS;

@Component
public class MortgageSimulator {
    private static final BigDecimal HUNDRED = new BigDecimal("100");
    private final int SCALE_OF_FOURTY = 40;
    private final int SCALE_OF_TWO = 2;

    private final MortgageOverpaymentStrategyFactory mortgageOverpaymentStrategyFactory;

    public MortgageSimulator(MortgageOverpaymentStrategyFactory mortgageOverpaymentStrategyFactory) {
        this.mortgageOverpaymentStrategyFactory = mortgageOverpaymentStrategyFactory;
    }

    public List<MortgageCalculationInstallment> simulate(MortgageCalculationParams mortgageCalculationParams) {
        List<MortgageCalculationInstallment> installments = new ArrayList<>();
        BigDecimal leftToRepay = mortgageCalculationParams.getMortgageAmount();
        BigDecimal annualRate = mortgageCalculationParams.getWibor().add(mortgageCalculationParams.getRate()).divide(HUNDRED, SCALE_OF_FOURTY, HALF_DOWN);

        BigDecimal installmentAmount = calculateInstallmentAmount(leftToRepay, annualRate, mortgageCalculationParams.getNumberOfInstallments(), mortgageCalculationParams.getRepaymentStart());

        while (leftToRepay.compareTo(ZERO) > 0) {
            LocalDate installmentStartDate = installments.isEmpty()
                    ? mortgageCalculationParams.getRepaymentStart()
                    : installments.get(installments.size() - 1).getPaymentTo().plusDays(1);

            LocalDate installmentEndDate = installmentStartDate.plusMonths(1).withDayOfMonth(1);

            if (installmentAmount.compareTo(ZERO) == 0) {
                installmentAmount = calculateInstallmentAmount(leftToRepay, annualRate, mortgageCalculationParams.getNumberOfInstallments() - installments.size(), installmentStartDate);
            }

            BigDecimal interestToPay = calculateInterest(leftToRepay, annualRate, installmentStartDate, installmentEndDate);

            BigDecimal repaidCapital = installmentAmount.subtract(interestToPay);
            BigDecimal overpayment = mortgageOverpaymentStrategyFactory
                    .create(mortgageCalculationParams)
                    .calculateOverpayment(installments.size() + 1, installmentAmount);

            if (leftToRepay.subtract(repaidCapital).compareTo(ZERO) < 0) {
                installmentAmount = leftToRepay.add(interestToPay);
                repaidCapital = leftToRepay;
                overpayment = ZERO;
            } else if (leftToRepay.subtract(repaidCapital).subtract(overpayment).compareTo(ZERO) < 0) {
                overpayment = leftToRepay.subtract(repaidCapital);
            }

            installments.add(
                    MortgageCalculationInstallment.newBuilder()
                            .paymentFrom(installmentStartDate)
                            .paymentTo(installmentEndDate)
                            .remainingCapitalAtTheBeginning(leftToRepay.setScale(SCALE_OF_TWO, HALF_DOWN))
                            .installment(installmentAmount.setScale(SCALE_OF_TWO, HALF_DOWN))
                            .paidInterest(interestToPay.setScale(SCALE_OF_TWO, HALF_DOWN))
                            .repaidCapital(repaidCapital.setScale(SCALE_OF_TWO, HALF_DOWN))
                            .overpayment(overpayment)
                            .build()
            );

            leftToRepay = leftToRepay.subtract(repaidCapital).subtract(overpayment);
            if (leftToRepay.compareTo(ZERO) <= 0) {
                break;
            }
            if (overpayment.compareTo(ZERO) > 0) {
                installmentAmount = ZERO;
            }
        }
        if (leftToRepay.compareTo(ZERO) > 0) {
            LocalDate installmentStartDate = installments.isEmpty()
                    ? mortgageCalculationParams.getRepaymentStart()
                    : installments.get(installments.size() - 1).getPaymentTo().plusDays(1);
            LocalDate installmentEndDate = installmentStartDate.plusMonths(1).minusDays(1);

            BigDecimal interestToPay = calculateInterest(leftToRepay, annualRate, installmentStartDate, installmentEndDate);

            installments.add(
                    MortgageCalculationInstallment.newBuilder()
                            .paymentFrom(installmentStartDate)
                            .paymentTo(installmentEndDate)
                            .remainingCapitalAtTheBeginning(leftToRepay.setScale(SCALE_OF_TWO, HALF_DOWN))
                            .installment(interestToPay.add(leftToRepay))
                            .paidInterest(interestToPay.setScale(SCALE_OF_TWO, HALF_DOWN))
                            .repaidCapital(leftToRepay.setScale(SCALE_OF_TWO, HALF_DOWN))
                            .overpayment(ZERO)
                            .build()
            );
        }
        return installments;
    }

    private BigDecimal calculateInstallmentAmount(BigDecimal mortgageAmount, BigDecimal annualRate, int numberOfInstallments, LocalDate startOfRepayment) {
        if (numberOfInstallments == 0) {
            return mortgageAmount;
        }

        LocalDate from = startOfRepayment;
        BigDecimal sumOfFactors = ZERO;
        for (int i = 1; i <= numberOfInstallments; i++) {
            LocalDate to = i == numberOfInstallments
                    ? from.plusMonths(1).withDayOfMonth(startOfRepayment.getDayOfMonth())
                    : from.plusMonths(1).withDayOfMonth(1);
            BigDecimal periodRate = calculatePeriodRate(annualRate, from, to);
            BigDecimal onePlusPeriodRate = periodRate.add(ONE);
            BigDecimal onePlusPeriodRatePowerOfInstallment = onePlusPeriodRate.pow(i);
            sumOfFactors = sumOfFactors.add(ONE.divide(onePlusPeriodRatePowerOfInstallment, SCALE_OF_FOURTY, HALF_DOWN));
            from = to.plusDays(1);
        }
        return mortgageAmount.divide(sumOfFactors, SCALE_OF_TWO, HALF_DOWN);
    }

    private BigDecimal calculateInterest(BigDecimal leftToRepay, BigDecimal annualRate, LocalDate installmentStartDate, LocalDate installmentEndDate) {
        return calculatePeriodRate(annualRate, installmentStartDate, installmentEndDate).multiply(leftToRepay).setScale(SCALE_OF_TWO, HALF_DOWN);
    }

    private BigDecimal calculatePeriodRate(BigDecimal annualRate, LocalDate fromInclusive, LocalDate toInclusive) {
        if (theSameMonth(fromInclusive, toInclusive)) {
            BigDecimal daysBetween = BigDecimal.valueOf(DAYS.between(fromInclusive, toInclusive.plusDays(1))).setScale(SCALE_OF_FOURTY, HALF_DOWN);
            BigDecimal dailyRate = annualRate.divide(daysInYear(fromInclusive), SCALE_OF_FOURTY, HALF_DOWN);
            return daysBetween.multiply(dailyRate);
        }
        return calculatePeriodRate(annualRate, fromInclusive, endOfMonth(fromInclusive))
                .add(calculatePeriodRate(annualRate, beginningOfNextMonth(fromInclusive), toInclusive));
    }

    private static LocalDate endOfMonth(LocalDate installmentStartDate) {
        return YearMonth.from(installmentStartDate).atEndOfMonth();
    }

    private static LocalDate beginningOfNextMonth(LocalDate installmentStartDate) {
        return YearMonth.from(installmentStartDate).atEndOfMonth().plusDays(1);
    }

    private static BigDecimal daysInYear(LocalDate installmentStartDate) {
        return BigDecimal.valueOf(Year.from(installmentStartDate).length());
    }

    private static boolean theSameMonth(LocalDate installmentStartDate, LocalDate installmentEndDate) {
        return YearMonth.from(installmentStartDate).equals(YearMonth.from(installmentEndDate));
    }

}
