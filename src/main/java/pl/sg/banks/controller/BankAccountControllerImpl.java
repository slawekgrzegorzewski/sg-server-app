package pl.sg.banks.controller;

import org.modelmapper.ModelMapper;
import org.springframework.web.bind.annotation.*;
import pl.sg.accountant.model.accounts.Account;
import pl.sg.application.model.Domain;
import pl.sg.application.security.annotations.*;
import pl.sg.banks.repositories.BankAccountRepository;
import pl.sg.banks.services.BankAccountService;
import pl.sg.banks.transport.BankAccount;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/banks")
public class BankAccountControllerImpl implements BankAccountController {

    private final BankAccountRepository bankAccountRepository;
    private final BankAccountService bankAccountService;
    private final ModelMapper modelMapper;

    public BankAccountControllerImpl(BankAccountRepository bankAccountRepository, BankAccountService bankAccountService, ModelMapper modelMapper) {
        this.bankAccountRepository = bankAccountRepository;
        this.bankAccountService = bankAccountService;
        this.modelMapper = modelMapper;
    }

    @Override
    @GetMapping("/account_to_assign")
    @TokenBearerAuth(any = {"ACCOUNTANT_ADMIN", "ACCOUNTANT_USER"})
    public List<BankAccount> getBankAccountsNotAssignedToAnyAccount(@RequestDomain Domain domain) {
        return bankAccountRepository.findBankAccountsNotAssignedToAccount(domain)
                .stream()
                .map(bankAccount -> modelMapper.map(bankAccount, BankAccount.class))
                .collect(Collectors.toList());
    }

    @Override
    @PutMapping("/{bankAccount}")
    @TokenBearerAuth(any = {"ACCOUNTANT_ADMIN", "ACCOUNTANT_USER"})
    public void assignBankAccountToAnAccount(
            @RequestDomain Domain domain,
            @PathVariableWithDomain(requireAdmin = true) pl.sg.banks.model.BankAccount bankAccount,
            @RequestBodyIdWithDomain(domainAdmin = true, required = true) Account account) {
        this.bankAccountService.assignBankAccountToAnAccount(domain, bankAccount, account);
    }

    @Override
    @PostMapping("/transactions")
    @TokenBearerAuth(any = {"ACCOUNTANT_ADMIN", "ACCOUNTANT_USER"})
    public void fetchAllTransactions(@RequestDomain Domain domain) {
        bankAccountService.fetchAllTransactions(domain);
    }

    @Override
    @PostMapping("/balances")
    @TokenBearerAuth(any = {"ACCOUNTANT_ADMIN", "ACCOUNTANT_USER"})
    public void fetchAllBalances(@RequestDomain Domain domain) {
        bankAccountService.fetchAllBalances(domain);
    }
}