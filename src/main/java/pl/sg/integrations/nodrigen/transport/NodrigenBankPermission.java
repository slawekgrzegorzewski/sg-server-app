package pl.sg.integrations.nodrigen.transport;

import pl.sg.application.api.DomainSimple;
import pl.sg.application.api.WithDomain;
import pl.sg.banks.transport.BankAccount;

import java.net.URL;
import java.time.LocalDateTime;
import java.util.List;

public class NodrigenBankPermission implements WithDomain {

    private Integer id;
    private DomainSimple domain;
    private String institutionId;
    private LocalDateTime createdAt;
    private LocalDateTime givenAt;
    private LocalDateTime withdrawnAt;
    private String reference;
    private String ssn;
    private URL confirmationLink;
    private List<BankAccount> bankAccounts;

    @Override
    public Integer getId() {
        return id;
    }

    public NodrigenBankPermission setId(Integer id) {
        this.id = id;
        return this;
    }

    @Override
    public DomainSimple getDomain() {
        return domain;
    }

    public NodrigenBankPermission setDomain(DomainSimple domain) {
        this.domain = domain;
        return this;
    }

    public String getInstitutionId() {
        return institutionId;
    }

    public NodrigenBankPermission setInstitutionId(String institutionId) {
        this.institutionId = institutionId;
        return this;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public NodrigenBankPermission setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
        return this;
    }

    public LocalDateTime getGivenAt() {
        return givenAt;
    }

    public NodrigenBankPermission setGivenAt(LocalDateTime givenAt) {
        this.givenAt = givenAt;
        return this;
    }

    public LocalDateTime getWithdrawnAt() {
        return withdrawnAt;
    }

    public NodrigenBankPermission setWithdrawnAt(LocalDateTime withdrawnAt) {
        this.withdrawnAt = withdrawnAt;
        return this;
    }

    public String getReference() {
        return reference;
    }

    public NodrigenBankPermission setReference(String reference) {
        this.reference = reference;
        return this;
    }

    public String getSsn() {
        return ssn;
    }

    public NodrigenBankPermission setSsn(String ssn) {
        this.ssn = ssn;
        return this;
    }

    public URL getConfirmationLink() {
        return confirmationLink;
    }

    public NodrigenBankPermission setConfirmationLink(URL confirmationLink) {
        this.confirmationLink = confirmationLink;
        return this;
    }

    public List<BankAccount> getBankAccounts() {
        return bankAccounts;
    }

    public NodrigenBankPermission setBankAccounts(List<BankAccount> bankAccounts) {
        this.bankAccounts = bankAccounts;
        return this;
    }
}