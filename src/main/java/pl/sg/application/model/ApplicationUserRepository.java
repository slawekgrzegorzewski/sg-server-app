package pl.sg.application.model;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface ApplicationUserRepository extends JpaRepository<ApplicationUser, Integer> {
    @Query("SELECT au FROM ApplicationUser au JOIN au.userLogins aul WHERE aul.login = ?1")
    Optional<ApplicationUser> findFirstByUserLogins(String login);
}
