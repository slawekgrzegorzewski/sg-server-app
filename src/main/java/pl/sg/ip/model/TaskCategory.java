package pl.sg.ip.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

@Entity
public class TaskCategory {

    @Id
    @SequenceGenerator(
            name = "taskCategoryIpGenerator",
            sequenceName = "task_category_id_seq",
            allocationSize = 1
    )
    @GeneratedValue(generator = "taskCategoryIpGenerator")
    private Integer id;
    @NotNull
    @Column(length = 1_000)
    private String name;

    public TaskCategory() {
    }

    public TaskCategory(Integer id, String name) {
        this.id = id;
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public TaskCategory setId(Integer id) {
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
