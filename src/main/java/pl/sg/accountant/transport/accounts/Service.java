package pl.sg.accountant.transport.accounts;

import pl.sg.application.api.DomainSimple;
import pl.sg.application.api.WithDomain;

public class Service implements WithDomain {

    private Integer id;
    String name;
    DomainSimple domain;


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

    public DomainSimple getDomain() {
        return domain;
    }

    public Service setDomain(DomainSimple domain) {
        this.domain = domain;
        return this;
    }
}
