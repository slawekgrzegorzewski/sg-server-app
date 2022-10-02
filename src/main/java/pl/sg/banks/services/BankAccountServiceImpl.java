package pl.sg.banks.services;

import org.springframework.stereotype.Controller;
import pl.sg.accountant.model.AccountsException;
import pl.sg.accountant.model.accounts.Account;
import pl.sg.accountant.repository.AccountRepository;
import pl.sg.application.model.Domain;
import pl.sg.banks.model.BankAccount;
import pl.sg.banks.repositories.BankAccountRepository;
import pl.sg.integrations.nodrigen.EUAEndedException;
import pl.sg.integrations.nodrigen.NodrigenClient;
import pl.sg.integrations.nodrigen.model.balances.NodrigenAmount;
import pl.sg.integrations.nodrigen.model.balances.NodrigenBalanceEmbeddable;
import pl.sg.integrations.nodrigen.model.balances.NodrigenBankAccountBalance;
import pl.sg.integrations.nodrigen.model.rest.balances.Amount;
import pl.sg.integrations.nodrigen.model.rest.balances.Balance;
import pl.sg.integrations.nodrigen.model.rest.transactions.CurrencyExchange;
import pl.sg.integrations.nodrigen.model.rest.transactions.Transaction;
import pl.sg.integrations.nodrigen.model.rest.transactions.Transactions;
import pl.sg.integrations.nodrigen.model.transcations.NodrigenAccount;
import pl.sg.integrations.nodrigen.model.transcations.NodrigenCurrencyExchange;
import pl.sg.integrations.nodrigen.model.transcations.NodrigenPhase;
import pl.sg.integrations.nodrigen.model.transcations.NodrigenTransaction;
import pl.sg.integrations.nodrigen.repository.NodrigenBankAccountBalanceRepository;
import pl.sg.integrations.nodrigen.repository.NodrigenTransactionRepository;
import pl.sg.integrations.nodrigen.services.NodrigenService;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.Optional.ofNullable;

@Controller
public class BankAccountServiceImpl implements BankAccountService {

    private final AccountRepository accountRepository;
    private final BankAccountRepository bankAccountRepository;
    private final NodrigenClient nodrigenClient;
    private final NodrigenBankAccountBalanceRepository nodrigenBankAccountBalanceRepository;
    private final NodrigenTransactionRepository nodrigenTransactionRepository;
    private final NodrigenService nodrigenService;

    public BankAccountServiceImpl(AccountRepository accountRepository, BankAccountRepository bankAccountRepository, NodrigenClient nodrigenClient, NodrigenBankAccountBalanceRepository nodrigenBankAccountBalanceRepository, NodrigenTransactionRepository nodrigenTransactionRepository, NodrigenService nodrigenService) {
        this.accountRepository = accountRepository;
        this.bankAccountRepository = bankAccountRepository;
        this.nodrigenClient = nodrigenClient;
        this.nodrigenBankAccountBalanceRepository = nodrigenBankAccountBalanceRepository;
        this.nodrigenTransactionRepository = nodrigenTransactionRepository;
        this.nodrigenService = nodrigenService;
    }

    @Override
    public void assignBankAccountToAnAccount(Domain domain, BankAccount bankAccount, Account account) {
        validateTheSameDomain(domain, bankAccount.getDomain());
        validateTheSameDomain(domain, account.getDomain());
        validateBankAccountDoesntHaveOtherAccountAssigned(bankAccount);
        validateAccountDoesntHaveOtherBankAccountAssigned(account);
        bankAccount.setAccount(account);
        account.setBankAccount(bankAccount);
        bankAccountRepository.save(bankAccount);
        accountRepository.save(account);
    }

    @Override
    public void fetchAllTransactions(Domain domain) {
        bankAccountRepository.findAllBankAccounts(domain).forEach(this::fetchAccountTransactions);
    }

    @Override
    public void fetchAccountTransactions(BankAccount bankAccount) {
        try {
            Transactions allTransactionsFromNodrigen =
                    nodrigenClient.getTransactions(UUID.fromString(bankAccount.getExternalId()))
                            .map(t -> t.transactions)
                            .orElseGet(Transactions::new);
            List<String> allNodrigenTransactionIds = Stream.concat(
                            allTransactionsFromNodrigen.booked.stream(),
                            allTransactionsFromNodrigen.pending.stream()
                    )
                    .map(transaction -> transaction.transactionId)
                    .collect(Collectors.toList());

            Map<String, List<NodrigenTransaction>> existingTransactions = nodrigenTransactionRepository.findAllByTransactionIdIn(allNodrigenTransactionIds)
                    .stream()
                    .collect(Collectors.groupingBy(
                            NodrigenTransaction::getTransactionId,
                            Collectors.toList()
                    ));

            LocalDateTime now = LocalDateTime.now();
            List<NodrigenTransaction> toSave = new ArrayList<>();
            allTransactionsFromNodrigen.booked.stream()
                    .filter(transaction -> !transactionsExists(existingTransactions, transaction, bankAccount))
                    .map(transaction -> mapToDb(transaction, bankAccount, now, NodrigenPhase.BOOKED))
                    .collect(Collectors.toCollection(() -> toSave));
            allTransactionsFromNodrigen.pending.stream()
                    .filter(transaction -> !transactionsExists(existingTransactions, transaction, bankAccount))
                    .map(transaction -> mapToDb(transaction, bankAccount, now, NodrigenPhase.PENDING))
                    .collect(Collectors.toCollection(() -> toSave));
            nodrigenTransactionRepository.saveAll(toSave);

        } catch (EUAEndedException exception) {
            nodrigenService.recreateRequisitionIfNeeded(bankAccount);
        }


    }

    private boolean transactionsExists(Map<String, List<NodrigenTransaction>> existingTransactions, Transaction transaction, BankAccount bankAccount) {
        if (!existingTransactions.containsKey(transaction.transactionId)) {
            return false;
        }
        List<NodrigenTransaction> nodrigenTransactions = existingTransactions.get(transaction.transactionId);
        return nodrigenTransactions.stream()
                .anyMatch(nodrigenTransaction -> nodrigenTransaction.getBankAccount().getId().equals(bankAccount.getId()));
    }

    private NodrigenTransaction mapToDb(Transaction transaction, BankAccount bankAccount, LocalDateTime now, NodrigenPhase phase) {
        return new NodrigenTransaction()
                .setBankAccount(bankAccount)
                .setImportTime(now)
                .setPhase(phase)
                .setAdditionalInformation(transaction.additionalInformation)
                .setAdditionalInformationStructured(transaction.additionalInformationStructured)
                .setBookingDate(transaction.bookingDate)
                .setBalanceAfterTransaction(mapNodrigenBalanceAfterTransaction(transaction.balanceAfterTransaction))
                .setBankTransactionCode(transaction.bankTransactionCode)
                .setBookingDateTime(transaction.bookingDateTime)
                .setCheckId(transaction.checkId)
                .setCreditorAccount(mapNodrigenAccount(transaction.creditorAccount))
                .setCreditorAgent(transaction.creditorAgent)
                .setCreditorId(transaction.creditorId)
                .setCreditorName(transaction.creditorName)
                .setCurrencyExchange(mapNodrigenCurrencyExchange(transaction.currencyExchange))
                .setDebtorAccount(mapNodrigenAccount(transaction.debtorAccount))
                .setDebtorAgent(transaction.debtorAgent)
                .setDebtorName(transaction.debtorName)
                .setEntryReference(transaction.entryReference)
                .setMandateId(transaction.mandateId)
                .setProprietaryBankTransactionCode(transaction.proprietaryBankTransactionCode)
                .setPurposeCode(transaction.purposeCode)
                .setRemittanceInformationStructured(transaction.remittanceInformationStructured)
                .setRemittanceInformationStructuredArray(ofNullable(transaction.remittanceInformationStructuredArray).map(Arrays::toString).orElse(null))
                .setRemittanceInformationUnstructured(transaction.remittanceInformationUnstructured)
                .setRemittanceInformationUnstructuredArray(ofNullable(transaction.remittanceInformationUnstructuredArray).map(Arrays::toString).orElse(null))
                .setTransactionAmount(mapNodrigenAmount(transaction.transactionAmount))
                .setTransactionId(transaction.transactionId)
                .setUltimateCreditor(transaction.ultimateCreditor)
                .setUltimateDebtor(transaction.ultimateDebtor)
                .setValueDate(transaction.valueDate)
                .setValueDateTime(transaction.valueDateTime);
    }

    private NodrigenBalanceEmbeddable mapNodrigenBalanceAfterTransaction(Balance balance) {
        return ofNullable(balance)
                .map(bat -> new NodrigenBalanceEmbeddable()
                        .setBalanceAmount(mapNodrigenBalanceAmount(bat.balanceAmount))
                        .setBalanceType(bat.balanceType)
                        .setCreditLimitIncluded(bat.creditLimitIncluded)
                        .setLastCommittedTransaction(bat.lastCommittedTransaction)
                        .setLastChangeDateTime(bat.lastChangeDateTime)
                        .setReferenceDate(bat.referenceDate))
                .orElse(null);
    }

    private NodrigenAmount mapNodrigenBalanceAmount(Amount amount) {
        return ofNullable(amount)
                .map(ba -> new NodrigenAmount().setAmount(ba.amount).setCurrency(ba.currency))
                .orElse(null);
    }

    private NodrigenCurrencyExchange mapNodrigenCurrencyExchange(CurrencyExchange currencyExchange) {
        return ofNullable(currencyExchange)
                .map(ce -> new NodrigenCurrencyExchange()
                        .setExchangeRate(ce.exchangeRate)
                        .setInstructedAmount(mapNodrigenAmount(ce.instructedAmount))
                        .setSourceCurrency(ce.sourceCurrency)
                        .setTargetCurrency(ce.targetCurrency)
                        .setUnitCurrency(ce.unitCurrency))
                .orElse(null);
    }

    private NodrigenAccount mapNodrigenAccount(pl.sg.integrations.nodrigen.model.rest.transactions.Account creditorAccount) {
        return ofNullable(creditorAccount)
                .map(ca -> new NodrigenAccount().setBban(ca.bban).setIban(ca.iban))
                .orElse(null);
    }

    private NodrigenAmount mapNodrigenAmount(Amount transactionAmount) {
        return ofNullable(transactionAmount)
                .map(ta -> new NodrigenAmount().setAmount(ta.amount).setCurrency(ta.currency))
                .orElse(null);
    }

    @Override
    public void fetch(Domain domain, String bankAccountExternalId) {
        BankAccount bankAccount = bankAccountRepository.getBankAccountByExternalId(bankAccountExternalId);
        validateTheSameDomain(bankAccount.getDomain(), domain);
        fetchAccountBalances(bankAccount);
        fetchAccountTransactions(bankAccount);
    }

    @Override
    public void fetchAllBalances(Domain domain) {
        bankAccountRepository.findAllBankAccounts(domain).forEach(this::fetchAccountBalances);
    }

    @Override
    public void fetchAccountBalances(BankAccount bankAccount) {
        try {
            Balance[] allTransactionsFromNodrigen = nodrigenClient
                    .getBalances(UUID.fromString(bankAccount.getExternalId()))
                    .map(b -> b.balances)
                    .orElseGet(() -> new Balance[0]);
            LocalDateTime now = LocalDateTime.now();
            List<NodrigenBankAccountBalance> toSave = new ArrayList<>();
            Arrays.stream(allTransactionsFromNodrigen)
                    .map(balance -> mapBalance(balance, bankAccount, now))
                    .collect(Collectors.toCollection(() -> toSave));
            nodrigenBankAccountBalanceRepository.saveAll(toSave);
        } catch (EUAEndedException exception) {
            nodrigenService.recreateRequisitionIfNeeded(bankAccount);
        }

    }

    private NodrigenBankAccountBalance mapBalance(Balance balance, BankAccount bankAccount, LocalDateTime now) {
        return new NodrigenBankAccountBalance()
                .setBankAccount(bankAccount)
                .setFetchTime(now)
                .setBalanceAmount(mapNodrigenBalanceAmount(balance.balanceAmount))
                .setBalanceType(balance.balanceType)
                .setCreditLimitIncluded(balance.creditLimitIncluded)
                .setLastChangeDateTime(balance.lastChangeDateTime)
                .setLastCommittedTransaction(balance.lastCommittedTransaction)
                .setReferenceDate(balance.referenceDate);
    }


    private void validateTheSameDomain(Domain domain, Domain other) {
        if (!domain.getId().equals(other.getId()))
            throw new AccountsException("Objects are not in the same domain.");
    }

    private void validateBankAccountDoesntHaveOtherAccountAssigned(BankAccount bankAccount) {
        if (bankAccount.getAccount() != null)
            throw new AccountsException("Bank account already assigned to other account.");
    }

    private void validateAccountDoesntHaveOtherBankAccountAssigned(Account account) {
        if (account.getBankAccount() != null)
            throw new AccountsException("An account already assigned to other bank account.");
    }
}
