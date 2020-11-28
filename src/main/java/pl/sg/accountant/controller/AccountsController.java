package pl.sg.accountant.controller;

import org.springframework.http.ResponseEntity;
import pl.sg.accountant.transport.accounts.AccountTO;

import javax.validation.Valid;
import java.util.List;

public interface AccountsController {

    List<AccountTO> allAccounts();

    List<AccountTO> userAccount(String login);

    AccountTO createAccount(@Valid AccountTO account, String login);

    ResponseEntity<String> updateAccount(@Valid AccountTO account, String login);

    ResponseEntity<String> deleteAccount(Integer id, String login);
}
