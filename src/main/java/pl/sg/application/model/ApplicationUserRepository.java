package pl.sg.application.model;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.sg.application.model.ApplicationUser;

import java.util.Optional;

public interface ApplicationUserRepository extends JpaRepository<ApplicationUser, Integer> {
    Optional<ApplicationUser> findFirstByLogin(String login);
}
