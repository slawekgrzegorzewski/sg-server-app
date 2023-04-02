package pl.sg.ip.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

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

    public TimeRecordCategory() {
    }

    public TimeRecordCategory(Integer id, String name) {
        this.id = id;
        this.name = name;
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

    public void setName(String name) {
        this.name = name;
    }
}
