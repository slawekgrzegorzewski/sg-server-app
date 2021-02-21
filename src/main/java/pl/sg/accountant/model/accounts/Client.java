package pl.sg.accountant.model.accounts;

import pl.sg.application.model.Domain;
import pl.sg.application.model.WithDomain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class Client implements WithDomain<Client> {
    @Id
    @GeneratedValue
    private Integer id;
    String name;
    @ManyToOne
    Domain domain;


    public Client() {
    }

    public Client(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public Client setId(Integer id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public Client setName(String name) {
        this.name = name;
        return this;
    }

    public Domain getDomain() {
        return domain;
    }

    public Client setDomain(Domain domain) {
        this.domain = domain;
        return this;
    }
}
