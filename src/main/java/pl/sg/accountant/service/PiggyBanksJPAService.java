package pl.sg.accountant.service;

import org.springframework.stereotype.Component;
import pl.sg.accountant.model.billings.PiggyBank;
import pl.sg.accountant.repository.PiggyBankRepository;
import pl.sg.application.model.ApplicationUser;
import pl.sg.application.model.Domain;
import pl.sg.application.service.DomainService;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static java.util.Optional.of;

@Component
public class PiggyBanksJPAService implements PiggyBanksService {

    private final PiggyBankRepository piggyBankRepository;
    private final DomainService domainService;

    public PiggyBanksJPAService(PiggyBankRepository piggyBankRepository, DomainService domainService) {
        this.piggyBankRepository = piggyBankRepository;
        this.domainService = domainService;
    }

    @Override
    public PiggyBank getById(ApplicationUser user, Integer id) {
        final PiggyBank result = this.piggyBankRepository.getOne(id);
        user.validateDomain(result.getDomain());
        return result;
    }

    @Override
    public List<PiggyBank> findByDomain(ApplicationUser user, int domainId) {
        final Domain domain = domainService.getById(domainId);
        user.validateDomain(domain);
        return piggyBankRepository.findByDomainId(domainId);
    }

    @Override
    public Optional<Integer> create(ApplicationUser user, PiggyBank piggyBank) {
        user.validateAdminDomain(piggyBank.getDomain());
        piggyBank.setId(null);
        return of(piggyBankRepository.save(piggyBank).getId());
    }

    @Override
    public void update(ApplicationUser user, PiggyBank piggyBank) {
        PiggyBank dbValue = piggyBankRepository.getOne(piggyBank.getId());
        user.validateAdminDomain(dbValue.getDomain());
        this.piggyBankRepository.save(piggyBank);
    }

    @Override
    public void updateAll(ApplicationUser user, List<PiggyBank> piggyBanks) {
        List<Integer> ids = piggyBanks.stream().map(PiggyBank::getId).collect(Collectors.toList());
        piggyBankRepository
                .findAllById(ids)
                .stream()
                .map(PiggyBank::getDomain)
                .forEach(user::validateAdminDomain);
        this.piggyBankRepository.saveAll(piggyBanks);
    }
}
