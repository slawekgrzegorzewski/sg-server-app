package pl.sg.accountant.model.bussines;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue(value = "CREDIT_INVOICE")
public class CreditInvoice extends Invoice {

}