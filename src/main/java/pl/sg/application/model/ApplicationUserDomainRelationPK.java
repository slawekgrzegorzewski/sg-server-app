package pl.sg.application.model;

import java.io.Serializable;

public class ApplicationUserDomainRelationPK implements Serializable {
    private int applicationUser;
    private int domain;

    public ApplicationUserDomainRelationPK() {
    }

    public ApplicationUserDomainRelationPK(int applicationUser, int domain) {
        this.applicationUser = applicationUser;
        this.domain = domain;
    }

    public int getApplicationUser() {
        return applicationUser;
    }

    public ApplicationUserDomainRelationPK setApplicationUser(int applicationUser) {
        this.applicationUser = applicationUser;
        return this;
    }

    public int getDomain() {
        return domain;
    }

    public ApplicationUserDomainRelationPK setDomain(int domain) {
        this.domain = domain;
        return this;
    }
}
