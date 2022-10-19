package pl.sg.accountant.transport;

import pl.sg.application.api.DomainSimple;
import pl.sg.application.api.WithDomain;


public class AccountantSettings implements WithDomain {


    private Integer id;
    private boolean isCompany;
    private DomainSimple domain;

    public AccountantSettings() {
    }

    public Integer getId() {
        return id;
    }

    public AccountantSettings setId(Integer id) {
        this.id = id;
        return this;
    }

    public boolean isCompany() {
        return isCompany;
    }

    public AccountantSettings setCompany(boolean company) {
        isCompany = company;
        return this;
    }

    @Override
    public DomainSimple getDomain() {
        return domain;
    }

    public AccountantSettings setDomain(DomainSimple domain) {
        this.domain = domain;
        return this;
    }
}
