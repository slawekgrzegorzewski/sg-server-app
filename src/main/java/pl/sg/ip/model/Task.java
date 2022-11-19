package pl.sg.ip.model;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Task {

    @Id
    @SequenceGenerator(
            name = "intellectualPropertyTaskIdGenerator",
            sequenceName = "intellectual_property_task_id_sequence",
            allocationSize = 1
    )
    @GeneratedValue(generator = "intellectualPropertyTaskIdGenerator")
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
        this.attachments = new ArrayList<>(attachments);
        this.intellectualProperty = intellectualProperty;
        this.timeRecords = new ArrayList<>(timeRecords);
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
        this.attachments = new ArrayList<>(attachments);
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
        this.timeRecords = new ArrayList<>(timeRecords);
        return this;
    }
}
