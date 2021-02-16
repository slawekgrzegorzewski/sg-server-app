package pl.sg.application.service;

import pl.sg.application.model.ApplicationUser;
import pl.sg.application.model.ApplicationUserDomainRelation;
import pl.sg.application.model.Domain;
import pl.sg.application.model.DomainInvitation;

import java.util.List;
import java.util.Optional;

public interface DomainService {

    Domain getById(int domainId);

    Optional<Domain> findById(int domainId);

    Domain create(ApplicationUser user, Domain newDomain);

    Domain save(ApplicationUser user, Domain domain);

    List<DomainInvitation> findUserInvitations(ApplicationUser user);

    DomainInvitation invite(ApplicationUser loggedInUser, Domain domain, ApplicationUser userToInvite);

    void acceptInvitations(ApplicationUser user, Domain domain);

    void rejectInvitations(ApplicationUser user, Domain domain);

    Domain setUserMemberOfDomain(ApplicationUser loggedInUser, Domain domain, ApplicationUser userToAdd);

    Domain setUserAdministratorOfDomain(ApplicationUser loggedInUser, Domain domain, ApplicationUser userToAdd);

    Domain removeUserFromDomain(ApplicationUser loggedInUser, Domain domain, ApplicationUser userToRemove);

    Optional<ApplicationUserDomainRelation> getExistingRelation(ApplicationUser user, Domain domain);
}
