package pl.sg.checker.model;

import org.hibernate.annotations.Fetch;

import jakarta.persistence.*;
import java.util.Map;

@Entity
public class SaveResultStep extends CheckerStep<SaveResultStep> {
    private String resultVariableName;
    private boolean sendEmailNotification;
    private String emailTitle;
    private String emailHeader;
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "save_result_step_email_tos", joinColumns = {@JoinColumn(name = "id", referencedColumnName = "id")})
    @MapKeyColumn(name = "to_name")
    @Column(name = "to_email")
    private Map<String, String> to;
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "save_result_step_email_ccs", joinColumns = {@JoinColumn(name = "id", referencedColumnName = "id")})
    @MapKeyColumn(name = "cc_name")
    @Column(name = "cc_email")
    private Map<String, String> cc;

    public SaveResultStep() {
    }

    public SaveResultStep(int id, String name, String description, int stepOrder, String resultVariableName, boolean sendEmailNotification, String emailTitle, String emailHeader, Map<String, String> to, Map<String, String> cc) {
        super(id, name, description, stepOrder);
        this.resultVariableName = resultVariableName;
        this.sendEmailNotification = sendEmailNotification;
        this.emailTitle = emailTitle;
        this.emailHeader = emailHeader;
        this.to = to;
        this.cc = cc;
    }

    public String getResultVariableName() {
        return resultVariableName;
    }

    public SaveResultStep setResultVariableName(String resultVariableName) {
        this.resultVariableName = resultVariableName;
        return this;
    }

    public boolean isSendEmailNotification() {
        return sendEmailNotification;
    }

    public SaveResultStep setSendEmailNotification(boolean sendEmailNotification) {
        this.sendEmailNotification = sendEmailNotification;
        return this;
    }

    public String getEmailTitle() {
        return emailTitle;
    }

    public SaveResultStep setEmailTitle(String emailTitle) {
        this.emailTitle = emailTitle;
        return this;
    }

    public String getEmailHeader() {
        return emailHeader;
    }

    public SaveResultStep setEmailHeader(String emailHeader) {
        this.emailHeader = emailHeader;
        return this;
    }

    public Map<String, String> getTo() {
        return to;
    }

    public SaveResultStep setTo(Map<String, String> to) {
        this.to = to;
        return this;
    }

    public Map<String, String> getCc() {
        return cc;
    }

    public SaveResultStep setCc(Map<String, String> cc) {
        this.cc = cc;
        return this;
    }
}
