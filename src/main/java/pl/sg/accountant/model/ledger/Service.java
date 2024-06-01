package pl.sg.accountant.model.ledger;

import jakarta.persistence.*;
import pl.sg.application.model.Domain;
import pl.sg.application.model.WithDomain;

@Entity
public class Service implements WithDomain<Service> {
    @Id
    @SequenceGenerator(
            name = "commonIdGenerator",
            sequenceName = "hibernate_sequence",
            allocationSize = 1
    )
    @GeneratedValue(generator = "commonIdGenerator")
    private Integer id;
    String name;
    @ManyToOne
    Domain domain;


    public Service() {
    }

    public Service(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public Service setId(Integer id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public Service setName(String name) {
        this.name = name;
        return this;
    }

    public Domain getDomain() {
        return domain;
    }

    public Service setDomain(Domain domain) {
        this.domain = domain;
        return this;
    }
}
