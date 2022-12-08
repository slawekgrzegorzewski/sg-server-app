package pl.sg.application.model;

import org.apache.commons.lang3.RandomStringUtils;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.jboss.aerogear.security.otp.api.Base32;
import pl.sg.application.ForbiddenException;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Entity
public class ApplicationUser {
    @Id
    
    @SequenceGenerator(
            name = "commonIdGenerator",
            sequenceName = "hibernate_sequence",
            allocationSize = 1
    )
    @GeneratedValue(generator = "commonIdGenerator")
    private Integer id;
    private String login;
    private String password;
    @Column(columnDefinition = "boolean not null default false")
    private boolean isUsing2FA;
    private String secret;

    private String firstName;
    private String lastName;
    private String email;
    @ElementCollection(fetch = FetchType.EAGER)
    @Fetch(value = FetchMode.JOIN)
    private List<String> roles;

    @OneToMany(mappedBy = "applicationUser")
    List<ApplicationUserDomainRelation> assignedDomains;

    @ManyToOne
    Domain defaultDomain;

    public ApplicationUser() {
        this.secret = Base32.encode(RandomStringUtils.randomAscii(10).getBytes());
        this.isUsing2FA = false;
    }

    public ApplicationUser(int id, String login, String password, String firstName, String lastName, String email,
                           List<String> roles, Domain defaultDomain) {
        this();
        this.id = id;
        this.login = login;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.roles = new ArrayList<>(roles);
        this.defaultDomain = defaultDomain;
    }

    public Integer getId() {
        return id;
    }

    public ApplicationUser setId(Integer id) {
        this.id = id;
        return this;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isUsing2FA() {
        return isUsing2FA;
    }

    public void setUsing2FA(boolean using2FA) {
        isUsing2FA = using2FA;
    }

    public String getSecret() {
        return secret;
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
        this.roles = new ArrayList<>(Arrays.asList(roles));
    }

    public List<ApplicationUserDomainRelation> getAssignedDomains() {
        return assignedDomains;
    }

    public ApplicationUser setAssignedDomains(List<ApplicationUserDomainRelation> assignedDomains) {
        this.assignedDomains = assignedDomains;
        return this;
    }

    public ApplicationUser addAssignedDomain(DomainAccessLevel accessLevel, Domain domain) {
        this.assignedDomains.add(new ApplicationUserDomainRelation(this, domain, accessLevel));
        return this;
    }

    public Domain getDefaultDomain() {
        return defaultDomain;
    }

    public ApplicationUser setDefaultDomain(Domain defaultDomain) {
        this.defaultDomain = defaultDomain;
        return this;
    }

    public void validateAdminDomain(Domain domain) {
        validateAdminDomain(domain.getId());
    }

    private void validateAdminDomain(Integer domainId) {
        validateDomain(domainId);
        final boolean userBelongsToDomain = assignedDomains.stream()
                .filter(r -> r.getAccessLevel() == DomainAccessLevel.ADMIN)
                .map(ApplicationUserDomainRelation::getDomain)
                .anyMatch(d -> d.getId().equals(domainId));
        if (!userBelongsToDomain) {
            throw new ForbiddenException("User " + getLogin() + " is not administrator of a \"" + domainId + "\" domain.");
        }
    }

    public void validateDomain(Domain domain) {
        validateDomain(domain.getId());
    }

    private void validateDomain(Integer domainId) {
        final boolean userBelongsToDomain = assignedDomains.stream()
                .map(ApplicationUserDomainRelation::getDomain)
                .anyMatch(d -> d.getId().equals(domainId));
        if (!userBelongsToDomain) {
            throw new ForbiddenException("User " + getLogin() + " does not belong to a \"" + domainId + "\" domain.");
        }
    }
}
