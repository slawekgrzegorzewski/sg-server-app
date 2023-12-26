package pl.sg.application.controller;

import org.modelmapper.ModelMapper;
import org.springframework.web.bind.annotation.*;
import pl.sg.application.model.ApplicationUser;
import pl.sg.application.model.ApplicationUserDomainRelation;
import pl.sg.application.model.DomainInvitation;
import pl.sg.application.security.annotations.RequestUser;
import pl.sg.application.security.annotations.TokenBearerAuth;
import pl.sg.application.service.ApplicationUserService;
import pl.sg.application.service.DomainService;
import pl.sg.application.api.Domain;
import pl.sg.application.api.DomainSimple;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static pl.sg.application.model.DomainAccessLevel.ADMIN;
import static pl.sg.application.model.DomainAccessLevel.MEMBER;

@RestController
@RequestMapping("/domains")
public class DomainRestController implements DomainController {

    private final DomainService domainService;
    private final ApplicationUserService applicationUserService;
    private final ModelMapper mapper;

    public DomainRestController(DomainService domainService, ApplicationUserService applicationUserService,
                                ModelMapper mapper) {
        this.domainService = domainService;
        this.applicationUserService = applicationUserService;
        this.mapper = mapper;
    }

    @Override
    @GetMapping
    @TokenBearerAuth
    public List<Domain> allUserDomains(@RequestUser ApplicationUser user) {
        return user.getAssignedDomains().stream()
                .map(ApplicationUserDomainRelation::getDomain)
                .map(d -> detailedDomainInfo(user, d))
                .collect(Collectors.toList());
    }

    @Override
    @PutMapping("/{domainName}")
    @TokenBearerAuth
    public DomainSimple createDomain(@RequestUser ApplicationUser user, @PathVariable("domainName") String domainName) {
        return mapper.map(domainService.create(user, new pl.sg.application.model.Domain().setName(domainName)), DomainSimple.class);
    }

    @Override
    @PatchMapping()
    @TokenBearerAuth
    public Domain updateDomain(@RequestUser ApplicationUser loggedInUser,
                               @RequestBody DomainSimple domainSimple) {
        pl.sg.application.model.Domain domain = domainService.getById(domainSimple.getId());
        loggedInUser.validateAdminDomain(domain);
        domain.setName(domainSimple.getName());
        return detailedDomainInfo(loggedInUser, domainService.save(loggedInUser, domain));
    }

    @Override
    @PostMapping("/invite/{domainId}/{userLoginToInvite}")
    @TokenBearerAuth
    public void inviteUser(@RequestUser ApplicationUser loggedInUser,
                           @PathVariable("domainId") int domainId,
                           @PathVariable("userLoginToInvite") String userLoginToInvite) {
        pl.sg.application.model.Domain domain = domainService.getById(domainId);
        ApplicationUser userToInvite = applicationUserService.getByUserLogins(userLoginToInvite);
        domainService.invite(loggedInUser, domain, userToInvite);
    }

    @Override
    @GetMapping("/invitations")
    @TokenBearerAuth
    public List<DomainSimple> findInvitations(@RequestUser ApplicationUser user) {
        return domainService.findUserInvitations(user).stream()
                .map(DomainInvitation::getDomain)
                .map(d -> mapper.map(d, DomainSimple.class))
                .collect(Collectors.toList());
    }

    @Override
    @PostMapping("/invitations/accept/{domainId}")
    @TokenBearerAuth
    public void acceptInvitation(@RequestUser ApplicationUser user, @PathVariable("domainId") int domainId) {
        pl.sg.application.model.Domain domain = domainService.getById(domainId);
        domainService.acceptInvitations(user, domain);
    }

    @Override
    @PostMapping("/invitations/reject/{domainId}")
    @TokenBearerAuth
    public void rejectInvitation(@RequestUser ApplicationUser user, @PathVariable("domainId") int domainId) {
        pl.sg.application.model.Domain domain = domainService.getById(domainId);
        domainService.rejectInvitations(user, domain);
    }

    @Override
    @PostMapping("/MEMBER/{domainId}/{userToAddLogin}")
    @TokenBearerAuth
    public Domain setUserMemberOfDomain(@RequestUser ApplicationUser loggedInUser,
                                        @PathVariable("domainId") int domainId,
                                        @PathVariable("userToAddLogin") String userToAddLogin) {
        pl.sg.application.model.Domain domain = domainService.setUserMemberOfDomain(
                loggedInUser,
                domainService.getById(domainId),
                applicationUserService.getByUserLogins(userToAddLogin));
        return detailedDomainInfo(loggedInUser, domain);
    }

    @Override
    @PostMapping("/ADMIN/{domainId}/{userToAddLogin}")
    @TokenBearerAuth
    public Domain setUserAdministratorOfDomain(@RequestUser ApplicationUser loggedInUser,
                                               @PathVariable("domainId") int domainId,
                                               @PathVariable("userToAddLogin") String userToAddLogin) {
        ApplicationUser userToAdd = applicationUserService.getByUserLogins(userToAddLogin);
        final pl.sg.application.model.Domain domain = domainService.getById(domainId);
        final pl.sg.application.model.Domain updatedDomain = domainService.setUserAdministratorOfDomain(loggedInUser, domain, userToAdd);
        return detailedDomainInfo(loggedInUser, updatedDomain);
    }

    @Override
    @DeleteMapping("/{domainId}/{userToRemoveLogin}")
    @TokenBearerAuth
    public Domain removeUserFromDomain(@RequestUser ApplicationUser loggedInUser,
                                       @PathVariable("domainId") int domainId,
                                       @PathVariable("userToRemoveLogin") String userToRemoveLogin) {
        ApplicationUser userToRemove = applicationUserService.getByUserLogins(userToRemoveLogin);
        final pl.sg.application.model.Domain domain = domainService.getById(domainId);
        final pl.sg.application.model.Domain updatedDomain = domainService.removeUserFromDomain(loggedInUser, domain, userToRemove);
        return detailedDomainInfo(loggedInUser, updatedDomain);
    }

    private Domain detailedDomainInfo(ApplicationUser user, pl.sg.application.model.Domain domain) {
        Domain result = new Domain()
                .setId(domain.getId())
                .setName(domain.getName());
        return domainService.getExistingRelation(user, domain)
                .map(existingRelation -> {
                    if (existingRelation.getAccessLevel() == ADMIN) {
                        return result.setUsersAccessLevel(domain.getAssignedUsers().stream().collect(Collectors.toMap(
                                r -> r.getApplicationUser().getLogin(),
                                r -> r.getAccessLevel().toString()
                        )));
                    } else {
                        return result.setUsersAccessLevel(Map.of(user.getLogin(), MEMBER.toString()));
                    }
                })
                .orElse(result);
    }
}
