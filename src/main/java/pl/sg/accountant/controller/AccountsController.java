package pl.sg.accountant.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import pl.sg.accountant.service.AccountstException;
import pl.sg.accountant.transport.AccountTO;
import pl.sg.accountant.transport.TransactionTO;

import java.math.BigDecimal;
import java.util.List;

public interface AccountsController {

    ResponseEntity<List<AccountTO>> accounts(AccountTO account, String token);

    ResponseEntity<AccountTO> createAccount(AccountTO account, String token);

    ResponseEntity<TransactionTO> transfer(int fromId, int toId, BigDecimal amount, String description, String token) throws AccountstException;
}
