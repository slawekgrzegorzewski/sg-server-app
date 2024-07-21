package pl.sg.accountant.model.accounts;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import pl.sg.application.model.Domain;

import java.util.UUID;

@Entity(name = "account_watchers")
public class AccountWatcher {

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
    private Account account;

    @OneToOne(mappedBy = "accountWatcher")
    private AccountWatcherConfig accountWatcherConfig;
}
