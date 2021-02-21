package pl.sg.accountant.transport.accounts;

import pl.sg.application.transport.DomainTO;
import pl.sg.application.transport.WithDomainTO;

public class ClientTO implements WithDomainTO {

    private Integer id;
    String name;
    DomainTO domain;


    public ClientTO() {
    }

    public ClientTO(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public ClientTO setId(Integer id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public ClientTO setName(String name) {
        this.name = name;
        return this;
    }

    public DomainTO getDomain() {
        return domain;
    }

    public ClientTO setDomain(DomainTO domain) {
        this.domain = domain;
        return this;
    }
}
