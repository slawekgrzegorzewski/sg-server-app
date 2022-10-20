package pl.sg.ipr.model;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.List;

@Entity
public class Task {

    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "intellectual_property_task_id_sequence"
    )
    private Integer id;
    @NotNull
    @Column(length = 10_000)
    private String description;
    @Column(length = 200)
    private String coAuthors;
    @ElementCollection
    private List<String> attachments;
    @ManyToOne
    private IntellectualProperty intellectualProperty;
    @OneToMany(mappedBy = "task")
    private List<TimeRecord> timeRecords;

    public Task() {
    }

    public Task(String description, String coAuthors, List<String> attachments, IntellectualProperty intellectualProperty, List<TimeRecord> timeRecords) {
        this.description = description;
        this.coAuthors = coAuthors;
        this.attachments = attachments;
        this.intellectualProperty = intellectualProperty;
        this.timeRecords = timeRecords;
    }

    public Integer getId() {
        return id;
    }

    public Task setId(Integer id) {
        this.id = id;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public Task setDescription(String description) {
        this.description = description;
        return this;
    }

    public String getCoAuthors() {
        return coAuthors;
    }

    public Task setCoAuthors(String coAuthors) {
        this.coAuthors = coAuthors;
        return this;
    }

    public List<String> getAttachments() {
        return attachments;
    }

    public Task setAttachments(List<String> attachments) {
        this.attachments = attachments;
        return this;
    }

    public IntellectualProperty getIntellectualProperty() {
        return intellectualProperty;
    }

    public Task setIntellectualProperty(IntellectualProperty intellectualProperty) {
        this.intellectualProperty = intellectualProperty;
        return this;
    }

    public List<TimeRecord> getTimeRecords() {
        return timeRecords;
    }

    public Task setTimeRecords(List<TimeRecord> timeRecords) {
        this.timeRecords = timeRecords;
        return this;
    }
}