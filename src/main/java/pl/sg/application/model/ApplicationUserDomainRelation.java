package pl.sg.application.model;

import jakarta.persistence.*;

@Entity
@IdClass(ApplicationUserDomainPK.class)
public class ApplicationUserDomainRelation {
    @Id
    @ManyToOne
    private ApplicationUser applicationUser;

    @Id
    @ManyToOne
    private Domain domain;

    @Enumerated(EnumType.STRING)
    private DomainAccessLevel accessLevel;

    public ApplicationUserDomainRelation() {
    }

    public ApplicationUserDomainRelation(ApplicationUser applicationUser, Domain domain, DomainAccessLevel accessLevel) {
        this.applicationUser = applicationUser;
        this.domain = domain;
        this.accessLevel = accessLevel;
    }

    public ApplicationUser getApplicationUser() {
        return applicationUser;
    }

    public ApplicationUserDomainRelation setApplicationUser(ApplicationUser applicationUser) {
        this.applicationUser = applicationUser;
        return this;
    }

    public Domain getDomain() {
        return domain;
    }

    public ApplicationUserDomainRelation setDomain(Domain domain) {
        this.domain = domain;
        return this;
    }

    public DomainAccessLevel getAccessLevel() {
        return accessLevel;
    }

    public ApplicationUserDomainRelation setAccessLevel(DomainAccessLevel accessLevel) {
        this.accessLevel = accessLevel;
        return this;
    }
}
