package pl.sg.accountant.model.billings;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.List;

@Entity
public class Category {
    @Id
    @GeneratedValue
    private int id;
    @NotNull
    private String name;
    @NotNull
    private String description;
    @OneToMany(mappedBy = "category", fetch = FetchType.LAZY)
    private List<Income> incomesOfThisType;

    public Category() {
    }

    public int getId() {
        return id;
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
}
