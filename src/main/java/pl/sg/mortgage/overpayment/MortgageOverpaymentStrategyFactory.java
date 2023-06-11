package pl.sg.mortgage.overpayment;

import pl.sg.graphql.schema.types.MortgageCalculationParams;

public interface MortgageOverpaymentStrategyFactory {
    MortgageOverpaymentStrategy create(MortgageCalculationParams mortgageCalculationParams);
}
