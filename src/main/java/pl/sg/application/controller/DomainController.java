package pl.sg.application.controller;

import pl.sg.application.model.ApplicationUser;
import pl.sg.application.api.Domain;
import pl.sg.application.api.DomainSimple;

import java.util.List;

public interface DomainController {

    List<Domain> allUserDomains(ApplicationUser user);

    DomainSimple createDomain(ApplicationUser user, String domainName);

    Domain updateDomain(ApplicationUser loggedInUser, DomainSimple domainSimple);

    void inviteUser(ApplicationUser loggedInUser, int domainId, String userLoginToInvite);

    List<DomainSimple> findInvitations(ApplicationUser user);

    void acceptInvitation(ApplicationUser user, int domainId);

    void rejectInvitation(ApplicationUser user, int domainId);

    Domain setUserMemberOfDomain(ApplicationUser loggedInUser, int domainId, String userToAddLogin);

    Domain setUserAdministratorOfDomain(ApplicationUser loggedInUser, int domainId, String userToAddLogin);

    Domain removeUserFromDomain(ApplicationUser loggedInUser, int domainId, String userToRemoveLogin);

}
