package pl.sg.accountant.model.billings;

import pl.sg.application.model.Domain;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.List;

@Entity
public class Category {
    @Id
    @GeneratedValue
    private Integer id;
    @NotNull
    private String name;
    @NotNull
    private String description;
    @OneToMany(mappedBy = "category", fetch = FetchType.LAZY)
    private List<Income> incomesOfThisType;
    @ManyToOne
    private Domain domain;

    public Category() {
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

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<Income> getIncomesOfThisType() {
        return incomesOfThisType;
    }

    public void setIncomesOfThisType(List<Income> income) {
        this.incomesOfThisType = income;
    }

    public Domain getDomain() {
        return domain;
    }

    public Category setDomain(Domain domain) {
        this.domain = domain;
        return this;
    }
}
