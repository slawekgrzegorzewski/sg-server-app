package pl.sg.accountant.service;

import org.springframework.stereotype.Component;
import pl.sg.accountant.model.billings.PiggyBank;
import pl.sg.accountant.repository.PiggyBankRepository;
import pl.sg.application.model.ApplicationUser;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static java.util.Optional.of;

@Component
public class PiggyBanksJPAService implements PiggyBanksService {

    private final PiggyBankRepository piggyBankRepository;

    public PiggyBanksJPAService(PiggyBankRepository piggyBankRepository) {
        this.piggyBankRepository = piggyBankRepository;
    }

    @Override
    public PiggyBank getById(Integer id) {
        return this.piggyBankRepository.getOne(id);
    }

    @Override
    public List<PiggyBank> findByUser(ApplicationUser user) {
        return piggyBankRepository.findByApplicationUser(user);
    }

    @Override
    public Optional<Integer> create(PiggyBank piggyBank) {
        return of(piggyBankRepository.save(piggyBank).getId());
    }

    @Override
    public void update(PiggyBank piggyBank) throws AccountsException {
        PiggyBank tuUpdate = piggyBankRepository.getOne(piggyBank.getId());
        validateUser(tuUpdate, piggyBank.getApplicationUser());
        this.piggyBankRepository.save(piggyBank);
    }

    @Override
    public void updateAll(List<PiggyBank> piggyBanks) throws AccountsException {
        List<Integer> ids = piggyBanks.stream().map(PiggyBank::getId).collect(Collectors.toList());
        Map<Integer, ApplicationUser> fromDB = piggyBankRepository
                .findAllById(ids)
                .stream()
                .collect(Collectors.toMap(PiggyBank::getId, PiggyBank::getApplicationUser));
        for (PiggyBank piggyBank : piggyBanks) {
            validateUser(piggyBank, fromDB.get(piggyBank.getId()));
        }
        this.piggyBankRepository.saveAll(piggyBanks);
    }

    private void validateUser(PiggyBank toUpdate, ApplicationUser applicationUser) throws AccountsException {
        if (!toUpdate.getApplicationUser().equals(applicationUser)) {
            throw new AccountsException("Changing owner is not allowed");
        }
    }
}
