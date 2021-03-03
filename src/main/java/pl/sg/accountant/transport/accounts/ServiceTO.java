package pl.sg.accountant.transport.accounts;

import pl.sg.application.transport.DomainTO;
import pl.sg.application.transport.WithDomainTO;

public class ServiceTO implements WithDomainTO {

    private Integer id;
    String name;
    DomainTO domain;


    public ServiceTO() {
    }

    public ServiceTO(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public ServiceTO setId(Integer id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public ServiceTO setName(String name) {
        this.name = name;
        return this;
    }

    public DomainTO getDomain() {
        return domain;
    }

    public ServiceTO setDomain(DomainTO domain) {
        this.domain = domain;
        return this;
    }
}
