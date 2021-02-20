package pl.sg.accountant.controller;

import pl.sg.accountant.model.billings.PiggyBank;
import pl.sg.accountant.transport.PiggyBankTO;
import pl.sg.application.model.ApplicationUser;
import pl.sg.application.model.Domain;

import java.util.List;

public interface PiggyBankController {

    List<PiggyBankTO> getAll(Domain domain);

    Integer create(PiggyBank piggyBank);

    String update(PiggyBank piggyBank);
}
