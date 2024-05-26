package pl.sg.loans.service.repayment;

import org.springframework.stereotype.Service;
import pl.sg.loans.model.Loan;
import pl.sg.loans.utils.LoansException;
import pl.sg.loans.model.NthDayOfMonthRepaymentDayStrategyConfig;
import pl.sg.loans.utils.RepaymentDayStrategy;

@Service
public class RepaymentDayStrategyFactory {
    public RepaymentDayStrategy create(Loan loan) {
        if (loan.getRepaymentDayStrategyConfig() instanceof NthDayOfMonthRepaymentDayStrategyConfig strategyConfig) {
            return new NthDayOfMonthRepaymentDayStrategy(strategyConfig.getDayOfMonth());
        }
        throw new LoansException("Not known repayment day strategy config: " + loan.getRepaymentDayStrategyConfig().getClass());
    }
}
