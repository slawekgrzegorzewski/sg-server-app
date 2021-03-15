package pl.sg.application.model;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Domain {
    @Id
    @GeneratedValue
    private Integer id;
    String name;

    @OneToMany(mappedBy = "domain")
    List<ApplicationUserDomainRelation> assignedUsers;


    public Domain() {
    }

    public Domain(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public Domain setId(Integer id) {
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

    public List<ApplicationUserDomainRelation> getAssignedUsers() {
        return assignedUsers;
    }

    public Domain setAssignedUsers(List<ApplicationUserDomainRelation> assignedUsers) {
        this.assignedUsers = assignedUsers;
        return this;
    }

    public ApplicationUserDomainRelation addAssignedUsers(DomainAccessLevel accessLevel, ApplicationUser user) {
        if (this.assignedUsers == null) {
            this.assignedUsers = new ArrayList<>();
        }
        final ApplicationUserDomainRelation relation = new ApplicationUserDomainRelation(user, this, accessLevel);
        this.assignedUsers.add(relation);
        return relation;
    }
}
