package pl.sg.ip.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import pl.sg.application.model.Domain;
import pl.sg.application.model.WithDomain;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
public class IntellectualProperty implements WithDomain<IntellectualProperty> {

    @Id
    @SequenceGenerator(
            name = "intellectualPropertyIdGenerator",
            sequenceName = "intellectual_property_id_sequence",
            allocationSize = 1
    )
    @GeneratedValue(generator = "intellectualPropertyIdGenerator")
    private Integer id;
    @NotNull
    @Column(length = 10_000)
    private String description;
    @ManyToOne
    private Domain domain;
    @OneToMany(mappedBy = "intellectualProperty", fetch = FetchType.LAZY)
    List<Task> tasks;

    public IntellectualProperty() {
    }

    public IntellectualProperty(String description, Domain domain) {
        this.description = description;
        this.domain = domain;
    }

    public Integer getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public IntellectualProperty setDescription(String description) {
        this.description = description;
        return this;
    }

    public List<Task> tasks() {
        return tasks;
    }

    public IntellectualProperty setTasks(List<Task> tasks) {
        this.tasks = new ArrayList<>(tasks);
        return this;
    }

    @Override
    public Domain getDomain() {
        return domain;
    }

    @Override
    public IntellectualProperty setDomain(Domain domain) {
        this.domain = domain;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        IntellectualProperty that = (IntellectualProperty) o;
        return Objects.equals(id, that.id) && Objects.equals(description, that.description) && Objects.equals(domain, that.domain) && Objects.equals(tasks, that.tasks);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, description, domain, tasks);
    }
}
