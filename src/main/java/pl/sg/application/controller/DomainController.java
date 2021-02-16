package pl.sg.application.controller;

import pl.sg.application.model.ApplicationUser;
import pl.sg.application.transport.DomainFullTO;
import pl.sg.application.transport.DomainTO;

import java.util.List;

public interface DomainController {

    List<DomainFullTO> allUserDomains(ApplicationUser user);

    DomainTO createDomain(ApplicationUser user, String domainName);

    DomainFullTO updateDomain(ApplicationUser loggedInUser, DomainTO domainTO);

    void inviteUser(ApplicationUser loggedInUser, int domainId, String userLoginToInvite);

    List<DomainTO> findInvitations(ApplicationUser user);

    void acceptInvitation(ApplicationUser user, int domainId);

    void rejectInvitation(ApplicationUser user, int domainId);

    DomainFullTO setUserMemberOfDomain(ApplicationUser loggedInUser, int domainId, String userToAddLogin);

    DomainFullTO setUserAdministratorOfDomain(ApplicationUser loggedInUser, int domainId, String userToAddLogin);

    DomainFullTO removeUserFromDomain(ApplicationUser loggedInUser, int domainId, String userToRemoveLogin);

}
