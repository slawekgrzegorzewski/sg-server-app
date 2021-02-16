package pl.sg.application.transport;

import java.util.Map;

public class DomainFullTO {

    private Integer id;
    String name;
    Map<String, String> usersAccessLevel;

    public DomainFullTO() {
    }

    public DomainFullTO(Integer id, String name, Map<String, String> usersAccessLevel) {
        this.id = id;
        this.name = name;
        this.usersAccessLevel = usersAccessLevel;
    }

    public Integer getId() {
        return id;
    }

    public DomainFullTO setId(int id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public DomainFullTO setName(String name) {
        this.name = name;
        return this;
    }

    public Map<String, String> getUsersAccessLevel() {
        return usersAccessLevel;
    }

    public DomainFullTO setUsersAccessLevel(Map<String, String> usersAccessLevel) {
        this.usersAccessLevel = usersAccessLevel;
        return this;
    }
}
