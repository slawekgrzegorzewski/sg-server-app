package pl.sg.accountant.model.accounts;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CompositeType;
import pl.sg.application.database.MonetaryAmountType;

import javax.money.MonetaryAmount;
import java.math.BigDecimal;

@Entity
@DiscriminatorValue("SUM_OF_TRANSACTIONS_IN_MONTH")
public class SumOfTransactionsInMonth extends AccountWatcherConfig {

    @Getter
    @Setter
    private int from_day_of_month;

    @Getter
    @Setter
    @AttributeOverride(name = "amount", column = @Column(name = "threshold_amount"))
    @AttributeOverride(name = "currency", column = @Column(name = "threshold_currency"))
    @CompositeType(MonetaryAmountType.class)
    private MonetaryAmount threshold;
}
