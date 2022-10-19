package pl.sg.accountant.transport.billings;

import pl.sg.application.api.DomainSimple;
import pl.sg.application.api.WithDomain;

public class Category implements WithDomain {
    private Integer id;
    private String name;
    private String description;
    private DomainSimple domain;

    public Category() {
    }

    public Category(int id, String name, String description, DomainSimple domain) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.domain = domain;
    }

    public Integer getId() {
        return id;
    }

    public Category setId(Integer id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public Category setName(String name) {
        this.name = name;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public Category setDescription(String description) {
        this.description = description;
        return this;
    }

    public DomainSimple getDomain() {
        return domain;
    }

    public Category setDomain(DomainSimple domain) {
        this.domain = domain;
        return this;
    }
}
