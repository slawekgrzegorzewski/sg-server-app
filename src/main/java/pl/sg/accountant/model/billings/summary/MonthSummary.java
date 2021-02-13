package pl.sg.accountant.model.billings.summary;

import com.google.gson.Gson;
import pl.sg.accountant.model.accounts.Account;
import pl.sg.accountant.model.billings.BillingPeriod;
import pl.sg.accountant.model.billings.PiggyBank;

import javax.persistence.*;
import java.util.List;

@Entity
public class MonthSummary {
    @Id
    @GeneratedValue
    private int id;
    @Column(length = 10000)
    private String data;
    @OneToOne
    @JoinColumn(name = "billing_period_id", referencedColumnName = "id")
    private BillingPeriod billingPeriod;

    public MonthSummary() {
    }

    public MonthSummary(BillingPeriod billingPeriod, List<Account> accounts, List<PiggyBank> piggyBanks) {
        this.billingPeriod = billingPeriod;
        data = new Gson().toJson(MonthSummaryData.of(accounts, piggyBanks));
    }

    public Integer getId() {
        return id;
    }

    public MonthSummaryData getData() {
        return new Gson().fromJson(data, MonthSummaryData.class);
    }

    public BillingPeriod getBillingPeriod() {
        return billingPeriod;
    }

    public MonthSummary setBillingPeriod(BillingPeriod billingPeriod) {
        this.billingPeriod = billingPeriod;
        return this;
    }
}
