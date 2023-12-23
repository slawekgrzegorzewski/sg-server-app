package pl.sg.ip.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import pl.sg.application.model.Domain;

import java.util.List;

@Entity
public class TimeRecordCategory {

    @Getter
    @Id
    @SequenceGenerator(
            name = "timeRecordCategoryIpGenerator",
            sequenceName = "time_record_category_id_seq",
            allocationSize = 1
    )
    @GeneratedValue(generator = "timeRecordCategoryIpGenerator")
    private Long id;
    @Getter
    @NotNull
    @Column(length = 1_000)
    private String name;
    @Getter
    @ManyToOne
    private Domain domain;
    @OneToMany(mappedBy = "timeRecordCategory", fetch = FetchType.LAZY)
    List<TimeRecord> timeRecords;

    public TimeRecordCategory() {
    }

    public TimeRecordCategory(Long id, String name, Domain domain) {
        this.id = id;
        this.name = name;
        this.domain = domain;
    }

    public TimeRecordCategory setId(Long id) {
        this.id = id;
        return this;
    }

    public TimeRecordCategory setName(String name) {
        this.name = name;
        return this;
    }

    public TimeRecordCategory setDomain(Domain domain) {
        this.domain = domain;
        return this;
    }

    public boolean isInUse() {
        return timeRecords != null && !timeRecords.isEmpty();
    }
}
