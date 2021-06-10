package pl.sg.accountant.transport;

import pl.sg.application.transport.DomainTO;
import pl.sg.application.transport.WithDomainTO;

import java.math.BigDecimal;

public class HolidayCurrenciesTO implements WithDomainTO {

    private Integer id;
    private BigDecimal euroConversionRate;
    private BigDecimal kunaConversionRate;
    private DomainTO domain;

    public HolidayCurrenciesTO() {
    }

    public HolidayCurrenciesTO(Integer id, BigDecimal euroConversionRate, BigDecimal kunaConversionRate, DomainTO domain) {
        this.id = id;
        this.euroConversionRate = euroConversionRate;
        this.kunaConversionRate = kunaConversionRate;
        this.domain = domain;
    }

    public Integer getId() {
        return id;
    }

    public HolidayCurrenciesTO setId(Integer id) {
        this.id = id;
        return this;
    }

    public BigDecimal getEuroConversionRate() {
        return euroConversionRate;
    }

    public HolidayCurrenciesTO setEuroConversionRate(BigDecimal euroConversionRate) {
        this.euroConversionRate = euroConversionRate;
        return this;
    }

    public BigDecimal getKunaConversionRate() {
        return kunaConversionRate;
    }

    public HolidayCurrenciesTO setKunaConversionRate(BigDecimal kunaConversionRate) {
        this.kunaConversionRate = kunaConversionRate;
        return this;
    }

    public DomainTO getDomain() {
        return domain;
    }

    public HolidayCurrenciesTO setDomain(DomainTO domain) {
        this.domain = domain;
        return this;
    }
}
