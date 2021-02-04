package pl.sg.application.transport;

public class DomainTO {
    private int id;

    String name;


    public DomainTO() {
    }

    public DomainTO(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
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
