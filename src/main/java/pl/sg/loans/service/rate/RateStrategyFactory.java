package pl.sg.loans.service.rate;

import org.springframework.stereotype.Service;
import pl.sg.loans.model.ConstantForNFirstInstallmentRateStrategyConfig;
import pl.sg.loans.model.Loan;
import pl.sg.loans.utils.LoansException;
import pl.sg.loans.utils.RateStrategy;

@Service
public class RateStrategyFactory {
    public RateStrategy create(Loan loan) {
        if (loan.getRateStrategyConfig() instanceof ConstantForNFirstInstallmentRateStrategyConfig config) {
            return new ConstantForNFirstInstallmentRateStrategy(loan, config.getConstantRate(), config.getVariableRateMargin(), config.getBecomesVariableRateAfterNInstallments());
        }
        throw new LoansException("Not known rate strategy config: " + loan.getRateStrategyConfig().getClass());
    }
}
