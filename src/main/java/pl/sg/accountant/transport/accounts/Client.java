package pl.sg.accountant.transport.accounts;

import pl.sg.application.api.DomainSimple;
import pl.sg.application.api.WithDomain;

public class Client implements WithDomain {

    private Integer id;
    String name;
    DomainSimple domain;


    public Client() {
    }

    public Client(Integer id, String name, DomainSimple domain) {
        this.id = id;
        this.name = name;
        this.domain = domain;
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

    public DomainSimple getDomain() {
        return domain;
    }

    public Client setDomain(DomainSimple domain) {
        this.domain = domain;
        return this;
    }
}
