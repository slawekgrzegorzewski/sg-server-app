package pl.sg.application.controller;

import org.modelmapper.ModelMapper;
import org.springframework.web.bind.annotation.*;
import pl.sg.application.model.ApplicationUser;
import pl.sg.application.model.ApplicationUserDomainRelation;
import pl.sg.application.model.Domain;
import pl.sg.application.model.DomainAccessLevel;
import pl.sg.application.security.annotations.RequestUser;
import pl.sg.application.security.annotations.TokenBearerAuth;
import pl.sg.application.service.ApplicationUserService;
import pl.sg.application.service.DomainService;
import pl.sg.application.transport.DomainTO;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/domains")
public class DomainRestController implements DomainController {

    private final DomainService domainService;
    private final ApplicationUserService applicationUserService;
    private final ModelMapper mapper;

    public DomainRestController(DomainService domainService, ApplicationUserService applicationUserService, ModelMapper mapper) {
        this.domainService = domainService;
        this.applicationUserService = applicationUserService;
        this.mapper = mapper;
    }

    @Override
    @GetMapping
    @TokenBearerAuth
    public List<DomainTO> allUserDomains(@RequestUser ApplicationUser user) {
        return user.getAssignedDomains().stream()
                .map(ApplicationUserDomainRelation::getDomain)
                .map(d -> mapper.map(d, DomainTO.class))
                .collect(Collectors.toList());
    }

    @Override
    @PutMapping("/{domainName}")
    @TokenBearerAuth
    public DomainTO createDomain(@RequestUser ApplicationUser user, @PathVariable String domainName) {
        final Domain newDomain = new Domain().setName(domainName).addAssignedUsers(DomainAccessLevel.ADMIN, user);
        //TODO Check if user's relation is being update actually
        return mapper.map(domainService.create(newDomain), DomainTO.class);
    }

    @Override
    @PostMapping("/MEMBER/{domainId}/{userToAddLogin}")
    @TokenBearerAuth
    public DomainTO setUserMemberOfDomain(@RequestUser ApplicationUser loggedInUser,
                                          @PathVariable int domainId,
                                          @PathVariable String userToAddLogin) {
        ApplicationUser userToAdd = applicationUserService.getByUserLogins(userToAddLogin);
        Domain domain = domainService.getById(domainId);
        loggedInUser.validateAdminDomain(domain);
        getExistingRelation(userToAdd, domain).ifPresent(domain.getAssignedUsers()::remove);
        final long numberOfAdmins = domain.getAssignedUsers().stream()
                .filter(r -> r.getAccessLevel() == DomainAccessLevel.ADMIN)
                .count();
        if (numberOfAdmins > 0) {
            domain.addAssignedUsers(DomainAccessLevel.MEMBER, userToAdd);
            domain = domainService.save(loggedInUser, domain);
        }
        return mapper.map(domain, DomainTO.class);
    }

    @Override
    public DomainTO setUserAdministratorOfDomain(@RequestUser ApplicationUser loggedInUser,
                                                 @PathVariable int domainId,
                                                 @PathVariable String userToAddLogin) {
        ApplicationUser userToAdd = applicationUserService.getByUserLogins(userToAddLogin);
        Domain domain = domainService.getById(domainId);
        loggedInUser.validateAdminDomain(domain);
        getExistingRelation(userToAdd, domain).ifPresent(domain.getAssignedUsers()::remove);
        domain.addAssignedUsers(DomainAccessLevel.ADMIN, userToAdd);
        domain = domainService.save(loggedInUser, domain);
        return mapper.map(domain, DomainTO.class);
    }

    private Optional<ApplicationUserDomainRelation> getExistingRelation(ApplicationUser user, Domain domain) {
        return domain.getAssignedUsers().stream()
                .filter(r -> r.getDomain().getId().equals(domain.getId()))
                .findFirst();
    }
}
