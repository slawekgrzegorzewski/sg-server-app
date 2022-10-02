package pl.sg.integrations.nodrigen.services;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Controller;
import pl.sg.accountant.model.AccountsException;
import pl.sg.application.model.Domain;
import pl.sg.banks.model.BankAccount;
import pl.sg.banks.repositories.BankAccountRepository;
import pl.sg.integrations.nodrigen.NodrigenClient;
import pl.sg.integrations.nodrigen.model.NodrigenBankPermission;
import pl.sg.integrations.nodrigen.model.rest.Account;
import pl.sg.integrations.nodrigen.model.transcations.NodrigenTransaction;
import pl.sg.integrations.nodrigen.repository.NodrigenBankPermissionRepository;
import pl.sg.integrations.nodrigen.repository.NodrigenTransactionRepository;
import pl.sg.integrations.nodrigen.transport.NodrigenPermissionRequest;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.Optional.ofNullable;

@Controller
public class NodrigenServiceImpl implements NodrigenService {

    private final BankAccountRepository bankAccountRepository;
    private final NodrigenBankPermissionRepository nodrigenBankPermissionRepository;
    private final NodrigenClient nodrigenClient;
    private final NodrigenTransactionRepository nodrigenTransactionRepository;

    public NodrigenServiceImpl(BankAccountRepository bankAccountRepository, NodrigenBankPermissionRepository nodrigenBankPermissionRepository, NodrigenClient nodrigenClient, NodrigenTransactionRepository nodrigenTransactionRepository) {
        this.bankAccountRepository = bankAccountRepository;
        this.nodrigenBankPermissionRepository = nodrigenBankPermissionRepository;
        this.nodrigenClient = nodrigenClient;
        this.nodrigenTransactionRepository = nodrigenTransactionRepository;
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

    @Override
    public void recreateRequisitionIfNeeded(BankAccount bankAccount) {
        nodrigenBankPermissionRepository.getPermissionsGrantedForBankAccount(bankAccount.getDomain(), bankAccount)
                .ifPresent((permission) -> {
                    if (permission.getGivenAt().isBefore(LocalDateTime.now().minusDays(90))) {
                        permission.setWithdrawnAt(LocalDateTime.now());
                        nodrigenBankPermissionRepository.save(permission);
                    }
                });
    }

    @Override
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
                    String externalId = String.valueOf(accountAndDetails.getKey()).toLowerCase(Locale.ROOT);
                    BankAccount bankAccount = ofNullable(bankAccountRepository.getBankAccountByExternalId(externalId))
                            .orElseGet(() -> {
                                BankAccount result = new BankAccount();
                                result.setDomain(domain);
                                result.setExternalId(externalId);
                                result.setIban(account.getIban());
                                result.setBic(account.getBic());
                                result.setCurrency(account.getCurrency());
                                result.setOwner(account.getOwnerName());
                                result.setProduct(account.getProduct());
                                return result;
                            });
                    bankAccount.setBankPermission(permission);
                    return bankAccount;
                }).collect(Collectors.toList());
        bankAccountRepository.saveAll(bankAccountsToCreate);
    }

    @Override
    public void mutuallyCancelTransactions(Domain domain, int firstTransactionId, int secondTransactionId) {
        NodrigenTransaction firstTransaction = nodrigenTransactionRepository.getOne(firstTransactionId);
        NodrigenTransaction secondTransaction = nodrigenTransactionRepository.getOne(secondTransactionId);
        validateSameDomain(domain, firstTransaction.getBankAccount().getDomain());
        validateSameDomain(domain, secondTransaction.getBankAccount().getDomain());
        firstTransaction.setResetIn(secondTransaction);
        nodrigenTransactionRepository.save(firstTransaction);
        nodrigenTransactionRepository.save(secondTransaction);
    }

    @Override
    public void ignoreTransactions(Domain domain, int transactionId) {
        NodrigenTransaction transaction = nodrigenTransactionRepository.getOne(transactionId);
        validateSameDomain(domain, transaction.getBankAccount().getDomain());
        transaction.setIgnored(true);
        nodrigenTransactionRepository.save(transaction);
    }

    @Override
    public List<String> getInstitutionsToRecreate(Domain domain) {
        return nodrigenBankPermissionRepository.findAllPermissions(domain)
                .stream()
                .collect(
                        Collectors.groupingBy(
                                NodrigenBankPermission::getInstitutionId,
                                Collectors.toList()
                        )
                )
                .entrySet().stream()
                .filter(entry -> entry.getValue().stream().map(NodrigenBankPermission::getWithdrawnAt).allMatch(Objects::nonNull))
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
    }

    private void validateSameDomain(Domain first, Domain second) {
        if (!Objects.equals(first.getId(), second.getId())) {
            throw new AccountsException("Payment has to be for the same currency.");
        }
    }
}
