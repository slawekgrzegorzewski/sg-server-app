package pl.sg.accountant;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Test {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    public Test() {
    }

    public Test(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public Test setId(Long id) {
        this.id = id;
        return this;
    }
}
