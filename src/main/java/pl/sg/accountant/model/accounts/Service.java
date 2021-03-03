package pl.sg.accountant.model.accounts;

import pl.sg.application.model.Domain;
import pl.sg.application.model.WithDomain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class Service implements WithDomain<Service> {
    @Id
    @GeneratedValue
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
