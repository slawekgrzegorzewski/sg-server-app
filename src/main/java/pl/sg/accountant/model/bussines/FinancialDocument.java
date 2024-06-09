package pl.sg.accountant.model.bussines;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CompositeType;
import pl.sg.application.database.MonetaryAmountType;
import pl.sg.application.model.Domain;

import javax.money.MonetaryAmount;
import java.time.LocalDate;
import java.util.UUID;

@Entity(name = "financial_documents")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "kind", discriminatorType = DiscriminatorType.STRING)
public abstract class FinancialDocument {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Getter
    private UUID publicId = UUID.randomUUID();

    @Getter
    @Setter
    private String name;

    @Getter
    @Setter
    @ManyToOne
    private OtherParty otherParty;

    @Getter
    @Setter
    @AttributeOverride(name = "amount", column = @Column(name = "amount"))
    @AttributeOverride(name = "currency", column = @Column(name = "currency"))
    @CompositeType(MonetaryAmountType.class)
    private MonetaryAmount amount;

    @Getter
    @Setter
    private LocalDate createdAt;

    @Getter
    @Setter
    private String description;

    @Getter
    @Setter
    @ManyToOne
    private Domain domain;
}
