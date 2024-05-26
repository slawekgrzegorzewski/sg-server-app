package pl.sg.loans.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CompositeType;
import pl.sg.application.database.MonetaryAmountType;
import pl.sg.application.model.Domain;

import javax.money.MonetaryAmount;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import static java.util.Optional.ofNullable;

@Entity(name = "loans")
public class Loan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter
    private Long id;

    @Getter
    private UUID publicId = UUID.randomUUID();

    @Getter
    @Setter
    private String name;

    @Getter
    @Setter
    private LocalDate paymentDate;

    @Getter
    @Setter
    private int numberOfInstallments;

    @Getter
    @Setter
    @OneToOne
    @JoinColumn(name = "repayment_day_strategy_config_id", referencedColumnName = "id")
    private RepaymentDayStrategyConfig repaymentDayStrategyConfig;

    @Getter
    @Setter
    @OneToOne
    @JoinColumn(name = "rate_strategy_config_id", referencedColumnName = "id")
    private RateStrategyConfig rateStrategyConfig;

    @Getter
    @Setter
    @AttributeOverride(name = "amount", column = @Column(name = "loan_amount"))
    @AttributeOverride(name = "currency", column = @Column(name = "loan_currency"))
    @CompositeType(MonetaryAmountType.class)
    private MonetaryAmount paidAmount;

    @Setter
    @OneToMany(mappedBy = "loan", fetch = FetchType.EAGER)
    private List<Installment> installments;

    @Getter
    @Setter
    @ManyToOne
    private Domain domain;

    public List<Installment> getInstallments() {
        return ofNullable(installments).orElseGet(List::of);
    }
}
