package pl.sg.accountant.transport.billings;

import pl.sg.application.transport.DomainTO;

public class CategoryTO {
    private Integer id;
    private String name;
    private String description;
    private DomainTO domain;

    public CategoryTO() {
    }

    public CategoryTO(int id, String name, String description, DomainTO domain) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.domain = domain;
    }

    public Integer getId() {
        return id;
    }

    public CategoryTO setId(Integer id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public CategoryTO setName(String name) {
        this.name = name;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public CategoryTO setDescription(String description) {
        this.description = description;
        return this;
    }

    public DomainTO getDomain() {
        return domain;
    }

    public CategoryTO setDomain(DomainTO domain) {
        this.domain = domain;
        return this;
    }
}
