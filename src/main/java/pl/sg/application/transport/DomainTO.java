package pl.sg.application.transport;

public class DomainTO {

    private Integer id;
    String name;


    public DomainTO() {
    }

    public DomainTO(Integer id, String name) {
        this.id = id;
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public DomainTO setId(int id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public DomainTO setName(String name) {
        this.name = name;
        return this;
    }
}
