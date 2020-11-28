package pl.sg.application.model;

import javax.persistence.*;
import java.util.List;

@Entity
public class ApplicationUser {
    @Id
    @GeneratedValue
    private int id;

    @Transient
    ApplicationUserLogin loggedInUser;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "applicationUser")
    private List<ApplicationUserLogin> userLogins;

    public ApplicationUser() {
    }

    public ApplicationUser(int id, List<ApplicationUserLogin> userLogins) {
        this.id = id;
        this.userLogins = userLogins;
    }

    public int getId() {
        return id;
    }

    public ApplicationUserLogin getLoggedInUser() {
        return loggedInUser;
    }

    public ApplicationUser setLoggedInUser(ApplicationUserLogin loggedInUser) {
        this.loggedInUser = loggedInUser;
        return this;
    }

    public List<ApplicationUserLogin> getUserLogins() {
        return userLogins;
    }

    public ApplicationUser setUserLogins(List<ApplicationUserLogin> userLogins) {
        this.userLogins = userLogins;
        return this;
    }
}
