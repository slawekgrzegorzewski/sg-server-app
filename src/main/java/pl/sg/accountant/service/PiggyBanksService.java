package pl.sg.accountant.service;

import pl.sg.accountant.model.billings.PiggyBank;
import pl.sg.application.model.ApplicationUser;
import pl.sg.application.model.Domain;

import java.util.List;

public interface PiggyBanksService {

    PiggyBank getById(ApplicationUser user, Integer id);

    List<PiggyBank> findByDomain(Domain domain);

    Integer create(PiggyBank piggyBank);

    void update(PiggyBank piggyBank);

    void updateAll(List<PiggyBank> piggyBanks);
}
