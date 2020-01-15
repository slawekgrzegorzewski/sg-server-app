package pl.sg.accountant.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import pl.sg.accountant.service.AccountstException;
import pl.sg.accountant.transport.AccountTO;
import pl.sg.accountant.transport.TransactionTO;

import java.math.BigDecimal;

public interface AccountsController {
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    ResponseEntity<AccountTO> createAccount(@RequestBody AccountTO account);

    @RequestMapping(value = "/transfer/{from}/{to}/{amount}", method = RequestMethod.POST)
    ResponseEntity<TransactionTO> transfer(
            @PathVariable("from") int fromId,
            @PathVariable("to") int toId,
            @PathVariable("amount") BigDecimal amount,
            @RequestBody String description) throws AccountstException;
}
