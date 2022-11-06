package pl.sg.ip.model;

import pl.sg.application.model.Domain;
import pl.sg.application.model.WithDomain;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Entity
public class TimeRecord implements WithDomain<TimeRecord> {

    @Id
    @SequenceGenerator(
            name = "timeRecordIdGenerator",
            sequenceName = "time_record_id_sequence",
            allocationSize = 1
    )
    @GeneratedValue(generator = "timeRecordIdGenerator")
    private Integer id;
    private LocalDate date;
    private int numberOfHours;
    @NotNull
    @Column(length = 10_000)
    private String description;
    @ManyToOne
    private Domain domain;
    @ManyToOne
    private Task task;

    public TimeRecord() {
    }

    public TimeRecord(LocalDate date, int numberOfHours, String description, Domain domain, Task task) {
        this.date = date;
        this.numberOfHours = numberOfHours;
        this.description = description;
        this.domain = domain;
        this.task = task;
    }

    public Integer getId() {
        return id;
    }

    public TimeRecord setId(Integer id) {
        this.id = id;
        return this;
    }

    public LocalDate getDate() {
        return date;
    }

    public TimeRecord setDate(LocalDate date) {
        this.date = date;
        return this;
    }

    public int getNumberOfHours() {
        return numberOfHours;
    }

    public TimeRecord setNumberOfHours(int numberOfHours) {
        this.numberOfHours = numberOfHours;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public TimeRecord setDescription(String description) {
        this.description = description;
        return this;
    }

    @Override
    public Domain getDomain() {
        return domain;
    }

    @Override
    public TimeRecord setDomain(Domain domain) {
        this.domain = domain;
        return this;
    }

    public Task getTask() {
        return task;
    }

    public TimeRecord setTask(Task task) {
        this.task = task;
        return this;
    }
}
