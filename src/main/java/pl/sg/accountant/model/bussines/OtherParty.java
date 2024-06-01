package pl.sg.accountant.model.bussines;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import pl.sg.application.model.Domain;
import pl.sg.application.model.WithDomain;

import java.util.UUID;

@Entity(name = "other_parties")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "kind", discriminatorType = DiscriminatorType.STRING)
public class OtherParty implements WithDomain<OtherParty> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter
    Long id;

    @Getter
    UUID publicId = UUID.randomUUID();

    @Getter
    @Enumerated(EnumType.STRING)
    @Column(name = "kind", insertable = false, updatable = false)
    OtherPartyKind kind;

    @Getter
    @Setter
    String name;


    @Accessors(chain = true)
    @Getter
    @Setter
    @ManyToOne
    Domain domain;


    public OtherParty() {
    }

    public OtherParty(long id, String name) {
        this.id = id;
        this.name = name;
    }
}
