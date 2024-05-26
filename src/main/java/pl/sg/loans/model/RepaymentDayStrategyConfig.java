package pl.sg.loans.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import pl.sg.application.model.Domain;

import java.util.UUID;

@Entity(name = "repayment_day_strategy_configs")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "strategy", discriminatorType = DiscriminatorType.STRING)
public class RepaymentDayStrategyConfig {

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
    @ManyToOne
    private Domain domain;
}
