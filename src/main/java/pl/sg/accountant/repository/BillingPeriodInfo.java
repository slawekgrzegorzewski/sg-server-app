package pl.sg.accountant.repository;

import pl.sg.accountant.transport.billings.BillingPeriod;

import java.util.List;

public class BillingPeriodInfo {
    private final BillingPeriod result;
    private final List<BillingPeriod> unfinishedBillingPeriods;

    public BillingPeriodInfo(BillingPeriod result, List<BillingPeriod> unfinishedBillingPeriods) {
        this.result = result;
        this.unfinishedBillingPeriods = unfinishedBillingPeriods;
    }

    public BillingPeriod getResult() {
        return result;
    }

    public List<BillingPeriod> getUnfinishedBillingPeriods() {
        return unfinishedBillingPeriods;
    }
}
