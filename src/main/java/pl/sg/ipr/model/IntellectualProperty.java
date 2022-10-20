package pl.sg.ipr.model;

import pl.sg.application.model.Domain;
import pl.sg.application.model.WithDomain;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.List;

@Entity
public class IntellectualProperty implements WithDomain<IntellectualProperty> {

    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "intellectual_property_id_sequence"
    )
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
}