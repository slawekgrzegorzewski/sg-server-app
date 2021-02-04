package pl.sg.application.model;

import javax.persistence.*;
import java.util.List;

@Entity
public class Domain {
    @Id
    @GeneratedValue
    private int id;

    String name;


    public Domain() {
    }

    public Domain(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Domain setName(String name) {
        this.name = name;
        return this;
    }
}
