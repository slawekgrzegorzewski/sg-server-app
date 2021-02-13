package pl.sg.accountant.controller;

import pl.sg.accountant.transport.PiggyBankTO;
import pl.sg.application.model.ApplicationUser;

import java.util.List;

public interface PiggyBankController {

    List<PiggyBankTO> getAll(ApplicationUser user, int domainId);

    Integer create(ApplicationUser user, PiggyBankTO piggyBankTO);

    String update(ApplicationUser user, PiggyBankTO piggyBankTO);
}
