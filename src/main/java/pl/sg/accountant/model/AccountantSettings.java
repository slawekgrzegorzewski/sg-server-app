package pl.sg.accountant.model;

import pl.sg.application.model.Domain;
import pl.sg.application.model.WithDomain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

@Entity
public class AccountantSettings implements WithDomain<AccountantSettings> {
    @Id
    @GeneratedValue
    private Integer id;
    @NotNull
    private boolean isCompany;
    @ManyToOne
    private Domain domain;

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
    public Domain getDomain() {
        return domain;
    }

    @Override
    public AccountantSettings setDomain(Domain domain) {
        this.domain = domain;
        return this;
    }
}
