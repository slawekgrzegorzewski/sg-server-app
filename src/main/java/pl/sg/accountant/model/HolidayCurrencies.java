package pl.sg.accountant.model;

import pl.sg.application.model.Domain;
import pl.sg.application.model.WithDomain;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
public class HolidayCurrencies implements WithDomain<HolidayCurrencies> {
    @Id
    @SequenceGenerator(
            name = "commonIdGenerator",
            sequenceName = "hibernate_sequence",
            allocationSize = 1
    )
    @GeneratedValue(generator = "commonIdGenerator")
    private Integer id;
    @Column(columnDefinition = "numeric(19,6) default 0")
    private BigDecimal euroConversionRate;
    @Column(columnDefinition = "numeric(19,6) default 0")
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
