package pl.sg.integrations.nodrigen.model.rest.transactions;

import pl.sg.integrations.nodrigen.model.rest.balances.Amount;
import pl.sg.integrations.nodrigen.model.rest.balances.Balance;

import java.time.LocalDate;
import java.time.OffsetDateTime;

public class Transaction {
    public String additionalInformation;
    public String additionalInformationStructured;
    public Balance balanceAfterTransaction;
    public String bankTransactionCode;
    public LocalDate bookingDate;
    public OffsetDateTime bookingDateTime;
    public String checkId;
    public Account creditorAccount;
    public String creditorAgent;
    public String creditorId;
    public String creditorName;
    public CurrencyExchange currencyExchange;
    public Account debtorAccount;
    public String debtorAgent;
    public String debtorName;
    public String entryReference;
    public String mandateId;
    public String proprietaryBankTransactionCode;
    public String purposeCode;
    public String remittanceInformationStructured;
    public String[] remittanceInformationStructuredArray;
    public String remittanceInformationUnstructured;
    public String[] remittanceInformationUnstructuredArray;
    public Amount transactionAmount;
    public String transactionId;
    public String ultimateCreditor;
    public String ultimateDebtor;
    public LocalDate valueDate;
    public OffsetDateTime valueDateTime;
}
