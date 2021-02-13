package pl.sg.accountant.service;

import pl.sg.accountant.model.billings.PiggyBank;
import pl.sg.application.model.ApplicationUser;

import java.util.List;

public interface PiggyBanksService {

    PiggyBank getById(ApplicationUser user, Integer id);

    List<PiggyBank> findByDomain(ApplicationUser user, int domainId);

    Integer create(ApplicationUser user, PiggyBank piggyBank);

    void update(ApplicationUser user, PiggyBank piggyBank);

    void updateAll(ApplicationUser user, List<PiggyBank> piggyBanks);
}
