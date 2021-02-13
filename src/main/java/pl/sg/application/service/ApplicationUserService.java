package pl.sg.application.service;

import pl.sg.application.model.ApplicationUser;

import java.util.Optional;

public interface ApplicationUserService {
    void save(ApplicationUser applicationUser);

    void create(ApplicationUser applicationUser);

    ApplicationUser getByUserLogins(String login);

    Optional<ApplicationUser> findByUserLogins(String login);
}
