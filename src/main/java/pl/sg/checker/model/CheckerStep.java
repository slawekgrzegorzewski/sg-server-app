package pl.sg.checker.model;

import javax.persistence.*;

@Entity
public class CheckerStep<T extends CheckerStep> {
    @Id
    @GeneratedValue
    private int id;
    @Column(length = 1000)
    private String name;
    @Column(length = 1000)
    private String description;
    private int stepOrder;

    public CheckerStep() {
    }

    public CheckerStep(int id, String name, String description, int stepOrder) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.stepOrder = stepOrder;
    }

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public T setName(String name) {
        this.name = name;
        return (T) this;
    }

    public String getDescription() {
        return description;
    }

    public T setDescription(String description) {
        this.description = description;
        return (T) this;
    }

    public int getStepOrder() {
        return stepOrder;
    }

    public T setStepOrder(int stepOrder) {
        this.stepOrder = stepOrder;
        return (T) this;
    }
}
