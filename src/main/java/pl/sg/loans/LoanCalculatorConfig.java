package pl.sg.loans;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import pl.sg.loans.calculator.LoanInstalmentCalculator;
import pl.sg.loans.calculator.LoanSimulator;
import pl.sg.loans.overpayment.LoanOverpaymentStrategyFactory;
import pl.sg.loans.overpayment.LoanOverpaymentStrategyFactoryImpl;

@Configuration
public class LoanCalculatorConfig {

    public static final String MORE_PRECISE_SCALE = "MORE_PRECISE_SCALE";
    public static final String LESS_PRECISE_SCALE = "LESS_PRECISE_SCALE";

    @Bean
    LoanOverpaymentStrategyFactory loanOverpaymentStrategyFactory() {
        return new LoanOverpaymentStrategyFactoryImpl();
    }

    @Bean(MORE_PRECISE_SCALE)
    int morePreciseScale() {
        return 10;
    }

    @Bean(LESS_PRECISE_SCALE)
    int lessPreciseScale() {
        return 2;
    }

    @Bean
    LoanInstalmentCalculator loanInstalmentCalculator(
            @Qualifier(LESS_PRECISE_SCALE) int lessPreciseScale,
            @Qualifier(MORE_PRECISE_SCALE) int morePreciseScale) {
        return new LoanInstalmentCalculator(lessPreciseScale, morePreciseScale);
    }

    @Bean
    LoanSimulator loanSimulator(
            LoanInstalmentCalculator loanInstalmentCalculator,
            LoanOverpaymentStrategyFactory loanOverpaymentStrategyFactory,
            @Qualifier(LESS_PRECISE_SCALE) int lessPreciseScale,
            @Qualifier(MORE_PRECISE_SCALE) int morePreciseScale) {
        return new LoanSimulator(
                loanInstalmentCalculator,
                loanOverpaymentStrategyFactory,
                lessPreciseScale,
                morePreciseScale);
    }
}
