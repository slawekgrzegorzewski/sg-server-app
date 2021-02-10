package pl.sg.accountant.controller;

import org.springframework.http.ResponseEntity;
import pl.sg.accountant.transport.accounts.AccountTO;
import pl.sg.application.model.ApplicationUser;

import javax.validation.Valid;
import java.util.List;

public interface AccountsController {

    List<AccountTO> allAccounts();

    List<AccountTO> userAccount(ApplicationUser user, int domainId);

    AccountTO createAccount(ApplicationUser user, @Valid AccountTO account);

    ResponseEntity<String> updateAccount(ApplicationUser user, @Valid AccountTO account);

    ResponseEntity<String> deleteAccount(Integer id, ApplicationUser user);
}
