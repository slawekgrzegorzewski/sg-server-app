package pl.sg.accountant.model;

import pl.sg.application.model.Domain;
import pl.sg.application.model.WithDomain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import java.math.BigDecimal;

@Entity
public class HolidayCurrencies implements WithDomain<HolidayCurrencies> {
    @Id
    @GeneratedValue
    private Integer id;
    private BigDecimal euroConversionRate;
    private BigDecimal kunaConversionRate;
    @ManyToOne
    private Domain domain;

    public HolidayCurrencies() {
    }

    public HolidayCurrencies(Integer id, BigDecimal euroConversionRate, BigDecimal kunaConversionRate, Domain domain) {
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

    public Domain getDomain() {
        return domain;
    }

    public HolidayCurrencies setDomain(Domain domain) {
        this.domain = domain;
        return this;
    }
}
