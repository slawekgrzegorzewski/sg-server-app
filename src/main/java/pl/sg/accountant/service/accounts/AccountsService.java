package pl.sg.accountant.service.accounts;

import jakarta.annotation.Nullable;
import pl.sg.accountant.model.accounts.Account;
import pl.sg.graphql.schema.types.MonetaryAmountInput;

import java.util.List;
import java.util.UUID;

public interface AccountsService {

    Account getById(Integer accountId);

    List<Account> getAll();

    List<Account> getForDomain(long domainId);

    Account createAccount(Account account);

    Account createAccount(int domainId, String name, MonetaryAmountInput currentBalance, MonetaryAmountInput creditLimit, boolean visible, @Nullable Integer bankAccountId, @Nullable Integer balanceIndex);

    Account update(Account account);

    Account update(int domainId, UUID publicId, String name, MonetaryAmountInput currentBalance, MonetaryAmountInput creditLimit, boolean visible, @Nullable Integer bankAccountId, @Nullable Integer balanceIndex);

    void delete(Account account);

    void delete(int domainId, UUID accountPublicId);
}
