package pl.sg.accountant.model.validation;

import pl.sg.accountant.model.ledger.FinancialTransaction;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class NoCurrencyConversionFinancialTransactionValidator implements ConstraintValidator<AccountTransaction, FinancialTransaction> {

    @Override
    public boolean isValid(FinancialTransaction value, ConstraintValidatorContext context) {
        if (value.getSource() != null && value.getDestination() != null
                && value.getSource().getCurrency().equals(value.getDestination().getCurrency())) {
            return value.getCredit().equals(value.getDebit());
        }
        return true;
    }
}
