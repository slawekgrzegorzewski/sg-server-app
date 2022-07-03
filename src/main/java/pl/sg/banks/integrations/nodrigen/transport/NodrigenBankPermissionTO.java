package pl.sg.banks.integrations.nodrigen.transport;

import pl.sg.application.transport.DomainTO;
import pl.sg.application.transport.WithDomainTO;
import pl.sg.banks.transport.BankAccountTO;

import java.net.URL;
import java.time.LocalDateTime;
import java.util.List;

public class NodrigenBankPermissionTO implements WithDomainTO {

    private Integer id;
    private DomainTO domain;
    private String institutionId;
    private LocalDateTime createdAt;
    private LocalDateTime givenAt;
    private LocalDateTime withdrawnAt;
    private String reference;
    private String ssn;
    private URL confirmationLink;
    private List<BankAccountTO> bankAccounts;

    @Override
    public Integer getId() {
        return id;
    }

    public NodrigenBankPermissionTO setId(Integer id) {
        this.id = id;
        return this;
    }

    @Override
    public DomainTO getDomain() {
        return domain;
    }

    public NodrigenBankPermissionTO setDomain(DomainTO domain) {
        this.domain = domain;
        return this;
    }

    public String getInstitutionId() {
        return institutionId;
    }

    public NodrigenBankPermissionTO setInstitutionId(String institutionId) {
        this.institutionId = institutionId;
        return this;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public NodrigenBankPermissionTO setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
        return this;
    }

    public LocalDateTime getGivenAt() {
        return givenAt;
    }

    public NodrigenBankPermissionTO setGivenAt(LocalDateTime givenAt) {
        this.givenAt = givenAt;
        return this;
    }

    public LocalDateTime getWithdrawnAt() {
        return withdrawnAt;
    }

    public NodrigenBankPermissionTO setWithdrawnAt(LocalDateTime withdrawnAt) {
        this.withdrawnAt = withdrawnAt;
        return this;
    }

    public String getReference() {
        return reference;
    }

    public NodrigenBankPermissionTO setReference(String reference) {
        this.reference = reference;
        return this;
    }

    public String getSsn() {
        return ssn;
    }

    public NodrigenBankPermissionTO setSsn(String ssn) {
        this.ssn = ssn;
        return this;
    }

    public URL getConfirmationLink() {
        return confirmationLink;
    }

    public NodrigenBankPermissionTO setConfirmationLink(URL confirmationLink) {
        this.confirmationLink = confirmationLink;
        return this;
    }

    public List<BankAccountTO> getBankAccounts() {
        return bankAccounts;
    }

    public NodrigenBankPermissionTO setBankAccounts(List<BankAccountTO> bankAccounts) {
        this.bankAccounts = bankAccounts;
        return this;
    }
}