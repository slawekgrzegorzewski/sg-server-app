package pl.sg.ip.model;

import jakarta.validation.constraints.NotNull;
import pl.sg.application.model.Domain;
import pl.sg.application.model.WithDomain;

import jakarta.persistence.*;

import java.math.BigDecimal;
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
    private BigDecimal numberOfHours;
    @NotNull
    @Column(length = 10_000)
    private String description;
    @ManyToOne
    private Domain domain;
    @ManyToOne
    private Task task;
    @ManyToOne
    private TimeRecordCategory timeRecordCategory;

    public TimeRecord() {
    }

    public TimeRecord(LocalDate date, BigDecimal numberOfHours, String description, Domain domain, Task task, TimeRecordCategory timeRecordCategory) {
        this.date = date;
        this.numberOfHours = numberOfHours;
        this.description = description;
        this.domain = domain;
        this.task = task;
        this.timeRecordCategory = timeRecordCategory;
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

    public BigDecimal getNumberOfHours() {
        return numberOfHours;
    }

    public TimeRecord setNumberOfHours(BigDecimal numberOfHours) {
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

    public TimeRecordCategory getTimeRecordCategory() {
        return timeRecordCategory;
    }

    public void setTimeRecordCategory(TimeRecordCategory category) {
        this.timeRecordCategory = category;
    }
}
