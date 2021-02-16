package pl.sg.application.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.ManyToOne;

@Entity
@IdClass(ApplicationUserDomainPK.class)
public class DomainInvitation {
    @Id
    @ManyToOne
    private ApplicationUser applicationUser;

    @Id
    @ManyToOne
    private Domain domain;

    public DomainInvitation() {
    }

    public DomainInvitation(ApplicationUser applicationUser, Domain domain) {
        this.applicationUser = applicationUser;
        this.domain = domain;
    }

    public ApplicationUser getApplicationUser() {
        return applicationUser;
    }

    public DomainInvitation setApplicationUser(ApplicationUser applicationUser) {
        this.applicationUser = applicationUser;
        return this;
    }

    public Domain getDomain() {
        return domain;
    }

    public DomainInvitation setDomain(Domain domain) {
        this.domain = domain;
        return this;
    }
}
