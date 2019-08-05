package pl.sg.accountant.oauth;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class User {
    private final String id;
    private final String firstName;
    private final String lastName;
    private final String email;
    private final List<String> roles = new ArrayList<>();

    public User(String id, String firstName, String lastName, String email) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
    }

    public String getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public List<String> getRoles() {
        return roles;
    }

    public void setRoles(String... roles) {
        this.roles.addAll(Arrays.asList(roles));
    }
}
