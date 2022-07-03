package pl.sg.banks.integrations.nodrigen.services;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Controller;
import pl.sg.application.model.Domain;
import pl.sg.banks.integrations.nodrigen.NodrigenClient;
import pl.sg.banks.integrations.nodrigen.model.NodrigenBankPermission;
import pl.sg.banks.integrations.nodrigen.model.rest.Account;
import pl.sg.banks.integrations.nodrigen.model.rest.AccountDetails;
import pl.sg.banks.integrations.nodrigen.repository.NodrigenBankPermissionRepository;
import pl.sg.banks.integrations.nodrigen.transport.NodrigenPermissionRequest;
import pl.sg.banks.model.BankAccount;
import pl.sg.banks.repositories.BankAccountRepository;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.Optional.ofNullable;

@Controller
public class NodrigenServiceImpl implements NodrigenService {

    private final BankAccountRepository bankAccountRepository;
    private final NodrigenBankPermissionRepository nodrigenBankPermissionRepository;
    private final NodrigenClient nodrigenClient;

    public NodrigenServiceImpl(BankAccountRepository bankAccountRepository, NodrigenBankPermissionRepository nodrigenBankPermissionRepository, NodrigenClient nodrigenClient) {
        this.bankAccountRepository = bankAccountRepository;
        this.nodrigenBankPermissionRepository = nodrigenBankPermissionRepository;
        this.nodrigenClient = nodrigenClient;
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
}
