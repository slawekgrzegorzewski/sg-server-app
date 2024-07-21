package pl.sg.accountant.model.accounts;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import pl.sg.application.model.Domain;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity(name = "account_watcher_configs")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "type", discriminatorType = DiscriminatorType.STRING)
public class AccountWatcherConfig {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter
    private Long id;

    @Getter
    private UUID publicId = UUID.randomUUID();

    @Getter
    @Setter
    @OneToOne
    private AccountWatcher accountWatcher;

    @Getter
    @OneToMany(mappedBy = "accountWatcherConfig")
    private List<AccountWatcherTransactionExclusion> exclusions = new ArrayList<>();
}
