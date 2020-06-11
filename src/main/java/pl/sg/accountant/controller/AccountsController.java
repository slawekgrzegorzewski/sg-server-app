package pl.sg.accountant.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import pl.sg.accountant.service.AccountstException;
import pl.sg.accountant.transport.AccountTO;
import pl.sg.accountant.transport.TransactionTO;

import java.math.BigDecimal;
import java.util.List;

public interface AccountsController {
    ResponseEntity<List<AccountTO>> accounts(AccountTO account);

    ResponseEntity<AccountTO> createAccount(AccountTO account);

    ResponseEntity<TransactionTO> transfer(int fromId, int toId, BigDecimal amount, String description) throws AccountstException;
}
