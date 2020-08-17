package pl.sg.accountant.controller;

import java.math.BigDecimal;
import java.util.List;
import org.springframework.http.ResponseEntity;
import pl.sg.accountant.service.AccountstException;
import pl.sg.accountant.transport.AccountTO;
import pl.sg.accountant.transport.TransactionTO;
import pl.sg.application.model.ApplicationUser;

public interface AccountsController {

    ResponseEntity<List<AccountTO>> accounts(ApplicationUser applicationUser);

    ResponseEntity<AccountTO> createAccount(AccountTO account, String login);

    ResponseEntity<TransactionTO> transfer(int fromId, int toId, BigDecimal amount, String description, String login) throws AccountstException;
}
