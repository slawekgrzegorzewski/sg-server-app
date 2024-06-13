package pl.sg.accountant.service.accounts;

import jakarta.annotation.Nullable;
import org.javamoney.moneta.Money;
import org.springframework.stereotype.Component;
import pl.sg.accountant.model.AccountsException;
import pl.sg.accountant.model.accounts.Account;
import pl.sg.accountant.repository.accounts.AccountRepository;
import pl.sg.accountant.repository.ledger.FinancialTransactionRepository;
import pl.sg.application.repository.DomainRepository;
import pl.sg.banks.repositories.BankAccountRepository;
import pl.sg.graphql.schema.types.MonetaryAmountInput;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import static java.util.Optional.ofNullable;
import static pl.sg.application.DomainValidator.validateDomain;

@Component
public class AccountsJPAService implements AccountsService {
    private final AccountRepository accountRepository;
    private final BankAccountRepository bankAccountRepository;
    private final DomainRepository domainRepository;
    private final FinancialTransactionRepository financialTransactionRepository;

    public AccountsJPAService(AccountRepository accountRepository, BankAccountRepository bankAccountRepository, DomainRepository domainRepository, FinancialTransactionRepository financialTransactionRepository) {
        this.accountRepository = accountRepository;
        this.bankAccountRepository = bankAccountRepository;
        this.domainRepository = domainRepository;
        this.financialTransactionRepository = financialTransactionRepository;
    }

    @Override
    public Account getById(Integer accountId) {
        return accountRepository.getOne(accountId);
    }

    @Override
    public List<Account> getAll() {
        return accountRepository.findAll();
    }

    @Override
    public List<Account> getForDomain(long domainId) {
        return accountRepository.findByDomain(domainRepository.getReferenceById((int) domainId));
    }

    @Override
    public Account createAccount(Account account) {
        validateCurrencies(account);
        validateCreditLimit(account);
        account.setId(null);
        return accountRepository.save(account);
    }

    @Override
    public Account createAccount(
            int domainId,
            String name,
            MonetaryAmountInput creditLimit,
            boolean visible,
            @Nullable Integer bankAccountId,
            @Nullable Integer balanceIndex) {
        Account account = new Account();
        account.setDomain(domainRepository.getReferenceById(domainId));
        account.setName(name);
        account.setCurrentBalance(Money.of(BigDecimal.ZERO, creditLimit.getCurrency().getCurrencyCode()));
        account.setCreditLimit(map(creditLimit));
        account.setVisible(visible);
        account.setBankAccount(ofNullable(bankAccountId).map(bankAccountRepository::getReferenceById).orElse(null));
        account.setLastTransactionIncludedInBalance(ofNullable(balanceIndex).map(financialTransactionRepository::getReferenceById).orElse(null));
        return createAccount(account);
    }

    @Override
    public Account update(Account toEdit) {
        validateCurrencies(toEdit);
        validateCreditLimit(toEdit);
        return accountRepository.save(toEdit);
    }

    @Override
    public Account update(
            int domainId,
            UUID publicId,
            String name,
            MonetaryAmountInput creditLimit,
            boolean visible,
            @Nullable Integer bankAccountId,
            @Nullable Integer balanceIndex) {
        Account account = Objects.requireNonNull(accountRepository.findByPublicId(publicId));
        validateDomain(domainId, account.getDomain());
        account.setName(name);
        account.setCreditLimit(map(creditLimit));
        account.setVisible(visible);
        account.setBankAccount(ofNullable(bankAccountId).map(bankAccountRepository::getReferenceById).orElse(null));
        account.setLastTransactionIncludedInBalance(ofNullable(balanceIndex).map(financialTransactionRepository::getReferenceById).orElse(null));
        return update(account);
    }

    private void validateCurrencies(Account account) {
        if (!account.getCreditLimit().getCurrency().equals(account.getCurrentBalance().getCurrency())) {
            throw new AccountsException("Currencies are not the same");
        }
    }

    private void validateCreditLimit(Account account) {
        if (!account.getAvailableBalance().isPositiveOrZero()) {
            throw new AccountsException("Credit limit too low");
        }
    }

    @Override
    public void delete(Account toDelete) {
        accountRepository.delete(toDelete);
    }

    @Override
    public void delete(int domainId, UUID accountPublicId) {
        Account account = Objects.requireNonNull(accountRepository.findByPublicId(accountPublicId));
        validateDomain(domainId, account.getDomain());
        accountRepository.delete(account);
    }

    private static javax.money.MonetaryAmount map(MonetaryAmountInput monetaryAmount) {
        return Money.of(
                monetaryAmount.getAmount(),
                monetaryAmount.getCurrency().getCurrencyCode());
    }
}
