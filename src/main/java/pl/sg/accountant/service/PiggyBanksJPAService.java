package pl.sg.accountant.service;

import org.springframework.stereotype.Component;
import pl.sg.accountant.model.billings.PiggyBank;
import pl.sg.accountant.repository.PiggyBankRepository;
import pl.sg.application.model.ApplicationUser;
import pl.sg.application.model.Domain;
import pl.sg.application.service.DomainService;

import java.util.List;
import java.util.stream.Collectors;

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
    public List<PiggyBank> findByDomain(Domain domain) {
        return piggyBankRepository.findByDomain(domain);
    }

    @Override
    public Integer create(PiggyBank piggyBank) {
        piggyBank.setId(null);
        return piggyBankRepository.save(piggyBank).getId();
    }

    @Override
    public void update(PiggyBank piggyBank) {
        this.piggyBankRepository.save(piggyBank);
    }

    @Override
    public void updateAll(List<PiggyBank> piggyBanks) {;
        this.piggyBankRepository.saveAll(piggyBanks);
    }
}
