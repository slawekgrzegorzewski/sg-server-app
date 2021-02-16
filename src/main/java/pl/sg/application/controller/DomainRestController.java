package pl.sg.application.controller;

import org.modelmapper.ModelMapper;
import org.springframework.web.bind.annotation.*;
import pl.sg.application.model.ApplicationUser;
import pl.sg.application.model.ApplicationUserDomainRelation;
import pl.sg.application.model.Domain;
import pl.sg.application.model.DomainInvitation;
import pl.sg.application.security.annotations.RequestUser;
import pl.sg.application.security.annotations.TokenBearerAuth;
import pl.sg.application.service.ApplicationUserService;
import pl.sg.application.service.DomainService;
import pl.sg.application.transport.DomainFullTO;
import pl.sg.application.transport.DomainTO;

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
    public List<DomainFullTO> allUserDomains(@RequestUser ApplicationUser user) {
        return user.getAssignedDomains().stream()
                .map(ApplicationUserDomainRelation::getDomain)
                .map(d -> detailedDomainInfo(user, d))
                .collect(Collectors.toList());
    }

    @Override
    @PutMapping("/{domainName}")
    @TokenBearerAuth
    public DomainTO createDomain(@RequestUser ApplicationUser user, @PathVariable String domainName) {
        return mapper.map(domainService.create(user, new Domain().setName(domainName)), DomainTO.class);
    }

    @Override
    @PatchMapping()
    @TokenBearerAuth
    public DomainFullTO updateDomain(@RequestUser ApplicationUser loggedInUser,
                                     @RequestBody DomainTO domainTO) {
        Domain domain = domainService.getById(domainTO.getId());
        loggedInUser.validateAdminDomain(domain);
        domain.setName(domainTO.getName());
        return detailedDomainInfo(loggedInUser, domainService.save(loggedInUser, domain));
    }

    @Override
    @PostMapping("/invite/{domainId}/{userLoginToInvite}")
    @TokenBearerAuth
    public void inviteUser(@RequestUser ApplicationUser loggedInUser,
                           @PathVariable int domainId,
                           @PathVariable String userLoginToInvite) {
        Domain domain = domainService.getById(domainId);
        ApplicationUser userToInvite = applicationUserService.getByUserLogins(userLoginToInvite);
        domainService.invite(loggedInUser, domain, userToInvite);
    }

    @Override
    @GetMapping("/invitations")
    @TokenBearerAuth
    public List<DomainTO> findInvitations(@RequestUser ApplicationUser user) {
        return domainService.findUserInvitations(user).stream()
                .map(DomainInvitation::getDomain)
                .map(d -> mapper.map(d, DomainTO.class))
                .collect(Collectors.toList());
    }

    @Override
    @PostMapping("/invitations/accept/{domainId}")
    @TokenBearerAuth
    public void acceptInvitation(@RequestUser ApplicationUser user, @PathVariable int domainId) {
        Domain domain = domainService.getById(domainId);
        domainService.acceptInvitations(user, domain);
    }

    @Override
    @PostMapping("/invitations/reject/{domainId}")
    @TokenBearerAuth
    public void rejectInvitation(@RequestUser ApplicationUser user, @PathVariable int domainId) {
        Domain domain = domainService.getById(domainId);
        domainService.rejectInvitations(user, domain);
    }

    @Override
    @PostMapping("/MEMBER/{domainId}/{userToAddLogin}")
    @TokenBearerAuth
    public DomainFullTO setUserMemberOfDomain(@RequestUser ApplicationUser loggedInUser,
                                              @PathVariable int domainId,
                                              @PathVariable String userToAddLogin) {
        Domain domain = domainService.setUserMemberOfDomain(
                loggedInUser,
                domainService.getById(domainId),
                applicationUserService.getByUserLogins(userToAddLogin));
        return detailedDomainInfo(loggedInUser, domain);
    }

    @Override
    @PostMapping("/ADMIN/{domainId}/{userToAddLogin}")
    @TokenBearerAuth
    public DomainFullTO setUserAdministratorOfDomain(@RequestUser ApplicationUser loggedInUser,
                                                     @PathVariable int domainId,
                                                     @PathVariable String userToAddLogin) {
        ApplicationUser userToAdd = applicationUserService.getByUserLogins(userToAddLogin);
        final Domain domain = domainService.getById(domainId);
        final Domain updatedDomain = domainService.setUserAdministratorOfDomain(loggedInUser, domain, userToAdd);
        return detailedDomainInfo(loggedInUser, updatedDomain);
    }

    @Override
    @DeleteMapping("/{domainId}/{userToRemoveLogin}")
    @TokenBearerAuth
    public DomainFullTO removeUserFromDomain(@RequestUser ApplicationUser loggedInUser,
                                             @PathVariable int domainId,
                                             @PathVariable String userToRemoveLogin) {
        ApplicationUser userToRemove = applicationUserService.getByUserLogins(userToRemoveLogin);
        final Domain domain = domainService.getById(domainId);
        final Domain updatedDomain = domainService.removeUserFromDomain(loggedInUser, domain, userToRemove);
        return detailedDomainInfo(loggedInUser, updatedDomain);
    }

    private DomainFullTO detailedDomainInfo(ApplicationUser user, Domain domain) {
        DomainFullTO result = new DomainFullTO()
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
