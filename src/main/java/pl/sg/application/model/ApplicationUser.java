package pl.sg.application.model;

import org.apache.commons.lang3.RandomStringUtils;
import org.jboss.aerogear.security.otp.api.Base32;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Entity
public class ApplicationUser {
    @Id
    @GeneratedValue
    private int id;
    private String login;
    private String password;

    private boolean isUsing2FA;
    private String secret;

    private String firstName;
    private String lastName;
    private String email;
    @ElementCollection(fetch = FetchType.EAGER)
    private List<String> roles;

    public ApplicationUser() {
        this.secret = Base32.encode(RandomStringUtils.randomAscii(10).getBytes());
        this.isUsing2FA = false;
    }

    public ApplicationUser(int id, String login, String password, String firstName, String lastName, String email, List<String> roles) {
        this();
        this.id = id;
        this.login = login;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.roles = new ArrayList<>(roles);
    }

    public int getId() {
        return id;
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
        this.roles.addAll(Arrays.asList(roles));
    }
}
