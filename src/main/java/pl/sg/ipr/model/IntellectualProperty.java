package pl.sg.ipr.model;

import pl.sg.application.model.Domain;
import pl.sg.application.model.WithDomain;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
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
    private LocalDate startDate;
    @NotNull
    private LocalDate endDate;
    @NotNull
    @Column(length = 10_000)
    private String description;
    @ManyToOne
    private Domain domain;
    @OneToMany(mappedBy = "intellectualProperty")
    List<Task> tasks;

    public IntellectualProperty() {
    }

    public IntellectualProperty(LocalDate startDate, LocalDate endDate, String description, Domain domain) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.description = description;
        this.domain = domain;
    }

    public Integer getId() {
        return id;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public IntellectualProperty setStartDate(LocalDate startDate) {
        this.startDate = startDate;
        return this;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public IntellectualProperty setEndDate(LocalDate endDate) {
        this.endDate = endDate;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public IntellectualProperty setDescription(String description) {
        this.description = description;
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
        return Objects.equals(id, that.id) && Objects.equals(startDate, that.startDate) && Objects.equals(endDate, that.endDate) && Objects.equals(description, that.description) && Objects.equals(domain, that.domain) && Objects.equals(tasks, that.tasks);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, startDate, endDate, description, domain, tasks);
    }
}
