package pl.sg.accountant.transport.billings;

public class MonthSummaryTO {
    private Integer id;
    private String data;
    private BillingPeriodTO billingPeriod;

    public MonthSummaryTO() {
    }

    public MonthSummaryTO(int id, String data, BillingPeriodTO billingPeriod) {
        this.id = id;
        this.data = data;
        this.billingPeriod = billingPeriod;
    }

    public Integer getId() {
        return id;
    }

    public MonthSummaryTO setId(int id) {
        this.id = id;
        return this;
    }

    public String getData() {
        return data;
    }

    public MonthSummaryTO setData(String data) {
        this.data = data;
        return this;
    }

    public BillingPeriodTO getBillingPeriod() {
        return billingPeriod;
    }

    public MonthSummaryTO setBillingPeriod(BillingPeriodTO billingPeriod) {
        this.billingPeriod = billingPeriod;
        return this;
    }
}
