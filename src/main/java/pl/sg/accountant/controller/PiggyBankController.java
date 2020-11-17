package pl.sg.accountant.controller;

import org.springframework.http.ResponseEntity;
import pl.sg.accountant.service.AccountsException;
import pl.sg.accountant.transport.PiggyBankTO;
import pl.sg.application.model.ApplicationUser;

import java.util.List;

public interface PiggyBankController {

    ResponseEntity<List<PiggyBankTO>> getAll(ApplicationUser user);

    ResponseEntity<Integer> create(PiggyBankTO piggyBankTO, ApplicationUser user);

    ResponseEntity<String> update (PiggyBankTO piggyBankTO, ApplicationUser user) throws AccountsException;
}
