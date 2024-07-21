package pl.sg.accountant.model.accounts;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import pl.sg.accountant.model.ledger.FinancialTransaction;

import java.util.UUID;

@Entity(name = "account_watcher_transaction_exclusions")
public class AccountWatcherTransactionExclusion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter
    private Long id;

    @Getter
    private UUID publicId = UUID.randomUUID();

    @Getter
    @Setter
    @OneToOne
    private AccountWatcherConfig accountWatcherConfig;

    @Getter
    @Setter
    @ManyToOne
    private FinancialTransaction financialTransaction;
}
