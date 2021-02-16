package pl.sg.application.service;

import org.springframework.stereotype.Component;
import pl.sg.application.DomainException;
import pl.sg.application.model.ApplicationUser;
import pl.sg.application.model.ApplicationUserDomainRelation;
import pl.sg.application.model.Domain;
import pl.sg.application.model.DomainInvitation;
import pl.sg.application.repository.ApplicationUserDomainRelationRepository;
import pl.sg.application.repository.DomainInvitationRepository;
import pl.sg.application.repository.DomainRepository;

import java.util.List;
import java.util.Optional;

import static pl.sg.application.model.DomainAccessLevel.ADMIN;
import static pl.sg.application.model.DomainAccessLevel.MEMBER;

@Component
public class DomainJPAService implements DomainService {

    private final ApplicationUserDomainRelationRepository applicationUserDomainRelationRepository;
    private final DomainRepository domainRepository;
    private final DomainInvitationRepository domainInvitationRepository;

    public DomainJPAService(ApplicationUserDomainRelationRepository applicationUserDomainRelationRepository,
                            DomainRepository domainRepository,
                            DomainInvitationRepository domainInvitationRepository) {
        this.applicationUserDomainRelationRepository = applicationUserDomainRelationRepository;
        this.domainRepository = domainRepository;
        this.domainInvitationRepository = domainInvitationRepository;
    }

    @Override
    public Domain getById(int domainId) {
        return domainRepository.getOne(domainId);
    }

    @Override
    public Optional<Domain> findById(int domainId) {
        return domainRepository.findById(domainId);
    }

    @Override
    public Domain create(ApplicationUser user, Domain newDomain) {
        newDomain.setId(null);
        final ApplicationUserDomainRelation newRelation = newDomain.addAssignedUsers(ADMIN, user);
        final Domain created = domainRepository.save(newDomain);
        applicationUserDomainRelationRepository.save(newRelation);
        return created;
    }

    @Override
    public Domain save(ApplicationUser user, Domain domain) {
        user.validateAdminDomain(domain);
        return domainRepository.save(domain);
    }

    @Override
    public List<DomainInvitation> findUserInvitations(ApplicationUser user) {
        return domainInvitationRepository.findByApplicationUser(user);
    }

    @Override
    public DomainInvitation invite(ApplicationUser loggedInUser, Domain domain, ApplicationUser userToInvite) {
        loggedInUser.validateAdminDomain(domain);
        getExistingRelation(userToInvite, domain)
                .map(r -> new DomainException("User " + userToInvite.getLogin() + " is already memeber of " + domain.getName() + " domain."))
                .ifPresent(ex -> {
                    throw ex;
                });
        return domainInvitationRepository.save(new DomainInvitation(userToInvite, domain));
    }

    @Override
    public void acceptInvitations(ApplicationUser user, Domain domain) {
        final DomainInvitation domainInvitation = findUserInvitations(user).stream()
                .filter(i -> i.getDomain().getId().equals(domain.getId()))
                .findFirst()
                .orElseThrow(() -> new DomainException("There is no invitation to " + domain.getName() + " domain for user " + user.getLogin()));
        domainInvitationRepository.delete(domainInvitation);
        addMemberToADomain(domain, user);
    }

    @Override
    public void rejectInvitations(ApplicationUser user, Domain domain) {
        final DomainInvitation domainInvitation = findUserInvitations(user).stream()
                .filter(i -> i.getDomain().getId().equals(domain.getId()))
                .findFirst()
                .orElseThrow(() -> new DomainException("There is no invitation to " + domain.getName() + " domain for user " + user.getLogin()));
        domainInvitationRepository.delete(domainInvitation);
    }

    @Override
    public Domain setUserMemberOfDomain(ApplicationUser loggedInUser, Domain domain, ApplicationUser userToAdd) {
        loggedInUser.validateAdminDomain(domain);
        return addMemberToADomain(domain, userToAdd);
    }

    @Override
    public Domain setUserAdministratorOfDomain(ApplicationUser loggedInUser, Domain domain, ApplicationUser userToAdd) {
        loggedInUser.validateAdminDomain(domain);
        getExistingRelation(userToAdd, domain).ifPresent(relation -> {
            domain.getAssignedUsers().remove(relation);
            applicationUserDomainRelationRepository.delete(relation);
        });
        final ApplicationUserDomainRelation newRelation = domain.addAssignedUsers(ADMIN, userToAdd);
        applicationUserDomainRelationRepository.save(newRelation);
        return save(loggedInUser, domain);
    }

    @Override
    public Domain removeUserFromDomain(ApplicationUser loggedInUser, Domain domain, ApplicationUser userToRemove) {
        if (!loggedInUser.getLogin().equals(userToRemove.getLogin())) {
            loggedInUser.validateAdminDomain(domain);
        }
        final Optional<ApplicationUserDomainRelation> relationToDelete = getExistingRelation(userToRemove, domain);
        relationToDelete.ifPresent(domain.getAssignedUsers()::remove);
        final long numberOfAdmins = domain.getAssignedUsers().stream()
                .filter(r -> r.getAccessLevel() == ADMIN)
                .count();
        if (numberOfAdmins > 0) {
            relationToDelete.ifPresent(applicationUserDomainRelationRepository::delete);
            domain = domainRepository.save(domain);
        } else {
            relationToDelete.ifPresent(domain.getAssignedUsers()::add);
        }
        return domain;
    }

    @Override
    public Optional<ApplicationUserDomainRelation> getExistingRelation(ApplicationUser user, Domain domain) {
        return domain.getAssignedUsers().stream()
                .filter(r -> r.getDomain().getId().equals(domain.getId()))
                .filter(r -> r.getApplicationUser().getId().equals(user.getId()))
                .findFirst();
    }

    private Domain addMemberToADomain(Domain domain, ApplicationUser userToAdd) {
        final Optional<ApplicationUserDomainRelation> relationToDelete = getExistingRelation(userToAdd, domain);
        relationToDelete.ifPresent(domain.getAssignedUsers()::remove);
        final long numberOfAdmins = domain.getAssignedUsers().stream()
                .filter(r -> r.getAccessLevel() == ADMIN)
                .count();
        if (numberOfAdmins > 0) {
            final ApplicationUserDomainRelation newRelation = domain.addAssignedUsers(MEMBER, userToAdd);
            relationToDelete.ifPresent(applicationUserDomainRelationRepository::delete);
            applicationUserDomainRelationRepository.save(newRelation);
            domain = domainRepository.save(domain);
        } else {
            relationToDelete.ifPresent(domain.getAssignedUsers()::add);
        }
        return domain;
    }
}
