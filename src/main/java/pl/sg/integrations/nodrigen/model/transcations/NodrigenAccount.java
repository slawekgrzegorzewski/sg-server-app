package pl.sg.integrations.nodrigen.model.transcations;

import jakarta.persistence.Embeddable;

@Embeddable
public class NodrigenAccount {
    public String bban;
    public String iban;

    public String getBban() {
        return bban;
    }

    public NodrigenAccount setBban(String bban) {
        this.bban = bban;
        return this;
    }

    public String getIban() {
        return iban;
    }

    public NodrigenAccount setIban(String iban) {
        this.iban = iban;
        return this;
    }
}
