package pl.sg.mortgage;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import pl.sg.graphql.schema.types.MortgageCalculationInstallment;
import pl.sg.graphql.schema.types.MortgageCalculationParams;
import pl.sg.mortgage.overpayment.MortgageOverpaymentStrategyFactory;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static java.math.BigDecimal.*;
import static java.math.RoundingMode.HALF_DOWN;
import static pl.sg.mortgage.LoanCalculatorConfig.LESS_PRECISE_SCALE;
import static pl.sg.mortgage.LoanCalculatorConfig.MORE_PRECISE_SCALE;

@Component
public class MortgageSimulator {
    private static final BigDecimal HUNDRED = new BigDecimal("100");

    private final MortgageOverpaymentStrategyFactory mortgageOverpaymentStrategyFactory;
    private final LoanInstalmentCalculator loanInstalmentCalculator;
    private final int lessPreciseScale;
    private final int morePreciseScale;

    public MortgageSimulator(
            LoanInstalmentCalculator loanInstalmentCalculator,
            MortgageOverpaymentStrategyFactory mortgageOverpaymentStrategyFactory,
            @Qualifier(LESS_PRECISE_SCALE) int lessPreciseScale,
            @Qualifier(MORE_PRECISE_SCALE) int morePreciseScale) {
        this.loanInstalmentCalculator = loanInstalmentCalculator;
        this.mortgageOverpaymentStrategyFactory = mortgageOverpaymentStrategyFactory;
        this.lessPreciseScale = lessPreciseScale;
        this.morePreciseScale = morePreciseScale;
    }

    public List<MortgageCalculationInstallment> simulate(MortgageCalculationParams mortgageCalculationParams) {
        List<MortgageCalculationInstallment> installments = new ArrayList<>();
        BigDecimal leftToRepay = mortgageCalculationParams.getMortgageAmount();
        BigDecimal annualRate = mortgageCalculationParams.getWibor().add(mortgageCalculationParams.getRate()).divide(HUNDRED, morePreciseScale, HALF_DOWN);

        BigDecimal installmentAmount = loanInstalmentCalculator.calculateInstallmentAmount(leftToRepay, annualRate, mortgageCalculationParams.getNumberOfInstallments(), mortgageCalculationParams.getRepaymentStart());

        while (leftToRepay.compareTo(ZERO) > 0) {
            LocalDate installmentStartDate = installments.isEmpty()
                    ? mortgageCalculationParams.getRepaymentStart()
                    : installments.get(installments.size() - 1).getPaymentTo().plusDays(1);

            LocalDate installmentEndDate = installmentStartDate.plusMonths(1).withDayOfMonth(1);

            if (installmentAmount.compareTo(ZERO) == 0) {
                installmentAmount = loanInstalmentCalculator.calculateInstallmentAmount(leftToRepay, annualRate, mortgageCalculationParams.getNumberOfInstallments() - installments.size(), installmentStartDate);
            }

            BigDecimal interestToPay = loanInstalmentCalculator.calculateInterest(leftToRepay, annualRate, installmentStartDate, installmentEndDate);

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
                            .remainingCapitalAtTheBeginning(leftToRepay.setScale(lessPreciseScale, HALF_DOWN))
                            .installment(installmentAmount.setScale(lessPreciseScale, HALF_DOWN))
                            .paidInterest(interestToPay.setScale(lessPreciseScale, HALF_DOWN))
                            .repaidCapital(repaidCapital.setScale(lessPreciseScale, HALF_DOWN))
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

            BigDecimal interestToPay = loanInstalmentCalculator.calculateInterest(leftToRepay, annualRate, installmentStartDate, installmentEndDate);

            installments.add(
                    MortgageCalculationInstallment.newBuilder()
                            .paymentFrom(installmentStartDate)
                            .paymentTo(installmentEndDate)
                            .remainingCapitalAtTheBeginning(leftToRepay.setScale(lessPreciseScale, HALF_DOWN))
                            .installment(interestToPay.add(leftToRepay))
                            .paidInterest(interestToPay.setScale(lessPreciseScale, HALF_DOWN))
                            .repaidCapital(leftToRepay.setScale(lessPreciseScale, HALF_DOWN))
                            .overpayment(ZERO)
                            .build()
            );
        }
        return installments;
    }

}
