package pl.sg.accountant.model.bussines;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue(value = "BILL_OF_PURCHASE")
public class BillOfPurchase extends FinancialDocument {

    @Override
    public void setOtherParty(OtherParty otherParty) {
        if (!(otherParty instanceof Supplier)) {
            throw new IllegalArgumentException("Only supplier allowed");
        }
        super.setOtherParty(otherParty);
    }
}
