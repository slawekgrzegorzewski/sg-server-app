package pl.sg.accountant.service;

import pl.sg.accountant.model.billings.PiggyBank;
import pl.sg.application.model.ApplicationUser;

import java.util.List;
import java.util.Optional;

public interface PiggyBanksService {
    PiggyBank getById(Integer id);

    List<PiggyBank> findByUser(ApplicationUser user);

    Optional<Integer> create(PiggyBank piggyBank);

    void update(PiggyBank piggyBank) throws AccountsException;

    void updateAll(List<PiggyBank> piggyBanks) throws AccountsException;
}
