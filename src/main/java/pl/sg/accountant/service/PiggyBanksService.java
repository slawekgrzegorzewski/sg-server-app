package pl.sg.accountant.service;

import org.modelmapper.ModelMapper;
import pl.sg.accountant.model.PiggyBank;
import pl.sg.application.model.ApplicationUser;

import java.util.List;
import java.util.Optional;

public interface PiggyBanksService {
    PiggyBank getById(Integer id);

    List<PiggyBank> findByUser(ApplicationUser user);

    Optional<Integer> create(PiggyBank piggyBank);

    void update(PiggyBank piggyBank) throws AccountsException;
}
