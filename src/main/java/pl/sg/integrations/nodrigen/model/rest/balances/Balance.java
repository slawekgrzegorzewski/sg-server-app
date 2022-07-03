package pl.sg.integrations.nodrigen.model.rest.balances;

import java.time.LocalDate;
import java.time.OffsetDateTime;

public class Balance {
    public Amount balanceAmount;
    public String balanceType;
    public Boolean creditLimitIncluded;
    public OffsetDateTime lastChangeDateTime;
    public String lastCommittedTransaction;
    public LocalDate referenceDate;
}
