package pl.sg.syr.transport;

import java.time.Year;

public class CountrySYRTO extends SYRTO<CountrySYRTO> {
    protected CountryTO country;
    protected Integer population;
    protected Integer ratio1PublisherTo;

    public CountrySYRTO() {
    }

    public CountrySYRTO(int id, Year year, Integer numberOfCountries, Integer peak, Integer average, Integer averagePreviousYear, Integer percentIncrease, Integer baptized, Integer averageAuxiliaryPioneers, Integer averagePioneers, Integer numberOfCongregations, Integer totalHours, Integer averageBibleStudies, Integer memorialAttendance, CountryTO country, Integer population, Integer ratio1PublisherTo) {
        super(id, year, numberOfCountries, peak, average, averagePreviousYear, percentIncrease, baptized, averageAuxiliaryPioneers, averagePioneers, numberOfCongregations, totalHours, averageBibleStudies, memorialAttendance);
        this.country = country;
        this.population = population;
        this.ratio1PublisherTo = ratio1PublisherTo;
    }

    public CountryTO getCountry() {
        return country;
    }

    public CountrySYRTO setCountry(CountryTO country) {
        this.country = country;
        return this;
    }

    public Integer getPopulation() {
        return population;
    }

    public CountrySYRTO setPopulation(Integer population) {
        this.population = population;
        return this;
    }

    public Integer getRatio1PublisherTo() {
        return ratio1PublisherTo;
    }

    public CountrySYRTO setRatio1PublisherTo(Integer ratio1PublisherTo) {
        this.ratio1PublisherTo = ratio1PublisherTo;
        return this;
    }
}

