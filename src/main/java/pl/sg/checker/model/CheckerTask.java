package pl.sg.checker.model;

import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;
import pl.sg.application.model.ApplicationUser;

import javax.persistence.*;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

@Entity
public class CheckerTask {
    @Id
    @GeneratedValue
    private int id;
    private String name;
    private String description;
    private Duration interval;
    private LocalDateTime nextRun;

    @ManyToOne
    private ApplicationUser forUser;

    @OneToMany(cascade = CascadeType.ALL)
    @LazyCollection(LazyCollectionOption.FALSE)
    private List<CheckerStep> steps;

    @OneToMany(mappedBy = "task", cascade = CascadeType.ALL)
    @LazyCollection(LazyCollectionOption.FALSE)
    List<CheckerTaskHistory> history;

    public CheckerTask() {
    }

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public CheckerTask setName(String name) {
        this.name = name;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public CheckerTask setDescription(String description) {
        this.description = description;
        return this;
    }

    public Duration getInterval() {
        return interval;
    }

    public CheckerTask setInterval(Duration interval) {
        this.interval = interval;
        return this;
    }

    public LocalDateTime getNextRun() {
        return nextRun;
    }

    public CheckerTask setNextRun(LocalDateTime nextRun) {
        this.nextRun = nextRun;
        return this;
    }

    public ApplicationUser getForUser() {
        return forUser;
    }

    public CheckerTask setForUser(ApplicationUser forUser) {
        this.forUser = forUser;
        return this;
    }

    public List<CheckerStep> getSteps() {
        return steps;
    }

    public CheckerTask setSteps(List<CheckerStep> steps) {
        this.steps = steps;
        return this;
    }

    public List<CheckerTaskHistory> getHistory() {
        return history;
    }

    public CheckerTask setHistory(List<CheckerTaskHistory> history) {
        this.history = history;
        return this;
    }
}
