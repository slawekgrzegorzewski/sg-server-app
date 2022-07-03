package pl.sg.integrations.nodrigen.model;

import pl.sg.application.model.Domain;
import pl.sg.banks.model.BankPermission;

import javax.persistence.*;
import java.net.URL;
import java.util.UUID;

@Entity
public class NodrigenBankPermission extends BankPermission{
    private String institutionId;
    @Column(columnDefinition = "TEXT")
    private String reference;
    @Column(columnDefinition = "TEXT")
    private String ssn;
    private URL confirmationLink;
    private UUID requisitionId;

    @Override
    public NodrigenBankPermission setDomain(Domain domain) {
        this.domain = domain;
        return this;
    }

    public String getInstitutionId() {
        return institutionId;
    }

    public void setInstitutionId(String institutionId) {
        this.institutionId = institutionId;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public String getSsn() {
        return ssn;
    }

    public void setSsn(String ssn) {
        this.ssn = ssn;
    }

    public URL getConfirmationLink() {
        return confirmationLink;
    }

    public void setConfirmationLink(URL confirmationLink) {
        this.confirmationLink = confirmationLink;
    }

    public UUID getRequisitionId() {
        return requisitionId;
    }

    public NodrigenBankPermission setRequisitionId(UUID requisitionId) {
        this.requisitionId = requisitionId;
        return this;
    }
}
