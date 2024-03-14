package pl.sg.mortgage;

import org.junit.jupiter.api.Test;
import pl.sg.mortgage.overpayment.MortgageOverpaymentStrategyFactory;

import static org.mockito.Mockito.mock;

class MortgageSimulatorTest {
    @Test
    void test() {
        MortgageOverpaymentStrategyFactory mortgageOverpaymentStrategyFactory = mock(MortgageOverpaymentStrategyFactory.class);
        LoanInstalmentCalculator loanInstalmentCalculator = mock(LoanInstalmentCalculator.class);
        MortgageSimulator mortgageSimulator = new MortgageSimulator(loanInstalmentCalculator, mortgageOverpaymentStrategyFactory, 2, 10);
    }
}