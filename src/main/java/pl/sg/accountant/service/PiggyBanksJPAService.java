package pl.sg.accountant.service;

import org.springframework.stereotype.Component;
import pl.sg.accountant.model.PiggyBank;
import pl.sg.accountant.repository.PiggyBankRepository;
import pl.sg.application.model.ApplicationUser;

import java.util.List;
import java.util.Optional;

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

    private void validateUser(PiggyBank toUpdate, ApplicationUser applicationUser) throws AccountsException {
        if (!toUpdate.getApplicationUser().equals(applicationUser)) {
            throw new AccountsException("Changing owner is not allowed");
        }
    }
}
