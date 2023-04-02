package pl.sg.ip.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import pl.sg.application.model.Domain;

import java.util.List;

@Entity
public class TimeRecordCategory {

    @Id
    @SequenceGenerator(
            name = "timeRecordCategoryIpGenerator",
            sequenceName = "time_record_category_id_seq",
            allocationSize = 1
    )
    @GeneratedValue(generator = "timeRecordCategoryIpGenerator")
    private Integer id;
    @NotNull
    @Column(length = 1_000)
    private String name;
    @ManyToOne
    private Domain domain;
    @OneToMany(mappedBy = "timeRecordCategory", fetch = FetchType.LAZY)
    List<TimeRecord> timeRecords;

    public TimeRecordCategory() {
    }

    public TimeRecordCategory(Integer id, String name, Domain domain) {
        this.id = id;
        this.name = name;
        this.domain = domain;
    }

    public Integer getId() {
        return id;
    }

    public TimeRecordCategory setId(Integer id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public TimeRecordCategory setName(String name) {
        this.name = name;
        return this;
    }

    public Domain getDomain() {
        return domain;
    }

    public TimeRecordCategory setDomain(Domain domain) {
        this.domain = domain;
        return this;
    }

    public boolean isInUse() {
        return timeRecords != null && !timeRecords.isEmpty();
    }
}
