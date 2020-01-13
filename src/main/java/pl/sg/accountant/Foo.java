package pl.sg.accountant;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Foo {
    @Id
    private int id;
    private String name;


    public Foo() {
    }

    public Foo(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
