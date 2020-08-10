package pl.sg.security;

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
    }

    public ApplicationUser(int id, String login, String password, boolean isUsing2FA, String secret, String firstName, String lastName, String email, List<String> roles) {
        this.id = id;
        this.login = login;
        this.password = password;
        this.isUsing2FA = isUsing2FA;
        this.secret = secret;
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
        if (isUsing2FA) {
            generateSecret();
        }
    }

    private void generateSecret() {
        if (secret == null) {
            secret = Base32.random();
        }
    }

    public String getSecret() {
        if (isUsing2FA) {
            generateSecret();
            return secret;
        }
        return null;
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
