package pl.sg.accountant.model.bussines;

import jakarta.persistence.AttributeOverride;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CompositeType;
import pl.sg.application.database.MonetaryAmountType;

import javax.money.MonetaryAmount;
import java.time.LocalDate;

@Entity
public abstract class Invoice extends FinancialDocument {

    @Getter
    @Setter
    private LocalDate dueTo;

    @Getter
    @Setter
    @AttributeOverride(name = "amount", column = @Column(name = "vat_amount"))
    @AttributeOverride(name = "currency", column = @Column(name = "vat_currency"))
    @CompositeType(MonetaryAmountType.class)
    private MonetaryAmount vat;

}
