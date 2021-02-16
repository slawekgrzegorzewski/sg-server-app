package pl.sg.application.model;

import java.io.Serializable;

public class ApplicationUserDomainPK implements Serializable {
    private int applicationUser;
    private int domain;

    public ApplicationUserDomainPK() {
    }

    public ApplicationUserDomainPK(int applicationUser, int domain) {
        this.applicationUser = applicationUser;
        this.domain = domain;
    }

    public int getApplicationUser() {
        return applicationUser;
    }

    public ApplicationUserDomainPK setApplicationUser(int applicationUser) {
        this.applicationUser = applicationUser;
        return this;
    }

    public int getDomain() {
        return domain;
    }

    public ApplicationUserDomainPK setDomain(int domain) {
        this.domain = domain;
        return this;
    }
}
