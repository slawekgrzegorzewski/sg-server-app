package pl.sg.application.controller;

import pl.sg.application.model.ApplicationUser;
import pl.sg.application.model.Domain;
import pl.sg.application.transport.DomainTO;

import java.util.List;

public interface DomainController {

    List<DomainTO> allUserDomains(ApplicationUser user);

    DomainTO createDomain(ApplicationUser user, String domainName);

    DomainTO setUserMemberOfDomain(ApplicationUser loggedInUser, int domainId, String userToAddLogin);

    DomainTO setUserAdministratorOfDomain(ApplicationUser loggedInUser, int domainId, String userToAddLogin);

}
