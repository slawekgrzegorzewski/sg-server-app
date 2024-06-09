package pl.sg.accountant.model.bussines;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue(value = "BILL_OF_SALE")
public class BillOfSale extends FinancialDocument {

}