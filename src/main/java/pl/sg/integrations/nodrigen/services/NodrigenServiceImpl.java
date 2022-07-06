package pl.sg.integrations.nodrigen.services;

import org.apache.commons.lang3.RandomStringUtils;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Controller;
import pl.sg.accountant.model.AccountsException;
import pl.sg.accountant.model.accounts.FinancialTransaction;
import pl.sg.accountant.repository.FinancialTransactionRepository;
import pl.sg.application.model.Domain;
import pl.sg.banks.model.BankAccount;
import pl.sg.banks.repositories.BankAccountRepository;
import pl.sg.integrations.nodrigen.NodrigenClient;
import pl.sg.integrations.nodrigen.controller.MatchingMode;
import pl.sg.integrations.nodrigen.model.NodrigenBankPermission;
import pl.sg.integrations.nodrigen.model.rest.Account;
import pl.sg.integrations.nodrigen.model.transcations.NodrigenTransaction;
import pl.sg.integrations.nodrigen.repository.NodrigenBankPermissionRepository;
import pl.sg.integrations.nodrigen.repository.NodrigenTransactionRepository;
import pl.sg.integrations.nodrigen.repository.NodrigenTransactionsToImportRepository;
import pl.sg.integrations.nodrigen.transport.NodrigenPermissionRequest;
import pl.sg.integrations.nodrigen.transport.NodrigenTransactionsToImportTO;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.Optional.ofNullable;

@Controller
public class NodrigenServiceImpl implements NodrigenService {

    private final BankAccountRepository bankAccountRepository;
    private final FinancialTransactionRepository financialTransactionRepository;
    private final ModelMapper modelMapper;
    private final NodrigenBankPermissionRepository nodrigenBankPermissionRepository;
    private final NodrigenClient nodrigenClient;
    private final NodrigenTransactionRepository nodrigenTransactionRepository;
    private final NodrigenTransactionsToImportRepository nodrigenTransactionsToImportRepository;

    public NodrigenServiceImpl(BankAccountRepository bankAccountRepository, FinancialTransactionRepository financialTransactionRepository, ModelMapper modelMapper, NodrigenBankPermissionRepository nodrigenBankPermissionRepository, NodrigenClient nodrigenClient, NodrigenTransactionRepository nodrigenTransactionRepository, NodrigenTransactionsToImportRepository nodrigenTransactionsToImportRepository) {
        this.bankAccountRepository = bankAccountRepository;
        this.financialTransactionRepository = financialTransactionRepository;
        this.modelMapper = modelMapper;
        this.nodrigenBankPermissionRepository = nodrigenBankPermissionRepository;
        this.nodrigenClient = nodrigenClient;
        this.nodrigenTransactionRepository = nodrigenTransactionRepository;
        this.nodrigenTransactionsToImportRepository = nodrigenTransactionsToImportRepository;
    }

    @Override
    public NodrigenBankPermission createRequisition(Domain domain, NodrigenPermissionRequest nodrigenPermissionRequest) {
        String reference = RandomStringUtils.randomAlphabetic(256);
        return nodrigenClient.createAgreement(nodrigenPermissionRequest)
                .map(agreementResponse -> nodrigenClient.createRequisition(nodrigenPermissionRequest, agreementResponse.getId(), reference)
                        .map(requisitionResponse -> {
                            NodrigenBankPermission permission = new NodrigenBankPermission();
                            permission.setCreatedAt(LocalDateTime.now());
                            permission.setConfirmationLink(requisitionResponse.getLink());
                            permission.setReference(reference);
                            permission.setInstitutionId(nodrigenPermissionRequest.getInstitutionId());
                            permission.setDomain(domain);
                            permission.setRequisitionId(requisitionResponse.getId());
                            return permission;
                        }).orElseThrow(() -> new RuntimeException("A problem with creation requisition.")))
                .orElseThrow(() -> new RuntimeException("A problem with creation enduser agreement."));
    }

    public void confirmPermission(Domain domain, String reference) {
        NodrigenBankPermission permission = this.nodrigenBankPermissionRepository.findPermissionToConfirm(domain, reference)
                .map(permissionToConfirm -> {
                    permissionToConfirm.setConfirmationLink(null);
                    permissionToConfirm.setReference(null);
                    permissionToConfirm.setSsn(null);
                    permissionToConfirm.setGivenAt(LocalDateTime.now());
                    return nodrigenBankPermissionRepository.save(permissionToConfirm);
                })
                .orElseThrow(() -> new RuntimeException("Can not find permission to confirm"));
        List<BankAccount> bankAccountsToCreate = this.nodrigenClient.getRequisition(permission.getRequisitionId())
                .stream()
                .flatMap(requisition -> Arrays.stream(requisition.getAccounts()))
                .map(accountId -> new AbstractMap.SimpleEntry<>(
                        accountId,
                        nodrigenClient.getAccountDetails(accountId).orElseThrow().getAccount()
                ))
                .map(accountAndDetails -> {
                    Account account = accountAndDetails.getValue();
                    BankAccount bankAccount = new BankAccount();
                    bankAccount.setBankPermission(permission);
                    bankAccount.setDomain(domain);
                    bankAccount.setExternalId(String.valueOf(accountAndDetails.getKey()).toLowerCase(Locale.ROOT));
                    bankAccount.setIban(account.getIban());
                    bankAccount.setBic(account.getBic());
                    bankAccount.setCurrency(account.getCurrency());
                    bankAccount.setOwner(account.getOwnerName());
                    bankAccount.setProduct(account.getProduct());
                    return bankAccount;
                }).collect(Collectors.toList());
        bankAccountRepository.saveAll(bankAccountsToCreate);
    }

    @Override
    public List<NodrigenTransactionsToImportTO> matchNodrigenTransactionsToImport(
            Domain domain,
            int nodrigenTransactionId,
            int financialTransactionId,
            MatchingMode matchingMode) {
        NodrigenTransaction nodrigenTransaction = nodrigenTransactionRepository.getOne(nodrigenTransactionId);
        FinancialTransaction financialTransaction = financialTransactionRepository.getOne(financialTransactionId);
        validateSameDomain(domain, nodrigenTransaction.getBankAccount().getDomain());
        validateSameDomain(domain, ofNullable(financialTransaction.getDestination())
                .or(() -> ofNullable(financialTransaction.getSource()))
                .map(pl.sg.accountant.model.accounts.Account::getDomain).orElseThrow());
        validateNotAssigned(nodrigenTransaction, financialTransaction);
        if (matchingMode == MatchingMode.CREDIT || matchingMode == MatchingMode.BOTH) {
            nodrigenTransaction.setCreditTransaction(financialTransaction);
            financialTransaction.setCreditNodrigenTransaction(nodrigenTransaction);
        }
        if (matchingMode == MatchingMode.DEBIT || matchingMode == MatchingMode.BOTH) {
            nodrigenTransaction.setDebitTransaction(financialTransaction);
            financialTransaction.setDebitNodrigenTransaction(nodrigenTransaction);
        }
        nodrigenTransactionRepository.save(nodrigenTransaction);
        financialTransactionRepository.save(financialTransaction);
        return nodrigenTransactionsToImportRepository.findNodrigenTransactionsToImportByDomainId(domain.getId())
                .stream()
                .map(t -> modelMapper.map(t, NodrigenTransactionsToImportTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<NodrigenTransactionsToImportTO> matchNodrigenTransactionsToImport(
            Domain domain, int nodrigenTransactionId, int secondNodrigenTransactionId,
            int financialTransactionId) {
        NodrigenTransaction debitNodrigenTransaction = nodrigenTransactionRepository.getOne(nodrigenTransactionId);
        NodrigenTransaction creditNodrigenTransaction = nodrigenTransactionRepository.getOne(secondNodrigenTransactionId);
        FinancialTransaction financialTransaction = financialTransactionRepository.getOne(financialTransactionId);


        if ((financialTransaction.getConversionRate().equals(BigDecimal.ONE)
                && debitNodrigenTransaction.getTransactionAmount().getAmount().compareTo(BigDecimal.ZERO) < 0)
                || (!financialTransaction.getConversionRate().equals(BigDecimal.ONE)
                && financialTransaction.getCredit().equals(debitNodrigenTransaction.getTransactionAmount().getAmount()))) {
            NodrigenTransaction cache = debitNodrigenTransaction;
            debitNodrigenTransaction = creditNodrigenTransaction;
            creditNodrigenTransaction = cache;
        }

        validateSameDomain(domain, debitNodrigenTransaction.getBankAccount().getDomain());
        validateSameDomain(domain, creditNodrigenTransaction.getBankAccount().getDomain());
        validateSameDomain(domain, ofNullable(financialTransaction.getDestination())
                .or(() -> ofNullable(financialTransaction.getSource()))
                .map(pl.sg.accountant.model.accounts.Account::getDomain).orElseThrow());
        validateNotAssigned(debitNodrigenTransaction, financialTransaction);
        validateNotAssigned(creditNodrigenTransaction, financialTransaction);

        debitNodrigenTransaction.setDebitTransaction(financialTransaction);
        creditNodrigenTransaction.setCreditTransaction(financialTransaction);
        financialTransaction.setCreditNodrigenTransaction(creditNodrigenTransaction);
        financialTransaction.setDebitNodrigenTransaction(debitNodrigenTransaction);


        if (!financialTransaction.getConversionRate().equals(BigDecimal.ONE)) {
            financialTransaction.setFee(
                    debitNodrigenTransaction.getTransactionAmount().getAmount()
                            .add(debitNodrigenTransaction.getCurrencyExchange().getInstructedAmount().getAmount())
                            .negate());
        }

        nodrigenTransactionRepository.save(debitNodrigenTransaction);
        nodrigenTransactionRepository.save(creditNodrigenTransaction);
        financialTransactionRepository.save(financialTransaction);
        return nodrigenTransactionsToImportRepository.findNodrigenTransactionsToImportByDomainId(domain.getId())
                .stream()
                .map(t -> modelMapper.map(t, NodrigenTransactionsToImportTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<NodrigenTransactionsToImportTO> matchNodrigenTransactionsToImport(
            Domain domain, int nodrigenTransactionId,
            int financialTransactionId) {
        NodrigenTransaction nodrigenTransaction = nodrigenTransactionRepository.getOne(nodrigenTransactionId);
        FinancialTransaction financialTransaction = financialTransactionRepository.getOne(financialTransactionId);
        validateSameDomain(domain, nodrigenTransaction.getBankAccount().getDomain());
        validateSameDomain(domain, ofNullable(financialTransaction.getDestination())
                .or(() -> ofNullable(financialTransaction.getSource()))
                .map(pl.sg.accountant.model.accounts.Account::getDomain).orElseThrow());
        validateNotAssigned(nodrigenTransaction, financialTransaction);

        if (nodrigenTransaction.getTransactionAmount().getAmount().compareTo(BigDecimal.ZERO) > 0) {
            nodrigenTransaction.setCreditTransaction(financialTransaction);
            financialTransaction.setCreditNodrigenTransaction(nodrigenTransaction);
        } else {
            nodrigenTransaction.setDebitTransaction(financialTransaction);
            financialTransaction.setDebitNodrigenTransaction(nodrigenTransaction);
        }

        nodrigenTransactionRepository.save(nodrigenTransaction);
        financialTransactionRepository.save(financialTransaction);
        return nodrigenTransactionsToImportRepository.findNodrigenTransactionsToImportByDomainId(domain.getId())
                .stream()
                .map(t -> modelMapper.map(t, NodrigenTransactionsToImportTO.class))
                .collect(Collectors.toList());
    }

    private void validateNotAssigned(NodrigenTransaction nodrigenTransaction, FinancialTransaction financialTransaction) {
        if (nodrigenTransaction.getCreditTransaction() != null) {
            throw new AccountsException("Nodrigen transaction has credit transaction set.");
        }
        if (nodrigenTransaction.getDebitTransaction() != null) {
            throw new AccountsException("Nodrigen transaction has debit transaction set.");
        }
        if (financialTransaction.getCreditNodrigenTransaction() != null) {
            throw new AccountsException("Financial transaction has credit nodrigen transaction set.");
        }
        if (financialTransaction.getDebitNodrigenTransaction() != null) {
            throw new AccountsException("Financial transaction has debit nodrigen transaction set.");
        }
    }

    private void validateSameDomain(Domain domain, Domain other) {
        if (!domain.getId().equals(other.getId())) {
            throw new AccountsException("Modyfing entity for other domain.");
        }
    }
}
