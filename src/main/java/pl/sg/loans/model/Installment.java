package pl.sg.loans.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CompositeType;
import pl.sg.application.database.MonetaryAmountType;

import javax.money.MonetaryAmount;
import java.time.LocalDate;
import java.util.UUID;

@Entity(name = "installments")
public class Installment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter
    private Long id;

    @Getter
    private UUID publicId = UUID.randomUUID();

    @Getter
    @Setter
    @ManyToOne
    private Loan loan;


    @Getter
    @Setter
    private LocalDate paidAt;

    @Getter
    @Setter
    @AttributeOverride(name = "amount", column = @Column(name = "repaid_interest_amount"))
    @AttributeOverride(name = "currency", column = @Column(name = "repaid_interest_currency"))
    @CompositeType(MonetaryAmountType.class)
    private MonetaryAmount repaidInterest;

    @Getter
    @Setter
    @AttributeOverride(name = "amount", column = @Column(name = "repaid_amount"))
    @AttributeOverride(name = "currency", column = @Column(name = "repaid_currency"))
    @CompositeType(MonetaryAmountType.class)
    private MonetaryAmount repaidAmount;

    @Getter
    @Setter
    @AttributeOverride(name = "amount", column = @Column(name = "overpayment_amount"))
    @AttributeOverride(name = "currency", column = @Column(name = "overpayment_currency"))
    @CompositeType(MonetaryAmountType.class)
    private MonetaryAmount overpayment;
}
