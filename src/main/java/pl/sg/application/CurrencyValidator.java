package pl.sg.application;

import pl.sg.loans.utils.LoansException;

import javax.money.CurrencyUnit;

public class CurrencyValidator {


    public static void validateCurrency(CurrencyUnit currency, CurrencyUnit otherCurrency, String fieldName) {
        if (!currency.equals(otherCurrency)) {
            throw new LoansException("Currencies of loan and " + fieldName + " doesn't match " + currency + ":" + otherCurrency);
        }
    }
}
