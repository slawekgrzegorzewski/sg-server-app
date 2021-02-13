package pl.sg.application.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
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

    public Domain addAssignedUsers(DomainAccessLevel accessLevel, ApplicationUser user) {
        this.assignedUsers.add(new ApplicationUserDomainRelation(user, this, accessLevel));
        return this;
    }
}
