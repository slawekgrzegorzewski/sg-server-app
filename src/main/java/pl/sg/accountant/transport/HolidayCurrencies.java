package pl.sg.accountant.transport;

import pl.sg.application.api.DomainSimple;
import pl.sg.application.api.WithDomain;

import java.math.BigDecimal;

public class HolidayCurrencies implements WithDomain {

    private Integer id;
    private BigDecimal euroConversionRate;
    private BigDecimal kunaConversionRate;
    private DomainSimple domain;

    public HolidayCurrencies() {
    }

    public HolidayCurrencies(Integer id, BigDecimal euroConversionRate, BigDecimal kunaConversionRate, DomainSimple domain) {
        this.id = id;
        this.euroConversionRate = euroConversionRate;
        this.kunaConversionRate = kunaConversionRate;
        this.domain = domain;
    }

    public Integer getId() {
        return id;
    }

    public HolidayCurrencies setId(Integer id) {
        this.id = id;
        return this;
    }

    public BigDecimal getEuroConversionRate() {
        return euroConversionRate;
    }

    public HolidayCurrencies setEuroConversionRate(BigDecimal euroConversionRate) {
        this.euroConversionRate = euroConversionRate;
        return this;
    }

    public BigDecimal getKunaConversionRate() {
        return kunaConversionRate;
    }

    public HolidayCurrencies setKunaConversionRate(BigDecimal kunaConversionRate) {
        this.kunaConversionRate = kunaConversionRate;
        return this;
    }

    public DomainSimple getDomain() {
        return domain;
    }

    public HolidayCurrencies setDomain(DomainSimple domain) {
        this.domain = domain;
        return this;
    }
}
