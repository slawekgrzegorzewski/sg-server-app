package pl.sg.mortgage.overpayment;

import org.springframework.stereotype.Component;
import pl.sg.graphql.schema.types.MortgageCalculationParams;

import java.math.BigDecimal;

@Component
public class MortgageOverpaymentStrategyFactoryImpl implements MortgageOverpaymentStrategyFactory {

    @Override
    public MortgageOverpaymentStrategy create(MortgageCalculationParams mortgageCalculationParams) {
        boolean yearly = hasYearlyBudgetSet(mortgageCalculationParams);
        boolean monthly = hasMonthlyBudgetSet(mortgageCalculationParams);

        if (!yearly && monthly) {
            return new MonthlyMortgageOverpaymentStrategy(mortgageCalculationParams.getOverpaymentMonthlyBudget());
        } else if (yearly) {
            return new YearlyMortgageOverpaymentStrategy(mortgageCalculationParams.getOverpaymentYearlyBudget(), mortgageCalculationParams.getOverpaymentMonthlyBudget());
        } else {
            return new NoneMortgageOverpaymentStrategy();
        }
    }

    private boolean hasYearlyBudgetSet(MortgageCalculationParams mortgageCalculationParams) {
        return mortgageCalculationParams.getOverpaymentYearlyBudget().compareTo(BigDecimal.ZERO) > 0;
    }

    private boolean hasMonthlyBudgetSet(MortgageCalculationParams mortgageCalculationParams) {
        return mortgageCalculationParams.getOverpaymentMonthlyBudget().compareTo(BigDecimal.ZERO) > 0;
    }
}
