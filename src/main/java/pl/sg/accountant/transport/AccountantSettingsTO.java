package pl.sg.accountant.transport;

import pl.sg.application.model.Domain;
import pl.sg.application.model.WithDomain;
import pl.sg.application.transport.DomainTO;
import pl.sg.application.transport.WithDomainTO;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;


public class AccountantSettingsTO implements WithDomainTO {


    private Integer id;
    private boolean isCompany;
    private DomainTO domain;

    public AccountantSettingsTO() {
    }

    public Integer getId() {
        return id;
    }

    public AccountantSettingsTO setId(Integer id) {
        this.id = id;
        return this;
    }

    public boolean isCompany() {
        return isCompany;
    }

    public AccountantSettingsTO setCompany(boolean company) {
        isCompany = company;
        return this;
    }

    @Override
    public DomainTO getDomain() {
        return domain;
    }

    public AccountantSettingsTO setDomain(DomainTO domain) {
        this.domain = domain;
        return this;
    }
}
