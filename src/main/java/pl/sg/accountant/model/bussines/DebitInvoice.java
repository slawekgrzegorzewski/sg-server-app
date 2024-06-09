package pl.sg.accountant.model.bussines;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue(value = "DEBIT_INVOICE")
public class DebitInvoice extends Invoice {

}