package pl.sg.loans.model;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

@Entity
@DiscriminatorValue("NTH_DAY")
public class NthDayOfMonthRepaymentDayStrategyConfig extends RepaymentDayStrategyConfig {

    @Getter
    @Setter
    private int dayOfMonth;
}
