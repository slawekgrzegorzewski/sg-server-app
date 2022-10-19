package pl.sg.application.api;

public class DomainSimple {

    private Integer id;
    String name;


    public DomainSimple() {
    }

    public DomainSimple(Integer id, String name) {
        this.id = id;
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public DomainSimple setId(int id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public DomainSimple setName(String name) {
        this.name = name;
        return this;
    }
}
