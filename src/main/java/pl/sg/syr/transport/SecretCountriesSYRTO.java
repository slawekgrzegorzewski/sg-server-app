package pl.sg.syr.transport;

import java.time.Year;

public class SecretCountriesSYRTO extends SYRTO<SecretCountriesSYRTO> {
    private Integer numberOfCountries;

    public SecretCountriesSYRTO() {
    }

    public SecretCountriesSYRTO(int id, Year year, Integer numberOfCountries, Integer peak, Integer average, Integer averagePreviousYear, Integer percentIncrease, Integer baptized, Integer averageAuxiliaryPioneers, Integer averagePioneers, Integer numberOfCongregations, Integer totalHours, Integer averageBibleStudies, Integer memorialAttendance, Integer numberOfCountries1) {
        super(id, year, numberOfCountries, peak, average, averagePreviousYear, percentIncrease, baptized, averageAuxiliaryPioneers, averagePioneers, numberOfCongregations, totalHours, averageBibleStudies, memorialAttendance);
        this.numberOfCountries = numberOfCountries1;
    }

    public Integer getNumberOfCountries() {
        return numberOfCountries;
    }

    public SecretCountriesSYRTO setNumberOfCountries(Integer numberOfCountries) {
        this.numberOfCountries = numberOfCountries;
        return this;
    }
}
