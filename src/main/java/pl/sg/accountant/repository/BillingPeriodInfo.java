package pl.sg.accountant.repository;

import pl.sg.accountant.transport.billings.BillingPeriodTO;

import java.util.List;

public class BillingPeriodInfo {
    private final BillingPeriodTO result;
    private final List<BillingPeriodTO> unfinishedBillingPeriods;

    public BillingPeriodInfo(BillingPeriodTO result, List<BillingPeriodTO> unfinishedBillingPeriods) {
        this.result = result;
        this.unfinishedBillingPeriods = unfinishedBillingPeriods;
    }

    public BillingPeriodTO getResult() {
        return result;
    }

    public List<BillingPeriodTO> getUnfinishedBillingPeriods() {
        return unfinishedBillingPeriods;
    }
}
