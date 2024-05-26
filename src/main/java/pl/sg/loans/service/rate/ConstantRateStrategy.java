package pl.sg.loans.service.rate;

import pl.sg.loans.utils.RateStrategy;

import java.math.BigDecimal;

public class ConstantRateStrategy implements RateStrategy {
    private final BigDecimal constantRate;

    public ConstantRateStrategy(BigDecimal constantRate) {
        this.constantRate = constantRate;
    }

    public BigDecimal getNextInstallmentRate() {
        return constantRate;
    }

    @Override
    public BigDecimal getNextInstallmentPercent() {
        return getNextInstallmentRate().multiply(new BigDecimal("100"));
    }
}
