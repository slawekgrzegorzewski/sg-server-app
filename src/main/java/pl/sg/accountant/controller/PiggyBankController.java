package pl.sg.accountant.controller;

import org.springframework.http.ResponseEntity;
import pl.sg.accountant.transport.PiggyBankTO;
import pl.sg.application.model.ApplicationUser;

import java.util.List;

public interface PiggyBankController {

    ResponseEntity<List<PiggyBankTO>> getAll(ApplicationUser user, int domainId);

    ResponseEntity<Integer> create(ApplicationUser user, PiggyBankTO piggyBankTO);

    ResponseEntity<String> update(ApplicationUser user, PiggyBankTO piggyBankTO);
}
