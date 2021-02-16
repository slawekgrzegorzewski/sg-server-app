package pl.sg.application.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.sg.application.model.ApplicationUser;
import pl.sg.application.model.DomainInvitation;

import java.util.List;

public interface DomainInvitationRepository extends JpaRepository<DomainInvitation, Integer> {
    List<DomainInvitation> findByApplicationUser(ApplicationUser user);
}
