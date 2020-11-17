package pl.sg.accountant.model;

import pl.sg.accountant.model.accounts.Account;
import pl.sg.accountant.model.accounts.FinancialTransaction;

import java.util.function.BiConsumer;

public enum OperationType {

    CREDIT(Account::credit), DEBIT(Account::debit);
    private final BiConsumer<Account, FinancialTransaction> operation;

    OperationType(BiConsumer<Account, FinancialTransaction> operation) {
        this.operation = operation;
    }

    public BiConsumer<Account, FinancialTransaction> getOperation() {
        return operation;
    }
}
