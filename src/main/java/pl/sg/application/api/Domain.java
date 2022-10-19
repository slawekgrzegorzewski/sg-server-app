package pl.sg.application.api;

import java.util.Map;

public class Domain {

    private Integer id;
    String name;
    Map<String, String> usersAccessLevel;

    public Domain() {
    }

    public Domain(Integer id, String name, Map<String, String> usersAccessLevel) {
        this.id = id;
        this.name = name;
        this.usersAccessLevel = usersAccessLevel;
    }

    public Integer getId() {
        return id;
    }

    public Domain setId(int id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public Domain setName(String name) {
        this.name = name;
        return this;
    }

    public Map<String, String> getUsersAccessLevel() {
        return usersAccessLevel;
    }

    public Domain setUsersAccessLevel(Map<String, String> usersAccessLevel) {
        this.usersAccessLevel = usersAccessLevel;
        return this;
    }
}
