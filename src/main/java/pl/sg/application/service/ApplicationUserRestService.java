package pl.sg.application.service;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Component;
import pl.sg.application.model.ApplicationUser;
import pl.sg.application.repository.ApplicationUserRepository;

import java.util.Optional;

@Component
public class ApplicationUserRestService implements ApplicationUserService {
    private final ApplicationUserRepository applicationUserRepository;

    public ApplicationUserRestService(ApplicationUserRepository applicationUserRepository) {
        this.applicationUserRepository = applicationUserRepository;
    }

    @Override
    public void save(ApplicationUser applicationUser) {
        applicationUserRepository.save(applicationUser);
    }

    @Override
    public void create(ApplicationUser applicationUser) {
        applicationUser.setId(null);
        applicationUserRepository.save(applicationUser);
    }

    @Override
    public Optional<ApplicationUser> findByUserLogins(String login) {
        return applicationUserRepository.findFirstByUserLogins(login);
    }

    @Override
    public ApplicationUser getByUserLogins(String login) {
        return findByUserLogins(login).orElseThrow(
                () -> new BadCredentialsException("Wrong user/password"));
    }
}
