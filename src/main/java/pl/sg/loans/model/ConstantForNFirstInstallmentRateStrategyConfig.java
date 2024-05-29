package pl.sg.loans.model;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@DiscriminatorValue(value = "CONSTANT_FOR_N_INSTALLMENTS")
public class ConstantForNFirstInstallmentRateStrategyConfig extends RateStrategyConfig {

    @Getter
    @Setter
    private BigDecimal constantRate;

    @Getter
    @Setter
    private BigDecimal variableRateMargin;

    @Getter
    @Setter
    @Column(name = "becomes_variable_rate_after_n_installments")
    private int becomesVariableRateAfterNInstallments;
}
